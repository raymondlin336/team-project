package gui.new_task;

import gui.task.TaskController;

public class NewTaskController extends TaskController {
    public NewTaskController(Boolean log_messages) {
        super(log_messages);
    }
    public void add_habit(int habit_id, String name, String description, String frequency){
        print_log_message("adding habit {ID: " + habit_id + " name: [" + name + "] description: [" + description + "] frequency: [" + frequency + "]}");
    }
}
