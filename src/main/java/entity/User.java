package entity;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

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

    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        JSONArray arr = new JSONArray();
        for (Habit h : this.habits) {
            arr.put(h.toJSON());
        }
        json.put("id", this.id);
        json.put("habits", arr);
        return json;
    }

    public static User fromJSON(JSONObject json) {
        User u = new User(json.getInt("id"));
        for (Object h : json.getJSONArray("habits")) {
            u.habits.add(Habit.fromJSON((JSONObject) h));
        }
        return u;
    }

    @Override
    public boolean equals(Object o) {
        User u = (User) o;
        for (int i = 0; i < u.habits.size(); i++) {
            if (!u.habits.get(i).equals(this.habits.get(i))) {
                return false;
            }
        }
        return (u.id == this.id);
    }
}
