package ru.bolobanov.rbkreader;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import org.androidannotations.annotations.EService;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.util.Random;

/**
 * Created by Bolobanov Nikolay on 10.12.15.
 */
@EService
public class LoadService extends Service {

    private Thread mLoadThread;

    @Pref
    PreferencesService_ mPreferences;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (mLoadThread != null) {
            BusProvider.getInstance().unregister(mLoadThread);
            mLoadThread.interrupt();
        }
        final String source = mPreferences.listOfSources().get();
        final String period = mPreferences.listOfPeriods().get();
        mLoadThread = new Thread(new SynkRbk(Integer.parseInt(period) * 60 * 1000L, source, this));
        BusProvider.getInstance().register(mLoadThread);
        mLoadThread.start();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        mLoadThread.interrupt();
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
