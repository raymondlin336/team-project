package gui.edit_task;

import entity.Habit;
import use_case.habit.edit.EditHabitOutputData;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class EditTaskViewModel{
    private Habit habit;
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    public void updateEditTask(EditHabitOutputData editHabitOutputData) {
        Habit oldhabit = this.habit;
        this.habit =  editHabitOutputData.getHabit();
        pcs.firePropertyChange("EditHabitOutputData", oldhabit, this.habit);
    }
    public void addPropertyChangeListener(PropertyChangeListener l) {
        pcs.addPropertyChangeListener(l);
    }
    public void removePropertyChangeListener(PropertyChangeListener l) {
        pcs.removePropertyChangeListener(l);
    }
    public String getName(){
        return habit.get_next().name;
    }
    public String getDesc() { return habit.get_next().desc; }
    public String getRepeat(){
        return habit.get_next().freq.toString();
    }
    public String getDueDate(){
        return habit.get_next().deadline.toString();
    }
    public int getID(){
        return habit.id;
    }
}
