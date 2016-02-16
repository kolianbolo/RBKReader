package ru.bolobanov.rbkreader;

import org.androidannotations.annotations.sharedpreferences.DefaultString;
import org.androidannotations.annotations.sharedpreferences.SharedPref;

/**
 * Created by Bolobanov Nikolay on 11.12.15.
 */

@SharedPref(value = SharedPref.Scope.APPLICATION_DEFAULT)
public interface PreferencesService {

    @DefaultString("http://static.feed.rbc.ru/rbc/internal/rss.rbc.ru/rbc.ru/mainnews.rss")
    String listOfSources();

    @DefaultString("10")
    String listOfPeriods();


}
