package com.udacity.stockhawk.ui;

import android.database.Cursor;
import android.graphics.Color;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.udacity.stockhawk.R;
import com.udacity.stockhawk.accessibility.TalkBackHelper;
import com.udacity.stockhawk.data.Contract;
import com.udacity.stockhawk.data.StockProvider;
import com.udacity.stockhawk.data.StockProvider.QuoteHistory;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * The StockDetailActivity show the quote history and detail.
 * By Breno Marques on 12/13/2016.
 */
public class StockDetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    public static final String PARAM_SYMBOL = "symbol";
    private static final int STOCK_LOADER = 0;
    StockItemView stockItemView;

    @BindView(R.id.chart)
    LineChart lineChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.stockItemView = new StockItemView(this.findViewById(R.id.linearLayoutItemQuote));
        ButterKnife.bind(this);
        getSupportLoaderManager().initLoader(STOCK_LOADER, this.getIntent().getExtras(), this);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String symbol = args.getString(PARAM_SYMBOL);

        return new CursorLoader(this,
                Contract.Quote.makeUriForStock(symbol),
                Contract.Quote.QUOTE_COLUMNS,
                null, null, Contract.Quote.COLUMN_SYMBOL);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        if (data.getCount() != 0) {
            data.moveToPosition(0);
            this.stockItemView.onBind(this, data);
            try {
                ChartDataSet chartDataSet = getChartEntities(data.getString(Contract.Quote.POSITION_HISTORY));
                LineDataSet dataSet = new LineDataSet(chartDataSet.entries, "");
                LineData lineData = new LineData(chartDataSet.labels, dataSet);
                dataSet.setColors(ColorTemplate.MATERIAL_COLORS); //
                dataSet.setDrawCubic(true);
                dataSet.setDrawFilled(true);
                lineChart.getXAxis().setTextColor(Color.WHITE);
                lineChart.getAxisLeft().setTextColor(Color.WHITE);
                lineChart.getAxisRight().setTextColor(Color.WHITE);
                lineChart.setData(lineData);
                lineChart.animateY(2500);
                if (chartDataSet.labels.size() > 1  ){
                    String strMinDate = chartDataSet.labels.get(chartDataSet.labels.size()-1);
                    String strMaxDate = chartDataSet.labels.get(0);
                    lineChart.setContentDescription(TalkBackHelper.getTalkStockChartHistory(this, strMinDate, strMaxDate));
                }
            } catch (IOException e) {
                Toast.makeText(this, getString(R.string.error_no_load_quote_history), Toast.LENGTH_LONG).show();
                Timber.e("Error no load quote history: %s", e.getMessage());
            }

        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    /**
     * Represents a DataSet for a {@see com.github.mikephil.charting.charts.LineChart}
     */
    private class ChartDataSet{
        public  ArrayList<Entry>entries;
        public  ArrayList<String> labels;

        public ChartDataSet(ArrayList<Entry>entries, ArrayList<String> labels){
            this.entries = entries;
            this.labels = labels;
        }


    }

    /**
     * Retrieves a {@see ChartDataSet} for the history of a quote, also considering the
     * RTL Layout Support
     * @param stockHistory Inform text in CSV format.
     * @return Retrieves a {@see ChartDataSet}
     */
    private ChartDataSet getChartEntities(String stockHistory) throws IOException {
        boolean rtLayoutSupport = this.getResources().getBoolean(R.bool.rtl_layout_support);
        ArrayList<Entry> entries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();

        List<String[]> QuoteHistories = StockProvider.getQuoteHistories(stockHistory);

        for (int index = 0; index < QuoteHistories.size(); index++) {
            int indexOrder = rtLayoutSupport ? (QuoteHistories.size() - 1) - index : index;
            QuoteHistory quoteClose = new StockProvider.QuoteHistory(QuoteHistories.get(indexOrder));
            entries.add(new Entry(quoteClose.quoteClose, indexOrder));
            labels.add(index, getDateFormatted(quoteClose.date, "dd, MMM/yy"));

        }
        return new ChartDataSet(entries, labels);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * Returns with a date as formatted in the dateFormat {@see SimpleDateFormat}
     * @param date Inform a valid date.
     * @param dateFormat Conforming {@see SimpleDateFormat}
     * @return Text with the date formatted.
     */
    public static String getDateFormatted(Date date, String dateFormat ){
        return new SimpleDateFormat(dateFormat, Locale.ENGLISH).format(date);
    }


}
