package gui.statistics;

import entity.Freq;
import entity.Habit;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;

public class StatisticsViewModel {
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    private ArrayList<Habit> habits = new ArrayList<Habit>();
    public int numOfTasks(){
        return this.habits.size();
    }
    public Habit getTask(int i){
        return this.habits.get(i);
    }
    public void updateHabits(ArrayList<Habit> newhabits){
        ArrayList<Habit> oldHabits = habits;
        habits = newhabits;
        pcs.firePropertyChange("habits", oldHabits, newhabits);
    }
    public void addPropertyChangeListener(PropertyChangeListener l) {
        pcs.addPropertyChangeListener(l);
    }
    public void removePropertyChangeListener(PropertyChangeListener l) {
        pcs.removePropertyChangeListener(l);
    }
}
