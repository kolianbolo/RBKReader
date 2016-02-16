package ru.bolobanov.rbkreader;

import android.content.Context;
import android.util.Log;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.List;

import ru.bolobanov.rbkreader.database.RbkDatabaseHelper;

/**
 * Created by Bolobanov Nikolay on 11.12.15.
 */
public class SynkHtml implements Runnable {

    List<Article> mLoadList;
    private Context mContext;

    public SynkHtml(List<Article> pLoadList, Context pContext) {
        mLoadList = pLoadList;
        mContext = pContext.getApplicationContext();
    }

    @Override
    public void run() {
        RbkDatabaseHelper databaseHelper = new RbkDatabaseHelper(mContext);
        for (Article article: mLoadList) {
            databaseHelper.saveHtml(article.mLink, getHtml(article.mLink));
        }
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
}
