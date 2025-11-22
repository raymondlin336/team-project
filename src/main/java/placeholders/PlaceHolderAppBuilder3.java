package placeholders;

import entity.Habit;
import entity.Task;
import entity.Freq;
import entity.Date;
import gui.ScreenManager;
import gui.edit_task.EditTaskController;
import gui.edit_task.EditTaskView;
import gui.edit_task.EditTaskViewModel;
import gui.home.HomeView;
import gui.home.HomeViewController;
import gui.home.HomeViewModel;
import gui.new_task.NewTaskController;
import gui.new_task.NewTaskView;
import gui.statistics.StatisticsController;
import gui.statistics.StatisticsView;
import gui.statistics.StatisticsViewModel;

import java.util.ArrayList;

public class PlaceHolderAppBuilder3 {
    public static void main(String[] args){
        ArrayList<Habit> habits = new ArrayList<Habit>();

        Habit habit1 = new Habit(0);
        create_and_attach_tasks(habit1, "Groceries", "Get milk, toast, eggs", Freq.Weekly, new Date(29, 11, 2025), true, 5);
        create_and_attach_tasks(habit1, "Groceries", "Get milk, toast, eggs", Freq.Weekly, new Date(29, 11, 2025), false, 2);
        create_and_attach_tasks(habit1, "Groceries", "Get milk, toast, eggs", Freq.Weekly, new Date(29, 11, 2025), true, 2);
        Habit habit2 = new Habit(1);
        create_and_attach_tasks(habit2, "Workout", "Run for 30 mins", Freq.Daily, new Date(4, 12, 2025), true, 33);
        create_and_attach_tasks(habit2, "Workout", "Run for 30 mins", Freq.Daily, new Date(4, 12, 2025), false, 8);
        create_and_attach_tasks(habit2, "Workout", "Run for 30 mins", Freq.Daily, new Date(4, 12, 2025), true, 7);
        create_and_attach_tasks(habit2, "Workout", "Run for 30 mins", Freq.Daily, new Date(4, 12, 2025), false, 4);
        create_and_attach_tasks(habit2, "Workout", "Run for 30 mins", Freq.Daily, new Date(4, 12, 2025), true, 28);
        Habit habit3 = new Habit(2);
        create_and_attach_tasks(habit3, "Basketball with friends", "Sumaid, Henry, and Kevin", Freq.Monthly, new Date(8, 12, 2025), true, 1);
        create_and_attach_tasks(habit3, "Basketball with friends", "Sumaid, Henry, and Kevin", Freq.Monthly, new Date(8, 12, 2025), false, 1);
        create_and_attach_tasks(habit3, "Basketball with friends", "Sumaid, Henry, and Kevin", Freq.Monthly, new Date(8, 12, 2025), true, 2);
        Habit habit4 = new Habit(3);
        create_and_attach_tasks(habit4, "Doctor's", "Appointment at 4pm", Freq.Once, new Date(10, 12, 2025), false, 1);


        habits.add(habit1);
        habits.add(habit2);
        habits.add(habit3);
        habits.add(habit4);

        // Editing a task
        EditTaskController editTaskController = new EditTaskController(true);
        EditTaskViewModel editTaskViewModel = new EditTaskViewModel(habit1);
        EditTaskView editTaskView = new EditTaskView(editTaskViewModel, editTaskController);

        // Adding a task
        NewTaskController newTaskController = new NewTaskController(true);
        NewTaskView newTaskView = new NewTaskView(newTaskController);

        // Statistics
        StatisticsViewModel vm = new StatisticsViewModel(habits);
        StatisticsController statisticsController = new StatisticsController();
        StatisticsView satisticsView = new StatisticsView("Statistics", vm, statisticsController);

        // Homepage
        HomeViewModel test = new HomeViewModel(habits);
        HomeViewController homeViewController = new HomeViewController(true);
        HomeView homeView = new HomeView(test, homeViewController);
//
        // ScreenManager
        ScreenManager manager = new ScreenManager(editTaskView, newTaskView, homeView, satisticsView);
        homeViewController.addScreenManager(manager);
        editTaskController.addScreenManager(manager);
        newTaskController.addScreenManager(manager);
        statisticsController.addScreenManager(manager);
        manager.showStatisticsView();
    }
    public static void create_and_attach_tasks(Habit habit, String name, String desc, Freq freq, Date date, Boolean completed, int total){
        for (int i = 0; i < total; i++) {
            Task task = new  Task(name, desc, freq, date, i, completed);
            habit.add_task(task);
        }
    }


}
