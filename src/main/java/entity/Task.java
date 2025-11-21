package entity;

public class Task {
    public String name;
    public String desc;
    public Freq freq;
    public Date deadline;
    public boolean completed;
    public int id;

    public Task(String name, String desc, Freq freq, Date deadline, int id) {
        this.name = name;
        this.desc = desc;
        this.freq = freq;
        this.deadline = deadline;
        this.completed = false;
    }

    public Task update_deadline() {
        Task x;
        try {
            x = (Task) this.clone();
            x.deadline = x.deadline.increase_date(this.freq);
            x.completed = false;
            return x;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }
}