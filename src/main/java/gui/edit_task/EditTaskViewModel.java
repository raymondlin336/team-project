package gui.edit_task;

import entity.Habit;

public class EditTaskViewModel{
    private Habit habit;
    public EditTaskViewModel(Habit habit) {
        super();
        this.habit = habit;
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
