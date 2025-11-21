package entity;

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
                try {
                    d = (Date) this.clone();
                    if (d.month++ == 12) {
                        d.month = 0;
                        d.year++;
                    }
                    d.day = Math.max(days_in_months[d.month], day);
                    return d;
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                    return null;
                }
            default:
                return null;
        }
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
}