package gui;

import data_access.habits.HabitDataAccess;
import gui.home.HomePresenter;
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
import use_case.habit.complete.CompleteHabitTaskInteractor;
import use_case.habit.create.CreateHabitInteractor;
import use_case.habit.overview.get.GetHabitsInteractor;
import use_case.statistics.get.GetStatisticsInteractor;

public class UIBuilderTests {

    @Test
    public void UIBuilderTest() {
        System.out.println("UIBuilderTest");

        HabitDataAccess dataAccess = new HabitDataAccess("src/main/java/placeholders/placeholder_json.json", 0);

        NewTaskViewModel newTaskViewModel = new NewTaskViewModel();
        NewTaskPresenter newTaskPresenter = new NewTaskPresenter(newTaskViewModel);
        CreateHabitInteractor createHabitInteractor = new CreateHabitInteractor(dataAccess, newTaskPresenter);
        NewTaskController newTaskController = new NewTaskController(false, createHabitInteractor);
        NewTaskView newTaskView = new NewTaskView(newTaskController, newTaskViewModel);

        StatisticsViewModel statisticsViewModel = new StatisticsViewModel();
        StatisticsPresenter statisticsPresenter = new StatisticsPresenter(statisticsViewModel);
        GetStatisticsInteractor getStatisticsInteractor = new GetStatisticsInteractor(dataAccess, statisticsPresenter);
        StatisticsController statisticsController = new StatisticsController(getStatisticsInteractor);
        StatisticsView satisticsView = new StatisticsView("Statistics", statisticsViewModel, statisticsController);

        HomeViewModel test = new HomeViewModel();
        HomePresenter homePresenter = new HomePresenter(test);
        GetHabitsInteractor homeInteractor = new GetHabitsInteractor(dataAccess, homePresenter);
        CompleteHabitTaskInteractor completeHabitInteractor = new CompleteHabitTaskInteractor(dataAccess, homePresenter);
        HomeViewController homeViewController = new HomeViewController(false, homeInteractor, completeHabitInteractor);
        HomeView homeView = new HomeView(test, homeViewController);

        ScreenManager manager = new ScreenManager(dataAccess, newTaskView, homeView, homeViewController, satisticsView, statisticsController);
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
}