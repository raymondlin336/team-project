package use_case.habit.delete;

/**
 * Input boundary for deleting habits.
 */
public interface DeleteHabitInputBoundary {
    void execute(DeleteHabitInputData inputData);
}
