package use_case.habit.complete;

/**
 * Input boundary for completing the current task of a habit.
 */
public interface CompleteHabitTaskInputBoundary {

    /**
     * Completes the most recent (current) task for the given habit.
     *
     * @param inputData data describing which habit to complete a task for
     */
    void execute(CompleteHabitTaskInputData inputData);
}
