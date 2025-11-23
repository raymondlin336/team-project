package gui.new_task;

import gui.task.TaskController;
import entity.Habit;
import entity.Freq;

public class NewTaskController extends TaskController {
    public NewTaskController(Boolean log_messages) {
        super(log_messages);
    }
    public void add_habit(String name, String description, String frequency){
        Freq freq = Freq.Once;
        if (frequency == "Daily"){
            freq = Freq.Daily;
        }
        else if (frequency == "Weekly"){
            freq = Freq.Weekly;
        }
        else if (frequency == "Monthly"){
            freq = Freq.Monthly;
        }
        showHomeWindow();
        print_log_message("adding habit {ID: [unassigned] name: [" + name + "] description: [" + description + "] frequency: [" + freq + "]}");
    }
}
