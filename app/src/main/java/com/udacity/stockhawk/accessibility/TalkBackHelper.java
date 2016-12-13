package com.udacity.stockhawk.accessibility;

import android.content.Context;
import android.view.accessibility.AccessibilityManager;
import android.widget.Toast;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.PrefUtils;

/**
 * TalkBackHelper provides features to help on the integration with Talkback Service.
 * {@see  <a href="https://developer.android.com/guide/topics/ui/accessibility/apps.html">Making Apps More Accessible</a>}
 */
public class TalkBackHelper {

    /**
     * Inform if TalkBack Service is active
     * @param context Inform a valid context
     * @return True if TalkBack Service it is ON
     */
    public static  boolean isEnabled(Context context){
        AccessibilityManager accessibilityManager = (AccessibilityManager) context.getSystemService(Context.ACCESSIBILITY_SERVICE);
        return accessibilityManager.isTouchExplorationEnabled();
    }

    /**
     * Speak the status of the display mode
     * @param context Inform a valid context
     * @param displayModeStatus Inform the display mode status
     */
    public static void talkDisplayModeStatus(Context context, String displayModeStatus){
        if (isEnabled(context) ) {
            Toast.makeText(context, context.getString(R.string.a11y_stock_display_mode_status, displayModeStatus), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Get the ContentDescription from a Stock.
     * @param context Inform a valid context
     * @param symbol
     * @param strPrice Price already formatted
     * @param strChange Change already formatted
     * @param isChangeHigh Inform if the Change it is positive or negative
     * @return
     */
    public static String getTalkStockDescription(Context context, String symbol, String strPrice, String strChange, boolean isChangeHigh){
        if (isEnabled(context) ) {
            if (isChangeHigh){
                return context.getString(R.string.a11y_stock_description_high, symbol, strPrice, strChange);
            } else{
                return context.getString(R.string.a11y_stock_description_low, symbol, strPrice, strChange);
            }
        } else {
            return null;
        }
    }

    /**
     * Get the ContentDescription from Stock Chart History
     * @param context Inform a valid context
     * @param strMinDate Inform a text date min formatted.
     * @param strMaxDate Inform a text date max formatted.
     * @return
     */
    public static String getTalkStockChartHistory(Context context, String strMinDate, String strMaxDate){
        return context.getString(R.string.a11y_stock_detail_activity_line_chart, strMinDate, strMaxDate);
    }

}
