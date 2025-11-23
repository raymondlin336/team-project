package main;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Habit {
    public int id;
    public String name;
    public String desc;
    public Freq freq;
    public Date deadline;
    public ArrayList<Integer> completion;
    public int colour;

    static public enum Freq {
        Once, Every_day, Every_week, Every_month;
        @Override
        public String toString() {
            return this.name().replace("_", " ");
        }
    }

    static public class Date {
        public int month;
        public int day;
        public int year;
        public static ArrayList days_in_months = new ArrayList<>(List.of(30, 27, 30, 29, 30, 29, 30, 30, 29, 30, 29, 30));

        public Date(int d, int m, int y) {
            this.day = d;
            this.month = m;
            this.year = y;
        }

        public void increase_date(Freq freq) {
            switch (freq) {
                case Once:
                    break;
                case Every_day:
                    this.increase_date_number(1);
                    break;
                case Every_week:
                    this.increase_date_number(7);
                    break;
                case Every_month:
                    if (this.month++ == 12) {
                        this.month = 0;
                        this.year++;
                    }
                    this.day = Math.max((int)days_in_months.get(this.month), day);
                default:
                    break;
            }
        }

        @Override
        public String toString(){
            return month + "/" + day + "/" + year;
        }

        public void increase_date_number(int num) {
            this.day += num;
            int month_days = (int)days_in_months.get(this.month);
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

    public Habit(int id, String name, String desc, Freq freq, Date deadline, ArrayList<Integer> completion) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.freq = freq;
        this.deadline = deadline;
        this.completion = completion;
        Random random = new Random();
        this.colour = random.nextInt(30) * 12;
    }

    public void update_deadline() {
        this.deadline.increase_date(this.freq);
    }
}