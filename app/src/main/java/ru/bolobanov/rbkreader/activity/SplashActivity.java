package ru.bolobanov.rbkreader.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

import ru.bolobanov.rbkreader.R;

/**
 * Created by Bolobanov Nikolay on 09.12.15.
 */

@EActivity(R.layout.a_splash)
public class SplashActivity extends Activity {

    @AfterViews
    public void transition(){
        new WaitAsyncTask().execute();
    }

    class WaitAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Thread.sleep(1000 * 2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void param) {
            Intent intent = new Intent(SplashActivity.this, DashActivity_.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }



    }
}
