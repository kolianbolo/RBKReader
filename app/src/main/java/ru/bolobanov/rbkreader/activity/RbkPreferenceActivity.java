package ru.bolobanov.rbkreader.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import ru.bolobanov.rbkreader.fragment.RbkPreferenceFragment;

/**
 * Created by Bolobanov Nikolay on 10.12.15.
 */
public class RbkPreferenceActivity extends RbkActivity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new RbkPreferenceFragment()).commit();

    }
}
