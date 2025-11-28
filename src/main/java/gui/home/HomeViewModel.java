package gui.home;

import entity.Freq;
import entity.Habit;

import java.util.ArrayList;

public class HomeViewModel {
    /// Assume ViewModel accepts arrays of tasks

    public ArrayList<Habit> dailyHabits  = new ArrayList<>();
    public ArrayList<Habit> weeklyHabits = new ArrayList<>();
    public ArrayList<Habit> monthlyHabits = new ArrayList<>();

}
