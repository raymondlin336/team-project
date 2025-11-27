package data_access.habits;

import java.util.List;
import java.util.Optional;

import entity.*;
import use_case.habit.HabitDataAccessInterface;

public class HabitDataAccess implements HabitDataAccessInterface {
    public User user;
    public JSONIO io;
    public int id;

    public HabitDataAccess(String path, int id) {
        this.user = new User(id);
        this.io = new JSONIO(path);
        this.id = id;
    }

    private void load() {
        this.user = this.io.getUser();
    }

    private void save() {
        this.io.saveUser(this.user);
    }

    @Override
    public Habit save(Habit habit) {
        this.load();
        user.habits.add(habit);
        this.save();
        return habit;
    }

    @Override
    public Optional<Habit> findById(int id) {
        this.load();
        for (Habit h : this.user.habits) {
            if (h.id == id) {
                return Optional.of(h);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Habit> findAll() {
        this.load();
        return this.user.habits;
    }

    @Override
    public void deleteById(int id) {
        this.load();
        int ind = -1;
        for (int i = 0; i < this.user.habits.size(); i++) {
            Habit h = this.user.habits.get(i);
            if (h.id == id) {
                ind = i;
            }
        }
        if (ind >= 0) {
            this.user.habits.remove(ind);
        }
        this.save();
    }

    @Override
    public int getNextId() {
        this.load();
        int curr_id = 0;
        for (Habit h : this.user.habits) {
            if (curr_id < h.id) {
                curr_id = h.id;
            }
        }
        return curr_id + 1;
    }

}
