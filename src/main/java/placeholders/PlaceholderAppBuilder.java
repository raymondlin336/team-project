package placeholders;

import gui.edit_task.EditTaskController;
import gui.edit_task.EditTaskView;
import gui.edit_task.EditTaskViewModel;
import gui.home.HomeView;
import gui.home.HomeViewModel;
import gui.statistics.StatisticsController;
import gui.statistics.StatisticsView;
import gui.statistics.StatisticsViewModel;
import main.Habit;

import java.util.ArrayList;
import java.util.List;

public class PlaceholderAppBuilder {
    public static void main(String[] args) {
        Habit habit1 = new Habit(0, "Groceries", "Get milk, toast, eggs", Habit.Freq.Every_week, new Habit.Date(10, 15, 2025), new ArrayList<>(List.of(1, 0, 0, 1, 0)));
        Habit habit2 = new Habit(1, "Workout", "Run for 30 mins", Habit.Freq.Every_day, new Habit.Date(10, 11, 2025), new ArrayList<>(List.of(5, 7, 2, 3, 0, 2, 1, 0, 0, 7, 2, 3, 7, 5, 1, 2)));
        Habit habit3 = new Habit(2, "Basketball with friends", "Sumaid, Henry, and Kevin", Habit.Freq.Every_month, new Habit.Date(10, 30, 2025), new ArrayList<>(List.of(0, 1)));
        Habit habit4 = new Habit(3, "Doctor's", "Appointment at 4pm", Habit.Freq.Once, new Habit.Date(10, 21, 2025), new ArrayList<>());
        ArrayList<Habit> habits = new ArrayList<>(List.of(habit1, habit2, habit3, habit4));
        // Editing a task
//        EditTaskController editTaskController = new EditTaskController(true);
//        EditTaskViewModel editTaskViewModel = new EditTaskViewModel(habit1);
//        EditTaskView view = new EditTaskView(editTaskViewModel, editTaskController);
//        view.setVisible();
//      // Adding a task
//        NewTaskController newTaskController = new NewTaskController(true);
//        NewTaskView newTaskView = new NewTaskView(newTaskController);
//        newTaskView.setVisible();
        // Statistics
        StatisticsViewModel vm = new StatisticsViewModel(habits);
        StatisticsController ct = new StatisticsController();
        StatisticsView stat = new StatisticsView("Statistics", vm, ct);
        stat.setVisible();
        // Homepage
//        HomeViewModel test = new HomeViewModel(habits);
//        HomeView homeTest = new HomeView(test);
//        homeTest.show();
    }
}
