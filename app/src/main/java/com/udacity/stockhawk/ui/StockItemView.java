package com.udacity.stockhawk.ui;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.StockHawkApp;
import com.udacity.stockhawk.accessibility.TalkBackHelper;
import com.udacity.stockhawk.data.Contract;
import com.udacity.stockhawk.data.PrefUtils;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Represents a quote and also allows reuse in more than one activity
 * Created by Breno Marques on 12/13/16.
 */
public class StockItemView extends RecyclerView.ViewHolder {

    @BindView(R.id.symbol)
    TextView symbol;

    @BindView(R.id.price)
    TextView price;

    @BindView(R.id.change)
    TextView change;

    @BindView(R.id.linearLayoutQuoteValues)
    LinearLayout linearLayoutQuoteValues;

    @BindView(R.id.error)
    TextView error;

    StockItemView(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);

    }

    /**
     * The QuoteContent contains only quote values and is reused in populating the {@see StockItemView} StockItemView
     * or RemoteViews {@see com.udacity.stockhawk.ui.widget.StockWidgetRVF}.
     */
    public static class QuoteContent{
        String symbol;
        String strPrice ;
        float rawAbsoluteChange;
        float percentageChange;
        boolean isChangeHigh ;
        String strChange;
        boolean isValid;

        public QuoteContent(Context context, Cursor cursor){
            float price = cursor.getFloat(Contract.Quote.POSITION_PRICE);
            this.isValid = price > 0.00;
            this.symbol = cursor.getString(Contract.Quote.POSITION_SYMBOL).toUpperCase(Locale.getDefault());
            this.strPrice = StockHawkApp.dollarFormat.format(price);
            this.rawAbsoluteChange = cursor.getFloat(Contract.Quote.POSITION_ABSOLUTE_CHANGE);
            this.percentageChange = cursor.getFloat(Contract.Quote.POSITION_PERCENTAGE_CHANGE);
            this.isChangeHigh = rawAbsoluteChange > 0.00;
            this.strChange = PrefUtils.getDisplayMode(context).equals(context.getString(R.string.pref_display_mode_absolute_key)) ? StockHawkApp.dollarFormatWithPlus.format(rawAbsoluteChange) : StockHawkApp.percentageFormat.format(percentageChange / 100);
        }


    }

    public void onBind(Context context, Cursor cursor) {

        QuoteContent quoteContent = new QuoteContent(context, cursor);
        this.symbol.setText(quoteContent.symbol);

        if ( quoteContent.isValid) {
            this.linearLayoutQuoteValues.setVisibility(View.VISIBLE);
            this.error.setVisibility(View.GONE);
            this.price.setText(quoteContent.strPrice);
            this.change.setText(quoteContent.strChange);
            if (quoteContent.isChangeHigh) {
                this.change.setBackgroundResource(R.drawable.percent_change_pill_green);
            } else {
                this.change.setBackgroundResource(R.drawable.percent_change_pill_red);
            }
        /*
         By Breno Marques on 08/12/2016.
         Do Speak the Stock when TalkBack is enabled.
        */
            this.itemView.setContentDescription(TalkBackHelper.getTalkStockDescription(context, quoteContent.symbol, quoteContent.strPrice, quoteContent.strChange, quoteContent.isChangeHigh));
        } else {
            linearLayoutQuoteValues.setVisibility(View.GONE);
            this.error.setVisibility(View.VISIBLE);
            this.error.setText(context.getString(R.string.error_stock_not_found));
        }
    }

    /**
     * Fill a StockItemView on a RemoteViews.
     * @param context inform a context valid.
     * @param cursor inform a quote cursor.
     * @param remoteViews
     */
    public static void fillRemoteViews(Context context, Cursor cursor, RemoteViews remoteViews ){
        QuoteContent quoteContent = new QuoteContent(context, cursor);
        remoteViews.setTextViewText(R.id.symbol, quoteContent.symbol);

        if ( quoteContent.isValid) {
            remoteViews.setViewVisibility(R.id.linearLayoutQuoteValues,View.VISIBLE);
            remoteViews.setViewVisibility(R.id.error,View.GONE);
            remoteViews.setTextViewText(R.id.price, quoteContent.strPrice);
            remoteViews.setTextViewText(R.id.change, quoteContent.strChange);
            if (quoteContent.isChangeHigh) {
                remoteViews.setInt(R.id.change, "setBackgroundResource", R.drawable.percent_change_pill_green);
            } else {
                remoteViews.setInt(R.id.change, "setBackgroundResource", R.drawable.percent_change_pill_red);
            }
            remoteViews.setContentDescription(R.id.linearLayout_item_quote, TalkBackHelper.getTalkStockDescription(context, quoteContent.symbol, quoteContent.strPrice, quoteContent.strChange, quoteContent.isChangeHigh));
            Intent intent = new Intent(context.getApplicationContext(), StockDetailActivity.class);
            intent.putExtra(StockDetailActivity.PARAM_SYMBOL, quoteContent.symbol);
            remoteViews.setOnClickFillInIntent(R.id.linearLayout_item_quote, intent);
        } else {
            remoteViews.setViewVisibility(R.id.linearLayoutQuoteValues,View.GONE);
            remoteViews.setViewVisibility(R.id.error,View.VISIBLE);
            remoteViews.setTextViewText(R.id.error, context.getString(R.string.error_stock_not_found_widget));
        }
    }







}