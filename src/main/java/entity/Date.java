package entity;

import org.json.JSONObject;

public class Date implements Cloneable {
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
                d.month++;
                if (d.month == 12) {
                    d.month = 0;
                    d.year++;
                }
                d.day = Math.min(days_in_months[d.month], day);
                return d;
            default:
                return null;
        }
    }

    public Date copy() {
        return new Date(this.day, this.month, this.year);
    }

    public Date increase_date_number(int num) {
        // Use copy() instead of clone/try-catch for cleaner code
        Date d = this.copy();
        d.day += num;

        // Fix if out of bounds (since month index starts at 0)
        while (d.month >= 12) {
            d.month -= 12;
            d.year++;
        }

        // LOGIC FIX:
        // Use a while loop instead of 'if' to handle adding large numbers of days.
        // Use subtraction instead of modulo (%) to prevent "Day 0" bugs.
        while (d.day > days_in_months[d.month]) {
            d.day -= days_in_months[d.month];
            d.month++;

            // Check month wrap-around immediately inside the loop
            if (d.month >= 12) {
                d.month = 0;
                d.year++;
            }
        }
        return d;
    }

    public JSONObject toJSON() {
        JSONObject j = new JSONObject();
        j.put("day", this.day);
        j.put("month", this.month);
        j.put("year", this.year);
        return j;
    }

    public static Date fromJSON(JSONObject json) {
        return new Date(json.getInt("day"), json.getInt("month"), json.getInt("year"));
    }

    @Override
    public String toString() {
        return month + "/" + day + "/" + year;
    }

    public static boolean leq(Date d1, Date d2) {
        if (d1.year < d2.year) {
            return true;
        } else if (d1.year == d2.year && d1.month < d2.month) {
            return true;
        } else if (d1.year == d2.year && d1.month == d2.month && d1.day <= d2.day) {
            return true;
        }
        return false;
    }

    public static boolean lessThan(Date d1, Date d2) {
        if (d1.year < d2.year) {
            return true;
        } else if (d1.year == d2.year && d1.month < d2.month) {
            return true;
        } else if (d1.year == d2.year && d1.month == d2.month && d1.day < d2.day) {
            return true;
        }
        return false;
    }

    public static boolean geq(Date d1, Date d2) {
        if (d1.year > d2.year) {
            return true;
        } else if (d1.year == d2.year && d1.month > d2.month) {
            return true;
        } else if (d1.year == d2.year && d1.month == d2.month && d1.day >= d2.day) {
            return true;
        }
        return false;
    }
}