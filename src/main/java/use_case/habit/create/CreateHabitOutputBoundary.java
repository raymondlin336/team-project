package use_case.habit.create;

/**
 * Presenter boundary for the create habit use case.
 */
public interface CreateHabitOutputBoundary {
    void prepareSuccessView(CreateHabitOutputData outputData);

    void prepareFailView(String errorMessage);
}
