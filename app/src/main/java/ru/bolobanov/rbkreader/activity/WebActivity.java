package ru.bolobanov.rbkreader.activity;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.webkit.WebView;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import ru.bolobanov.rbkreader.Constants;
import ru.bolobanov.rbkreader.R;
import ru.bolobanov.rbkreader.database.RbkDatabaseHelper;

/**
 * Created by Bolobanov Nikolay on 09.12.15.
 */
@EActivity(R.layout.a_web)
public class WebActivity extends RbkActivity {

    @ViewById
    WebView web;

    @AfterViews
    protected void init() {
        Intent intent = getIntent();
        String link = intent.getStringExtra(Constants.LINK_KEY);
        if (link != null) {
            if (!isNetworkAvailable()) {
                RbkDatabaseHelper databaseHelper = new RbkDatabaseHelper(this);
                String html = databaseHelper.getHtml(link);
                if (html != null) {
                    final String mimeType = "text/html";
                    final String encoding = "UTF-8";
                    web.loadDataWithBaseURL("", html, mimeType, encoding, "");
                    Toast.makeText(this, "загружено из кеша", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            web.loadUrl(link);
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

}
