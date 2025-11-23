package entity;

public class Task {
    public String name;
    public String desc;
    public Freq freq;
    public Date deadline;
    public boolean completed;
    public int id;

    public Task(String name, String desc, Freq freq, Date deadline, int id, boolean completed) {
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
}