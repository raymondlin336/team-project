package gui.new_task;

import gui.task.TaskController;
import main.Habit;

public class NewTaskController extends TaskController {
    public NewTaskController(Boolean log_messages) {
        super(log_messages);
    }
    public void add_habit(String name, String description, String frequency){
        Habit.Freq freq = Habit.Freq.Once;
        if (frequency == "Every day"){
            freq = Habit.Freq.Every_day;
        }
        else if (frequency == "Every week"){
            freq = Habit.Freq.Every_week;
        }
        else if (frequency == "Every month"){
            freq = Habit.Freq.Every_month;
        }
        print_log_message("adding habit {ID: [unassigned] name: [" + name + "] description: [" + description + "] frequency: [" + freq + "]}");
    }
}
