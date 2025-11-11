package placeholders;

import gui.edit_task.EditTaskController;
import gui.edit_task.EditTaskView;
import gui.edit_task.EditTaskViewModel;
import gui.new_task.NewTaskController;
import gui.new_task.NewTaskView;

public class PlaceholderAppBuilder {
    public static void main(String[] args) {
        // Editing a task
//        EditTaskController editTaskController = new EditTaskController(true);
//        EditTaskViewModel editTaskViewModel = new EditTaskViewModel(new PlaceholderTask("Groceries", "Get milk, toast, eggs", "every week", "November 15th, 2025"));
//        EditTaskView view = new EditTaskView(editTaskViewModel, editTaskController);
//        view.setVisible();
        // Adding a task
        NewTaskController newTaskController = new NewTaskController(true);
        NewTaskView newTaskView = new NewTaskView(newTaskController);
        newTaskView.setVisible();
    }
}
