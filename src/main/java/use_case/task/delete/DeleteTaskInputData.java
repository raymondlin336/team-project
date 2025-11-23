package use_case.task.delete;

/**
 * Input data describing which task to delete.
 */
public class DeleteTaskInputData {
    private final int taskId;

    public DeleteTaskInputData(int taskId) {
        this.taskId = taskId;
    }

    public int getTaskId() {
        return taskId;
    }
}
