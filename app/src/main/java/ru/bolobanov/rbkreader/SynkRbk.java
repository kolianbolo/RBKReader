package ru.bolobanov.rbkreader;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import ru.bolobanov.rbkreader.database.RbkDatabaseHelper;

/**
 * Created by Bolobanov Nikolay on 10.12.15.
 */
public class SynkRbk implements Runnable {

    private final long mPeriod;
    private Context mContext;
    private String mUrlString;

    public SynkRbk(long refreshMillis, String pUrlString, Context pContext) {
        mPeriod = refreshMillis;
        mContext = pContext.getApplicationContext();
        mUrlString = pUrlString;
    }

    public void run() {
        while (true) {
            load();
            try {
                Thread.sleep(mPeriod);
            } catch (InterruptedException e) {
                return;
            }
        }
    }

    /**
     * @param a элементы этого списка ищутся в
     * @param b этом списке
     * @return выводятся все элементы a не входяшие в список b
     */
    private List<Article> notIncluded(List<Article> a, List<Article> b) {
        List<Article> returned = new ArrayList<>();
        HashSet<String> setB = new HashSet<>();
        for (Article article : b) {
            setB.add(article.mGuid);
        }
        for (Article article : a) {
            if (!setB.contains(article.mGuid)) {
                returned.add(article);
            }
        }
        return returned;
    }


    private void load() {
        printToast("Начало синхронизации");
        try {
            URL url = new URL(mUrlString);

            List<Article> rssArticles = parseRss(url);
            List<Article> databaseArticles = new RbkDatabaseHelper(mContext).getArticles();
            List<Article> newArticles = notIncluded(rssArticles, databaseArticles);
            List<Article> nonActualArticles = notIncluded(databaseArticles, rssArticles);
            RbkDatabaseHelper databaseHelper = new RbkDatabaseHelper(mContext);
            for (Article article : newArticles) {
                databaseHelper.saveArticle(article);
            }
            databaseHelper.deleteHtmls();
            Thread httpThread = new Thread(new SynkHtml(newArticles, mContext));
            httpThread.start();

            for (Article article : nonActualArticles) {
                databaseHelper.deleteArticle(article.mGuid);
            }
            Handler mainHandler = new Handler(mContext.getMainLooper());
            Runnable busMessage = new Runnable() {
                @Override
                public void run() {
                    BusProvider.getInstance().post("refreshed");
                }
            };
            mainHandler.post(busMessage);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            printToast("Синхронизацмя завершена");
        }
    }

    private Article conversionToArticle(Element pElement) {
        String guid = pElement.getElementsByTagName("guid").item(0).getTextContent();
        String title = pElement.getElementsByTagName("title").item(0).getTextContent();
        String link = pElement.getElementsByTagName("link").item(0).getTextContent();
        return new Article(title, guid, link);
    }

    private List<Article>  parseRss(URL url) throws ParserConfigurationException, IOException, SAXException {
        List<Article> returned = new ArrayList<>();
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = dbf.newDocumentBuilder();
        InputSource inputSource = new InputSource(url.openStream());
        Document document = documentBuilder.parse(inputSource);
        document.getDocumentElement().normalize();
        NodeList channelList = document.getElementsByTagName("channel");
        NodeList itemList = channelList.item(0).getChildNodes();
        for (int i = 0; i < itemList.getLength(); i++) {
            if (itemList.item(i).getNodeType() == Node.ELEMENT_NODE) {
                if ("item".equals(((Element) itemList.item(i)).getTagName())) {
                    returned.add(conversionToArticle((Element) itemList.item(i)));
                }
            }
        }
        return returned;
    }

    public String getHtml(String pUrl) {
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(pUrl)
                    .build();

            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                return response.body().string();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void printToast(final String message) {
        Handler mainHandler = new Handler(mContext.getMainLooper());

        Runnable toastMessage = new Runnable() {
            @Override
            public void run() {
                Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
            }
        };
        mainHandler.post(toastMessage);
    }
}
