public class User {
    public ArrayList<Habit> habits;
    public int id;

    public User(int id) {
        Super();
        this.habits = new ArrayList();
        this.id = id;
    }

    public void add_habit(Habit h) {
        this.habits.append(h);
    }

    public void remove_habit(Habit h) {
        this.habits.remove(h);
    }

    public ArrayList<Habit> get_habits() {
        return this.habits;
    }
}
