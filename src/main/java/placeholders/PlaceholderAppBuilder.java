package placeholders;

import gui.edit_task.EditTaskController;
import gui.edit_task.EditTaskView;
import gui.edit_task.EditTaskViewModel;

public class PlaceholderAppBuilder {
    public static void main(String[] args) {
        EditTaskController editTaskController = new EditTaskController(true);
        EditTaskViewModel editTaskViewModel = new EditTaskViewModel(new PlaceholderTask("Groceries", "Get milk, toast, eggs", "every week", "November 15th, 2025"));
        EditTaskView view = new EditTaskView(editTaskViewModel, editTaskController);
        view.setVisible();
    }
}
