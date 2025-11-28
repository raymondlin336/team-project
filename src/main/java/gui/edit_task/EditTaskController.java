package gui.edit_task;

import entity.Freq;
import gui.ScreenManager;
import gui.task.TaskController;
import entity.Habit;
import use_case.habit.delete.DeleteHabitInputData;
import use_case.habit.delete.DeleteHabitInteractor;
import use_case.habit.edit.EditHabitInputData;
import use_case.habit.edit.EditHabitInteractor;

public class EditTaskController extends TaskController {
    private EditHabitInteractor editHabitInteractor;
    private DeleteHabitInteractor deleteHabitInteractor;
    public EditTaskController(Boolean log_messages, EditHabitInteractor editHabitInteractor, DeleteHabitInteractor deleteHabitInteractor) {
        super(log_messages);
        this.editHabitInteractor = editHabitInteractor;
        this.deleteHabitInteractor = deleteHabitInteractor;
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
        updateEditTask(habit_id, name, description, freq);
        print_log_message("updating habit info for habit {ID: " + habit_id + " name: [" + name + "] description: [" + description + "] frequency: [" + freq + "]}");
    }
    public void delete_habit(int habit_id){
        deleteHabitInteractor.execute(new DeleteHabitInputData(habit_id));
        print_log_message("deleting habit {ID: " + habit_id + "} and closing the window");
    }
    public void addScreenManager(ScreenManager screenManager){
        this.screenManager = screenManager;
    }
    public void updateEditTask(int habitId, String name, String description, Freq freq){
        editHabitInteractor.execute(new EditHabitInputData(habitId, name, description, freq));
    }
    public void firstShowEditTask(int habitId){
        editHabitInteractor.first_execute(habitId);
    }
}
