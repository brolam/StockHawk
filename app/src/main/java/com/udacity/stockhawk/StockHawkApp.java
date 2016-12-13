package com.udacity.stockhawk;

import android.app.Application;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import timber.log.Timber;

public class StockHawkApp extends Application {

    public static DecimalFormat dollarFormat = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.US);
    public static DecimalFormat dollarFormatWithPlus = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.US);
    /*
    By Breno Marques on 08/12/2016.
    Locale.getDefault () does not work with languages that require RTL Layout Support
    percentageFormat = (DecimalFormat) NumberFormat.getPercentInstance(Locale.getDefault());
    */
    public static DecimalFormat percentageFormat = (DecimalFormat) NumberFormat.getPercentInstance(Locale.US);

    @Override
    public void onCreate() {
        super.onCreate();
        dollarFormatWithPlus.setPositivePrefix("+$");
        percentageFormat.setMaximumFractionDigits(2);
        percentageFormat.setMinimumFractionDigits(2);
        percentageFormat.setPositivePrefix("+");

        if (BuildConfig.DEBUG) {
            Timber.uprootAll();
            Timber.plant(new Timber.DebugTree());
        }
    }
}
