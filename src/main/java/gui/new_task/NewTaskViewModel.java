package gui.new_task;

import use_case.habit.create.CreateHabitOutputBoundary;
import use_case.habit.create.CreateHabitOutputData;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class NewTaskViewModel {
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    public void updateNewTask(CreateHabitOutputData createHabitOutputData) {
        pcs.firePropertyChange("AddSuccess", null, null);
    }
    public void addPropertyChangeListener(PropertyChangeListener l) {
        pcs.addPropertyChangeListener(l);
    }
    public void removePropertyChangeListener(PropertyChangeListener l) {
        pcs.removePropertyChangeListener(l);
    }
}
