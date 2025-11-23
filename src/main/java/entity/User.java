package entity;

import java.util.ArrayList;

public class User {
    public ArrayList<Habit> habits;
    public int id;

    public User(int id) {
        this.habits = new ArrayList<Habit>();
        this.id = id;
    }

    public void add_habit(Habit h) {
        this.habits.add(h);
    }

    public void remove_habit(Habit h) {
        this.habits.remove(h);
    }

    public ArrayList<Habit> get_habits() {
        return this.habits;
    }

    public User copy() {
        User u = new User(this.id);
        for (Habit h : this.habits) {
            u.habits.add(h.copy());
        }
        return u;
    }
}
