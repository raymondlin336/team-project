package use_case.habit.edit;

/**
 * Input boundary for the edit habit use case.
 */
public interface EditHabitInputBoundary {

    /**
     * Executes the edit habit use case with the provided input data.
     *
     * @param inputData the data required to edit an existing habit
     */
    void execute(EditHabitInputData inputData);
}