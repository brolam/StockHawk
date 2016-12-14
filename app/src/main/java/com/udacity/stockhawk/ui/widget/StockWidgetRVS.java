package com.udacity.stockhawk.ui.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;


/**
 * The StockWidgetRVS It's a RemoteViewsService to manager the quotes list update on {@see StockWidget}.
 * By Breno Marques on 12/14/2016.
 */
public class StockWidgetRVS extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new StockWidgetRVF(this);
    }
}
