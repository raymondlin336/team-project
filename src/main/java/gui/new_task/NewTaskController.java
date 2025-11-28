package gui.new_task;

import entity.Freq;
import gui.task.TaskController;
import use_case.habit.create.CreateHabitInputData;
import use_case.habit.create.CreateHabitInteractor;

public class NewTaskController extends TaskController {
    private CreateHabitInteractor createHabitInteractor;
    public NewTaskController(Boolean log_messages, CreateHabitInteractor createHabitInteractor) {
        super(log_messages);
        this.createHabitInteractor = createHabitInteractor;
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
        updateNewHabit(new CreateHabitInputData(name, description, freq));
        print_log_message("adding habit {ID: [unassigned] name: [" + name + "] description: [" + description + "] frequency: [" + freq + "]}");
    }
    public void updateNewHabit(CreateHabitInputData createHabitInputData){
        createHabitInteractor.execute(createHabitInputData);
    }
}
