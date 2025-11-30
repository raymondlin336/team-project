package entity;

import java.util.ArrayList;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONObject;

public class Habit {
    public ArrayList<Task> tasks = new ArrayList<>();
    public int id;
    public int colour;

    public Habit(int id) {
        this.id = id;
        Random random = new Random();
        this.colour = random.nextInt(30) * 12;
    }

    public Habit(String name, String desc, Freq freq, Date date, int id) {
        Task task = new Task(name, desc, freq, date, 0, false);
        this.tasks.add(task);
        Random random = new Random();
        this.colour = random.nextInt(30) * 12;
    }

    public Habit(int id, int colour) {
        this.id = id;
        this.colour = colour;
    }

    public Habit copy() {
        Habit h = new Habit(this.id, this.colour);
        for (Task t : this.tasks) {
            h.tasks.add(t.copy());
        }
        return h;
    }

    public void complete_most_recent() {
        this.tasks.get(this.tasks.size() - 1).completed = true;
        Task t = this.tasks.get(this.tasks.size() - 1).update_deadline();
        if (t.deadline != null) {
            this.tasks.add(t);
        }
    }

    public void update_deadline() {
        Task t = this.tasks.get(this.tasks.size() - 1).update_deadline();
        if (t.deadline != null) {
            this.tasks.add(t);
        }
    }

    public Task get_next() {
        return this.tasks.get(this.tasks.size() - 1);
    }

    public void change_freq(Freq freq) {
        this.get_next().freq = freq;
    }

    public void change_name(String name) {
        this.get_next().name = name;
    }

    public void change_desc(String desc) {
        this.get_next().desc = desc;
    }

    public Boolean[] get_completion_data() {
        ArrayList<Boolean> arr = new ArrayList<>();
        for (Task task : this.tasks) {
            arr.add(task.completed);
        }
        return arr.toArray(new Boolean[0]);
    }

    public void add_task(Task t) {
        this.tasks.add(t);
    }

    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        JSONArray arr = new JSONArray();
        for (Task t : this.tasks) {
            arr.put(t.toJSON());
        }
        json.put("tasks", arr);
        json.put("id", id);
        json.put("colour", colour);
        return json;
    }

    public static Habit fromJSON(JSONObject json) {
        Habit h = new Habit(json.getInt("id"), json.getInt("colour"));
        for (Object t : json.getJSONArray("tasks")) {
            h.tasks.add(Task.fromJSON((JSONObject) t));
        }
        return h;
    }
}
