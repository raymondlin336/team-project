package use_case.task.delete;

/**
 * Input boundary for deleting tasks.
 */
public interface DeleteTaskInputBoundary {
    void execute(DeleteTaskInputData inputData);
}
