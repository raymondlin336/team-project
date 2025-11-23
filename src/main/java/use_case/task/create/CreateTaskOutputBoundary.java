package use_case.task.create;

/**
 * Presenter boundary for the create task use case.
 */
public interface CreateTaskOutputBoundary {
    void prepareSuccessView(CreateTaskOutputData outputData);

    void prepareFailView(String errorMessage);
}
