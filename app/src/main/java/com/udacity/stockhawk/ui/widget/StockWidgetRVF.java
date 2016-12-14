package com.udacity.stockhawk.ui.widget;

import android.database.Cursor;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.Contract;
import com.udacity.stockhawk.ui.StockItemView;

/**
 * The StockWidgetRVF It's a RemoteViewsService to update the quotes list on {@see StockWidget}.
 * By Breno Marques on 12/14/2016.
 */
public class StockWidgetRVF implements RemoteViewsService.RemoteViewsFactory {

    StockWidgetRVS stockWidgetRVS;
    private Cursor cursor = null;

    public StockWidgetRVF(StockWidgetRVS stockWidgetRVS){
        this.stockWidgetRVS = stockWidgetRVS;
    }

    @Override
    public void onCreate() {}

    @Override
    public void onDataSetChanged() {
        if (cursor != null)
            cursor.close();

        this.cursor = stockWidgetRVS.getContentResolver().query(
                Contract.Quote.uri, Contract.Quote.QUOTE_COLUMNS, null, null, Contract.Quote.COLUMN_SYMBOL);
    }

    @Override
    public void onDestroy() {

        if (cursor != null) {
            cursor.close();
            cursor = null;
        }
    }

    @Override
    public int getCount() {
        return (cursor == null) ? 0 : cursor.getCount();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if ((position == AdapterView.INVALID_POSITION) || (cursor == null) || !cursor.moveToPosition(position)) {
            return null;
        }
        RemoteViews remoteViews = new RemoteViews(stockWidgetRVS.getPackageName(), R.layout.list_item_quote);
        StockItemView.fillRemoteViews(stockWidgetRVS.getBaseContext(), cursor, remoteViews);

        return remoteViews;

    }

    @Override
    public RemoteViews getLoadingView() {
        return new RemoteViews(stockWidgetRVS.getPackageName(), R.layout.list_item_quote);
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        if (cursor.moveToPosition(position))
            return cursor.getLong(Contract.Quote.POSITION_ID);
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
