package entity;

import org.json.JSONObject;

public class Date {
    public int month;
    public int day;
    public int year;
    static int[] days_in_months = { 30, 27, 30, 29, 30, 29, 30, 30, 29, 30, 29, 30 };

    public Date(int d, int m, int y) {
        this.day = d;
        this.month = m;
        this.year = y;
    }

    public Date increase_date(Freq freq) {
        switch (freq) {
            case Once:
                return null;
            case Daily:
                return this.increase_date_number(1);
            case Weekly:
                return this.increase_date_number(7);
            case Monthly:
                Date d;
                d = (Date) this.copy();
                if (d.month++ == 12) {
                    d.month = 0;
                    d.year++;
                }
                d.day = Math.max(days_in_months[d.month], day);
                return d;
            default:
                return null;
        }
    }

    public Date copy() {
        return new Date(this.day, this.month, this.day);
    }

    public Date increase_date_number(int num) {
        Date d;
        try {
            d = (Date) this.clone();
            d.day += num;
            int month_days = days_in_months[d.month];
            if (d.day > month_days) {
                d.day = d.day % month_days;
                d.month++;
                if (d.month == 12) {
                    d.month = 0;
                    d.year++;
                }
            }
            return d;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public JSONObject toJSON() {
        JSONObject j = new JSONObject();
        j.put("day", this.day);
        j.put("month", this.month);
        j.put("year", this.year);
        return j;
    }

    public static Date fromJSON(JSONObject json) {
        return new Date(json.getInt("year"), json.getInt("month"), json.getInt("year"));
    }

    @Override
    public String toString() {
        return month + "/" + day + "/" + year;
    }
}