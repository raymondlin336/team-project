package use_case.habit.edit;


/**
 * Output boundary for the edit habit use case.
 * <p>
 * Implementations of this interface are responsible for
 * preparing view models or UI updates based on the result
 * of editing a habit.
 */
public interface EditHabitOutputBoundary {


    /**
     * Prepares the success view when a habit has been edited successfully.
     *
     * @param outputData the data representing the updated habit and metadata
     */
    void prepareSuccessView(EditHabitOutputData outputData);


    /**
     * Prepares the failure view when the edit habit use case cannot complete.
     *
     * @param errorMessage a human-readable description of the error
     */
    void prepareFailView(EditHabitOutputData outputData, String errorMessage);
}
