package entity;

import java.util.ArrayList;

public class Habit {
    public ArrayList<Task> tasks = new ArrayList<>();
    public int id;

    public Habit(String name, String desc, Freq freq, Date date, int id) {
        Task task = new Task(name, desc, freq, date, 0);
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
}
