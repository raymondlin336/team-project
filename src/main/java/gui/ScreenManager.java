package gui;

import data_access.habits.HabitDataAccess;
import gui.edit_task.EditTaskController;
import gui.edit_task.EditTaskPresenter;
import gui.edit_task.EditTaskView;
import gui.edit_task.EditTaskViewModel;
import gui.home.HomeView;
import gui.home.HomeViewController;
import gui.new_task.NewTaskController;
import gui.new_task.NewTaskView;
import gui.statistics.StatisticsController;
import gui.statistics.StatisticsView;
import use_case.habit.delete.DeleteHabitInteractor;
import use_case.habit.edit.EditHabitInteractor;

import javax.swing.*;

public class ScreenManager {
    private JFrame mainFrame;
    private HabitDataAccess habitDataAccess;
    private NewTaskView newTaskView;
    private HomeView homeView;
    private HomeViewController homeViewController;
    private StatisticsView statisticsView;
    private StatisticsController statisticsController;

    public ScreenManager(HabitDataAccess habitDataAccess,
                         NewTaskView newTaskView,
                         HomeView homeView,
                         HomeViewController homeViewController,
                         StatisticsView statisticsView,
                         StatisticsController statisticsController) {
        this.mainFrame = new JFrame();
        mainFrame.setVisible(true);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(800, 600);
        mainFrame.setLocationRelativeTo(null);
        this.habitDataAccess = habitDataAccess;
        this.newTaskView = newTaskView;
        this.homeView = homeView;
        this.homeViewController = homeViewController;
        this.statisticsView = statisticsView;
        this.statisticsController = statisticsController;
    }

    public void showEditTaskView(int habitId) {
        ///  Create new edit view based on passed habit
        EditTaskViewModel editTaskViewModel = new EditTaskViewModel();
        EditTaskPresenter editTaskPresenter = new EditTaskPresenter(editTaskViewModel);
        EditHabitInteractor editHabitInteractor = new EditHabitInteractor(habitDataAccess, editTaskPresenter, editTaskPresenter);
        DeleteHabitInteractor deleteHabitInteractor = new DeleteHabitInteractor(habitDataAccess, editTaskPresenter);
        EditTaskController editTaskController = new EditTaskController(true, editHabitInteractor, deleteHabitInteractor);
        EditTaskView newEditTaskView = new EditTaskView(editTaskViewModel, editTaskController);

        editTaskController.addScreenManager(statisticsController.getScreenManager());
        editTaskController.firstShowEditTask(habitId);
        mainFrame.setContentPane(newEditTaskView.getPanel());
        mainFrame.setVisible(true);
        mainFrame.revalidate();
        mainFrame.repaint();

    }

    public void showAddTaskView(){
        mainFrame.setContentPane(newTaskView.getPanel());
        mainFrame.setVisible(true);
        mainFrame.revalidate();
        mainFrame.repaint();
    }

    public void showHomeView(){
        homeViewController.updateHome();
        mainFrame.setContentPane(homeView.getPanel());
        mainFrame.setVisible(true);
        mainFrame.revalidate();
        mainFrame.repaint();
    };

    public void refreshEditView(JPanel panel){
        mainFrame.setContentPane(panel);
        mainFrame.setVisible(true);
        mainFrame.revalidate();
        mainFrame.repaint();
    }

    public void showStatisticsView(){
        statisticsController.updateStatistics();
        mainFrame.setContentPane(statisticsView.getPanel());
        mainFrame.setVisible(true);
        mainFrame.revalidate();
        mainFrame.repaint();
    }

}
