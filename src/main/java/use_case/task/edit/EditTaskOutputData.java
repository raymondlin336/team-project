package use_case.task.edit;

import entity.Task;

/**
 * Output data returned after successfully editing a task.
 */
public class EditTaskOutputData {

    private final Task task;

    public EditTaskOutputData(Task task) {
        this.task = task;
    }

    /**
     * @return the updated task entity
     */
    public Task getTask() {
        return task;
    }
}
