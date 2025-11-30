package gui.home;

import entity.Freq;
import entity.Habit;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;

public class HomeViewModel {
    /// Assume ViewModel accepts arrays of tasks

    public ArrayList<Habit> dailyHabits  = new ArrayList<>();
    public ArrayList<Habit> weeklyHabits = new ArrayList<>();
    public ArrayList<Habit> monthlyHabits = new ArrayList<>();
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    public void updateHabits(ArrayList<Habit> newDailyHabits, ArrayList<Habit> newWeeklyHabits, ArrayList<Habit> newMonthlyHabits){
        ArrayList<Habit> oldDailyHabits = dailyHabits;
        dailyHabits = newDailyHabits;
        pcs.firePropertyChange("habits", oldDailyHabits, newDailyHabits);

        ArrayList<Habit> oldWeeklyHabits = weeklyHabits;
        weeklyHabits = newWeeklyHabits;
        pcs.firePropertyChange("habits", oldWeeklyHabits, newWeeklyHabits);

        ArrayList<Habit> oldMonthlyHabits = monthlyHabits;
        monthlyHabits = newMonthlyHabits;
        pcs.firePropertyChange("habits", oldMonthlyHabits, newMonthlyHabits);
    }

    public void updateSingleHabit(Habit updatedHabit) {
        replaceInList(dailyHabits, updatedHabit);
        replaceInList(weeklyHabits, updatedHabit);
        replaceInList(monthlyHabits, updatedHabit);

        // Fire property change to trigger HomeView.propertyChange -> refreshAll()
        pcs.firePropertyChange("habits", null, null);
    }
    private void replaceInList(ArrayList<Habit> list, Habit habit) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).id == habit.id) {
                list.set(i, habit);
                return;
            }
        }
    }

    public void addPropertyChangeListener(PropertyChangeListener l) {
        pcs.addPropertyChangeListener(l);
    }
    public void removePropertyChangeListener(PropertyChangeListener l) {
        pcs.removePropertyChangeListener(l);
    }
}
