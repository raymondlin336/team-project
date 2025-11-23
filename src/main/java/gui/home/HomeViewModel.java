package gui.home;

import entity.Freq;
import entity.Habit;

import java.util.ArrayList;

public class HomeViewModel {
    /// Assume ViewModel accepts arrays of tasks

    public ArrayList<Habit> dailyHabits  = new ArrayList<>();
    public ArrayList<Habit> weeklyHabits = new ArrayList<>();
    public ArrayList<Habit> monthlyHabits = new ArrayList<>();

    public HomeViewModel(ArrayList<Habit> habits) {
        for (Habit habit : habits) {
            if (habit.get_next().freq == Freq.Daily) {
                this.dailyHabits.add(habit);
            } else if (habit.get_next().freq == Freq.Weekly) {
                this.weeklyHabits.add(habit);
            } else if (habit.get_next().freq == Freq.Monthly) {
                this.monthlyHabits.add(habit);
            }
        }
    }
}
