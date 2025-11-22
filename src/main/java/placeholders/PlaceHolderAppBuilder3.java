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

public class PlaceHolderAppBuilder3 {
    public static void main(String[] args){
        Habit habit1 = new Habit();
        create_and_attach_tasks(habit1, "Groceries", "Get milk, toast, eggs", Freq.Weekly, new Date(11, 2, 2025), 0, true, 10);

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

        // ScreenManager
        ScreenManager manager = new ScreenManager(editTaskView, newTaskView, homeView, satisticsView);
        homeViewController.addScreenManager(manager);
        editTaskController.addScreenManager(manager);
        newTaskController.addScreenManager(manager);
        statisticsController.addScreenManager(manager);
        manager.showHomeView();
    }
    public static void create_and_attach_tasks(Habit habit, String name, String desc, Freq freq, Date date, int id, Boolean completed, int total){
        for (int i = 0; i < total; i++) {
            Task task = new  Task(name, desc, freq, date, id, completed);
            habit.add_task(task);
        }
    }


}
