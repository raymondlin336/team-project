package entity;

import java.util.ArrayList;

public class Habit {
    public ArrayList<Task> tasks = new ArrayList<>();
    public int id;

    public Habit(String name, String desc, Freq freq, Date date, int id) {
        Task task = new Task(name, desc, freq, date, 0, false);
        this.tasks.add(task);
    }

    public void complete_most_recent() {
        this.tasks.get(this.tasks.size() - 1).completed = true;
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
        return (Boolean[]) arr.toArray();
    }

    public void add_task(Task t) {
        this.tasks.add(t);
    }
}
