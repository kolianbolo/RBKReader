package ru.bolobanov.rbkreader;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import org.androidannotations.annotations.EApplication;
import org.androidannotations.annotations.sharedpreferences.Pref;

/**
 * Created by Bolobanov Nikolay on 10.12.15.
 */
@EApplication
public class RbkReaderApplication extends Application {

    @Override
    public void onCreate() {
        startService(new Intent(this, LoadService_.class));
        super.onCreate();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

}
