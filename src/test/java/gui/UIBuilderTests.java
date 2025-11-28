package gui;

import data_access.habits.HabitDataAccess;
import entity.Date;
import entity.Freq;
import entity.Habit;
import entity.Task;
import gui.home.HomeView;
import gui.home.HomeViewController;
import gui.home.HomeViewModel;
import gui.new_task.NewTaskController;
import gui.new_task.NewTaskPresenter;
import gui.new_task.NewTaskView;
import gui.new_task.NewTaskViewModel;
import gui.statistics.StatisticsController;
import gui.statistics.StatisticsPresenter;
import gui.statistics.StatisticsView;
import gui.statistics.StatisticsViewModel;
import org.junit.Test;
import use_case.habit.create.CreateHabitInteractor;
import use_case.habit.create.CreateHabitOutputData;
import use_case.habit.edit.EditHabitInteractor;
import use_case.statistics.get.GetStatisticsInteractor;

import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

public class UIBuilderTests {

    @Test
    public void UIBuilderTest() {
        System.out.println("UIBuilderTest");
        ArrayList<Habit> habits = createTestHabits();

        HabitDataAccess dataAccess = new HabitDataAccess("src/main/java/placeholders/placeholder_json.json", 0);

        NewTaskViewModel newTaskViewModel = new NewTaskViewModel();
        NewTaskPresenter newTaskPresenter = new NewTaskPresenter(newTaskViewModel);
        CreateHabitInteractor createHabitInteractor = new CreateHabitInteractor(dataAccess, newTaskPresenter);
        NewTaskController newTaskController = new NewTaskController(true, createHabitInteractor);
        NewTaskView newTaskView = new NewTaskView(newTaskController, newTaskViewModel);

        StatisticsViewModel statisticsViewModel = new StatisticsViewModel();
        StatisticsPresenter statisticsPresenter = new StatisticsPresenter(statisticsViewModel);
        GetStatisticsInteractor getStatisticsInteractor = new GetStatisticsInteractor(dataAccess, statisticsPresenter);
        StatisticsController statisticsController = new StatisticsController(getStatisticsInteractor);
        StatisticsView satisticsView = new StatisticsView("Statistics", statisticsViewModel, statisticsController);

        HomeViewModel test = new HomeViewModel(habits);
        HomeViewController homeViewController = new HomeViewController(true);
        HomeView homeView = new HomeView(test, homeViewController);

        ScreenManager manager = new ScreenManager(dataAccess, newTaskView, newTaskController, homeView, satisticsView, statisticsController);
        homeViewController.addScreenManager(manager);
        newTaskController.addScreenManager(manager);
        statisticsController.addScreenManager(manager);

        manager.showHomeView();

        try {
            Thread.sleep(600_000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<Habit> createTestHabits() {
        ArrayList<Habit> habits = new ArrayList<>();

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
        Habit habit5 = new Habit(4);
        create_and_attach_tasks(habit5, "Read before bed", "Try to do 30 pages a day!", Freq.Daily, new Date(1, 12, 2025), false, 3);
        create_and_attach_tasks(habit5, "Read before bed", "Try to do 30 pages a day!", Freq.Daily, new Date(1, 12, 2025), true, 7);
        create_and_attach_tasks(habit5, "Read before bed", "Try to do 30 pages a day!", Freq.Daily, new Date(1, 12, 2025), false, 4);

        habits.add(habit1);
        habits.add(habit2);
        habits.add(habit3);
        habits.add(habit4);
        habits.add(habit5);
        return habits;
    }

    public static void create_and_attach_tasks(Habit habit, String name, String desc, Freq freq, Date date, Boolean completed, int total){
        for (int i = 0; i < total; i++) {
            Task task = new  Task(name, desc, freq, date, i, completed);
            habit.add_task(task);
        }
    }
}