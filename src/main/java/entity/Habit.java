import java.util.ArrayList;

public class Habit {
    public ArrayList<Task> tasks = new ArrayList<>();
    public int id;

    public Habit(String name, String desc, Freq freq, Date date, int id) {
        Super();
        Task task = new Task(name, desc, freq, date, 0);
        this.tasks.append(task);
    }

    public void complete_most_recent() {
        this.tasks.last().completed = true;
        Date d = this.tasks.last().update_deadline();
        if (d != null) {
            this.tasks.append(this.tasks.last().update_deadline());
        }
    }

    public Task get_next() {
        return self.tasks.last();
    }
}
