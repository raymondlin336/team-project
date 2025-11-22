package gui.edit_task;

import gui.task.TaskController;
import entity.Habit;
import entity.Freq;

public class EditTaskController extends TaskController {
    public EditTaskController(Boolean log_messages) {
        super(log_messages);
    }
    public void save_habit_info(int habit_id, String name, String description, String frequency){
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
        print_log_message("updating habit info for habit {ID: " + habit_id + " name: [" + name + "] description: [" + description + "] frequency: [" + freq + "]}");
    }
    public void delete_habit(int habit_id){
        showHomeWindow();
        print_log_message("deleting habit {ID: " + habit_id + "} and closing the window");
    }
}
