package gui.home;

import entity.Freq;
import gui.ScreenManager;
import gui.task.TaskController;
import entity.Habit;
import use_case.habit.overview.get.GetHabitsInputData;
import use_case.habit.overview.get.GetHabitsInteractor;
import use_case.statistics.get.GetStatisticsInteractor;
import use_case.habit.complete.CompleteHabitTaskInputBoundary;
import use_case.habit.complete.CompleteHabitTaskInputData;

import javax.script.ScriptEngine;

public class HomeViewController extends TaskController {
    private static ScreenManager screenManager;
    private GetHabitsInteractor getHabitsInteractor;
    private CompleteHabitTaskInputBoundary completeHabitInteractor;

    public HomeViewController(Boolean log_messages, GetHabitsInteractor interactor, CompleteHabitTaskInputBoundary completeHabitInteractor) {
        super(log_messages);
        this.getHabitsInteractor = interactor;
        this.completeHabitInteractor = completeHabitInteractor;

    }

    public void onHabitCheckboxClicked(Habit habit) {
        print_log_message("Toggled checkbox for habit {ID: " + habit.id + "}");

        // Create the input data containing the habit ID
        CompleteHabitTaskInputData inputData = new CompleteHabitTaskInputData(habit.id);

        // Execute the Use Case
        completeHabitInteractor.execute(inputData);
    }

    public void addScreenManager(ScreenManager screenManager){
        this.screenManager = screenManager;
    }

    public static void showEditTaskWindow(int habitId){
        screenManager.showEditTaskView(habitId);
    }

    public static void showStatisticsWindow(){
        screenManager.showStatisticsView(); // Needs to be implemented somewhere!
    }

    public static void showAddTaskWindow(){
        screenManager.showAddTaskView();
    }

    public void updateHome(){
        getHabitsInteractor.execute(new GetHabitsInputData());
    }

}
