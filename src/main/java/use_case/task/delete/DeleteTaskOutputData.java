package use_case.task.delete;

/**
 * Output data returned after deleting a task.
 */
public class DeleteTaskOutputData {
    private final int deletedTaskId;

    public DeleteTaskOutputData(int deletedTaskId) {
        this.deletedTaskId = deletedTaskId;
    }

    public int getDeletedTaskId() {
        return deletedTaskId;
    }
}
