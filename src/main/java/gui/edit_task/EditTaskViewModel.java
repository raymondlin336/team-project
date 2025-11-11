package gui.edit_task;

import placeholders.PlaceholderTask;

public class EditTaskViewModel{
    private PlaceholderTask task;
    public EditTaskViewModel(PlaceholderTask task) {
        super();
        this.task = task;
    }
    public String getName(){
        return task.name;
    }
    public String getDesc(){
        return task.description;
    }
    public String getRepeat(){
        return task.repeat;
    }
    public String getDueDate(){
        return task.due_date;
    }
}
