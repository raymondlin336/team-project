package application.habit.create;

/**
 * Presenter-facing boundary for the create habit use case.
 */
public interface CreateHabitOutputBoundary {
    void presentSuccess(CreateHabitResponseModel responseModel);

    void presentFailure(String errorMessage);
}
