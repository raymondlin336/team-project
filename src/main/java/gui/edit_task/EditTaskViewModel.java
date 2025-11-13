package gui.edit_task;

import main.Habit;

public class EditTaskViewModel{
    private Habit habit;
    public EditTaskViewModel(Habit habit) {
        super();
        this.habit = habit;
    }
    public String getName(){
        return habit.name;
    }
    public String getDesc(){
        return habit.desc;
    }
    public String getRepeat(){
        return habit.freq.toString();
    }
    public String getDueDate(){
        return habit.deadline.toString();
    }
    public int getID(){
        return habit.id;
    }
}
