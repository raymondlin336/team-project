package gui.home;

import entity.Freq;
import gui.ScreenManager;
import gui.task.TaskController;
import entity.Habit;

import javax.script.ScriptEngine;

public class HomeViewController extends TaskController {
    private static ScreenManager screenManager;
    private HomePresenter homePresenter;
    public HomeViewController(Boolean log_messages, HomePresenter presenter /*, MarkHabitDoneUseCase uc */) {
        super(log_messages);
        this.homePresenter = presenter;
        // this.markHabitDoneUseCase = uc;
    }

    public void onHabitCheckboxClicked(Habit habit) {
        // 1) Call the use case to toggle the habit’s done status
        // markHabitDoneUseCase.execute(habit);

        // 2) After use case gives you updated list of habits, re-present them:
        // presenter.presentHabits(updatedHabits);
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
}
