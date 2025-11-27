package use_case.task.create;

import entity.Task;

/**
 * Output data returned when a task is created.
 */
public class CreateTaskOutputData {
    private final Task task;

    public CreateTaskOutputData(Task task) {
        this.task = task;
    }

    public Task getTask() {
        return task;
    }
}
