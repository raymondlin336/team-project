package use_case.habit.complete;

/**
 * Presenter boundary for the complete-habit-task use case.
 */
public interface CompleteHabitTaskOutputBoundary {

    /**
     * Called when completing the habit's current task succeeds.
     */
    void prepareSuccessView(CompleteHabitTaskOutputData outputData);

    /**
     * Called when completing the habit's current task fails (validation error, not found, etc.).
     */
    void prepareFailView(String errorMessage);
}
