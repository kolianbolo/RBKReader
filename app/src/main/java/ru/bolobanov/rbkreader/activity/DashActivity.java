package ru.bolobanov.rbkreader.activity;

import android.content.Intent;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

import ru.bolobanov.rbkreader.R;

@EActivity(R.layout.a_dash)
public class DashActivity extends RbkActivity {

    @Click(R.id.list_button)
    void clickList() {
        startActivity(new Intent(this, ListActivity_.class));
    }

    @Click(R.id.settings_button)
    void clickSettings() {
        startActivity(new Intent(this, RbkPreferenceActivity.class));
    }

    @AfterViews
    public void init(){
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


}