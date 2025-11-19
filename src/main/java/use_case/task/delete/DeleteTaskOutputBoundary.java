package use_case.task.delete;

/**
 * Presenter boundary for delete task.
 */
public interface DeleteTaskOutputBoundary {
    void prepareSuccessView(DeleteTaskOutputData outputData);

    void prepareFailView(String errorMessage);
}
