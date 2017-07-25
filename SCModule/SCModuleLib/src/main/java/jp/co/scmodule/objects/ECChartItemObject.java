package jp.co.scmodule.objects;

import org.json.JSONException;
import org.json.JSONObject;

import jp.co.scmodule.utils.SCConstants;

/**
 * Created by VNCCO on 8/14/2015.
 */
public class ECChartItemObject {
    private int in = 0;
    private int out = 0;
    private int percentIn = 0;
    private int percentOut = 0;
    private int month = 0;

    public ECChartItemObject() {
    }

    public int getPercentIn() {
        return percentIn;
    }

    public void setPercentIn(int percentIn) {
        this.percentIn = percentIn;
    }

    public int getPercentOut() {
        return percentOut;
    }

    public void setPercentOut(int percentOut) {
        this.percentOut = percentOut;
    }

    public int getIn() {
        return in;
    }

    public void setIn(int in) {
        this.in = in;
    }

    public int getOut() {
        return out;
    }

    public void setOut(int out) {
        this.out = out;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void parseJSON(JSONObject jsonObject) {
        try {
            if (jsonObject.has(SCConstants.TAG_POINT_IN)) {
                setIn(Math.abs(jsonObject.getInt(SCConstants.TAG_POINT_IN)));
            }
            if (jsonObject.has(SCConstants.TAG_POINT_OUT)) {
                setOut(Math.abs(jsonObject.getInt(SCConstants.TAG_POINT_OUT)));
            }
            if (jsonObject.has(SCConstants.TAG_YEAR_MONTH)) {
                String year_month = jsonObject.getString(SCConstants.TAG_YEAR_MONTH);
                String month = year_month.substring(year_month.length() - 2, year_month.length());
                int monthint = Integer.parseInt(month);
                setMonth(monthint);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
