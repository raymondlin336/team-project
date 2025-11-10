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

    public void increase_date(Freq freq) {
        switch (freq) {
            case Once:
                break;
            case Daily:
                this.increase_date_number(1);
                break;
            case Weekly:
                this.increase_date_number(7);
                break;
            case Monthly:
                if (this.month++ == 12) {
                    this.month = 0;
                    this.year++;
                }
                this.day = Math.max(days_in_months.get(this.month), day);
            default:
                break;
        }
    }

    public void increase_date_number(int num) {
        this.day += num;
        int month_days = days_in_months.get(this.month);
        if (this.day > month_days) {
            this.day = this.day % month_days;
            this.month++;
            if (this.month == 12) {
                this.month = 0;
                this.year++;
            }
        }
    }
}

public class Task {
    public String name;
    public String desc;
    public Freq freq;
    public Date deadline;

    public Task(String name, String desc, Freq freq, Date deadline) {
        this.name = name;
        this.desc = desc;
        this.freq = freq;
        this.deadline = deadline;
    }

    public void update_deadline() {
        this.deadline.increase_date(this.freq);
    }
}