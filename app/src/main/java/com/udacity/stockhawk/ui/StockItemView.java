package com.udacity.stockhawk.ui;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.View;
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
 * Created by brenomar on 12/13/16.
 */
public class StockItemView extends RecyclerView.ViewHolder {

    @BindView(R.id.symbol)
    TextView symbol;

    @BindView(R.id.price)
    TextView price;

    @BindView(R.id.change)
    TextView change;

    StockItemView(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void onBind(Context context, Cursor cursor) {

        String symbol = cursor.getString(Contract.Quote.POSITION_SYMBOL).toUpperCase(Locale.getDefault());
        String strPrice = StockHawkApp.dollarFormat.format(cursor.getFloat(Contract.Quote.POSITION_PRICE));
        float rawAbsoluteChange = cursor.getFloat(Contract.Quote.POSITION_ABSOLUTE_CHANGE);
        float percentageChange = cursor.getFloat(Contract.Quote.POSITION_PERCENTAGE_CHANGE);

        boolean isChangeHigh = rawAbsoluteChange > 0.00;
        String strChange = PrefUtils.getDisplayMode(context).equals(context.getString(R.string.pref_display_mode_absolute_key)) ? StockHawkApp.dollarFormatWithPlus.format(rawAbsoluteChange) : StockHawkApp.percentageFormat.format(percentageChange / 100);

        this.symbol.setText(symbol);
        this.price.setText(strPrice);
        this.change.setText(strChange);

        if (isChangeHigh) {
            this.change.setBackgroundResource(R.drawable.percent_change_pill_green);
        } else {
            this.change.setBackgroundResource(R.drawable.percent_change_pill_red);
        }
        /*
         By Breno Marques on 08/12/2016.
         Do Speak the Stock when TalkBack is enabled.
        */
        this.itemView.setContentDescription(TalkBackHelper.getTalkStockDescription(context, symbol, strPrice, strChange, isChangeHigh  ));


    }



}