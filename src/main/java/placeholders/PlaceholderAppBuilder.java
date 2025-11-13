package placeholders;

import gui.edit_task.EditTaskController;
import gui.edit_task.EditTaskView;
import gui.edit_task.EditTaskViewModel;
import gui.new_task.NewTaskController;
import gui.new_task.NewTaskView;
import gui.statistics.StatisticsView;
import gui.statistics.StatisticsViewModel;

import java.util.ArrayList;
import java.util.List;

public class PlaceholderAppBuilder {
    public static void main(String[] args) {
        PlaceholderTask task1 = new PlaceholderTask("Groceries", "Get milk, toast, eggs", "every week", "November 15th, 2025", new ArrayList<>(List.of(1, 0, 0, 1, 0)));
        PlaceholderTask task2 = new PlaceholderTask("Workout", "Run for 30 mins", "every day", "November 11th, 2025", new ArrayList<>(List.of(5, 7, 2, 3, 0, 2, 1, 0, 0, 7, 2, 3, 7, 5, 1, 2)));
        PlaceholderTask task3 = new PlaceholderTask("Basketball with friends", "Sumaid, Henry, and Kevin", "every month", "November 30th, 2025", new ArrayList<>(List.of(0, 1)));
        PlaceholderTask task4 = new PlaceholderTask("Doctor's", "Appointment at 4pm", "once", "November 21th, 2025", new ArrayList<>());
        //Editing a task
//        EditTaskController editTaskController = new EditTaskController(true);
//        EditTaskViewModel editTaskViewModel = new EditTaskViewModel(task1);
//        EditTaskView view = new EditTaskView(editTaskViewModel, editTaskController);
//        view.setVisible();
//        // Adding a task
//        NewTaskController newTaskController = new NewTaskController(true);
//        NewTaskView newTaskView = new NewTaskView(newTaskController);
//        newTaskView.setVisible();
        // Statistics
        ArrayList<PlaceholderTask> tasks = new ArrayList<>(List.of(task1, task2, task3, task4));
        StatisticsViewModel vm = new StatisticsViewModel(tasks);
        StatisticsView stat = new StatisticsView("Statistics", vm);
        stat.setVisible();
    }
}
