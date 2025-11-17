import java.lang.reflect.Array;

public enum Freq {
    Once, Daily, Weekly, Monthly
}

public class Date {
    public int month;
    public int day;
    public int year;
    static Array days_in_months = new Array(30, 27, 30, 29, 30, 29, 30, 30, 29, 30, 29, 30);

    public Date(int d, int m, int y) {
        this.day = d;
        this.month = m;
        this.year = y;
    }

    public Date increase_date(Freq freq) {
        switch (freq) {
            case Once:
                return null;
                break;
            case Daily:
                return this.increase_date_number(1);
                break;
            case Weekly:
                return this.increase_date_number(7);
                break;
            case Monthly:
                Date d = this.clone();
                if (d.month++ == 12) {
                    d.month = 0;
                    d.year++;
                }
                d.day = Math.max(days_in_months.get(d.month), day);
            default:
                break;
        }
    }

    public Date increase_date_number(int num) {
        Date d = this.clone();
        d.day += num;
        int month_days = days_in_months.get(d.month);
        if (d.day > month_days) {
            d.day = d.day % month_days;
            d.month++;
            if (d.month == 12) {
                d.month = 0;
                d.year++;
            }
        }
        return d;
    }
}

public class Task {
    public String name;
    public String desc;
    public Freq freq;
    public Date deadline;
    public bool completed;

    public Task(String name, String desc, Freq freq, Date deadline) {
        this.name = name;
        this.desc = desc;
        this.freq = freq;
        this.deadline = deadline;
        this.completed = false;
    }

    public Task update_deadline() {
        Task x = this.clone();
        x.deadline = x.deadline.increase_date(this.freq);
        x.completed = false;
        return x;
    }
}