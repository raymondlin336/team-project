package entity;

import org.json.JSONObject;

public class Task {
    public String name;
    public String desc;
    public Freq freq;
    public Date deadline;
    public boolean completed;
    public int id;

    public Task(String name, String desc, Freq freq, Date deadline, int id, boolean completed) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.freq = freq;
        this.deadline = deadline;
        this.completed = completed;
    }

    public Task update_deadline() {
        Task x;
        x = (Task) this.copy();
        x.deadline = x.deadline.increase_date(this.freq);
        x.completed = false;
        return x;
    }

    public Task copy() {
        return new Task(this.name, this.desc, this.freq, this.deadline.copy(), this.id, this.completed);
    }

    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put("name", this.name);
        json.put("desc", this.desc);
        json.put("id", this.id);
        json.put("freq", Freq.getType(this.freq));
        json.put("deadline", this.deadline.toJSON());
        json.put("completed", this.completed);
        return json;
    }

    public static Task fromJSON(JSONObject json) {
        return new Task(json.getString("name"), json.getString("desc"), Freq.fromString(json.getString("freq")),
                Date.fromJSON(json.getJSONObject("deadline")), json.getInt("id"), json.getBoolean("completed"));
    }
}