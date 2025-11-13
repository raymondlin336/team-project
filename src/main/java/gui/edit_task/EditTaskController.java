package gui.edit_task;

import gui.task.TaskController;
import main.Habit;

public class EditTaskController extends TaskController {
    public EditTaskController(Boolean log_messages) {
        super(log_messages);
    }
    public void save_habit_info(int habit_id, String name, String description, String frequency){
        Habit.Freq freq = Habit.Freq.Once;
        if (frequency == "Every day"){
            freq = Habit.Freq.Every_day;
        }
        else if (frequency == "Every week"){
            freq = Habit.Freq.Every_day;
        }
        else if (frequency == "Every month"){
            freq = Habit.Freq.Every_month;
        }
        print_log_message("updating habit info for habit {ID: " + habit_id + " name: [" + name + "] description: [" + description + "] frequency: [" + freq + "]}");
    }
    public void delete_habit(int habit_id){
        print_log_message("deleting habit {ID: " + habit_id + "} and closing the window");
    }
}
