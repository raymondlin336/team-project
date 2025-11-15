package gui.Home;

import main.Habit;
import placeholders.PlaceholderTask;

import java.util.ArrayList;

public class HomeViewModel {
    /// Assume ViewModel accepts arrays of tasks

    public ArrayList<Habit> dailyHabits;
    public ArrayList<Habit> weeklyHabits;
    public ArrayList<Habit> monthlyHabits;

    public HomeViewModel(ArrayList<Habit> habits) {
        for (Habit habit : habits) {
            if (habit.freq == Habit.Freq.Every_day) {
                this.dailyHabits.add(habit);
            } else if (habit.freq == Habit.Freq.Every_week) {
                this.weeklyHabits.add(habit);
            } else if (habit.freq == Habit.Freq.Every_month) {
                this.monthlyHabits.add(habit);
            }
        }
    }
}
