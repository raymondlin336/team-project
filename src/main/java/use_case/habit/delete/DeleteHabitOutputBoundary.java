package use_case.habit.delete;

/**
 * Presenter boundary for the delete habit use case.
 */
public interface DeleteHabitOutputBoundary {
    void prepareSuccessView(DeleteHabitOutputData outputData);

    void prepareFailView(String errorMessage);
}
