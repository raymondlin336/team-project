package use_case.task.edit;

/**
 * Presenter boundary for the edit task use case.
 */
public interface EditTaskOutputBoundary {
    void prepareSuccessView(EditTaskOutputData outputData);

    void prepareFailView(String errorMessage);
}
