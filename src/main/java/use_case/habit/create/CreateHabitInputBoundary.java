package use_case.habit.create;

/**
 * Input boundary for the create habit use case.
 */
public interface CreateHabitInputBoundary {
    void execute(CreateHabitInputData inputData);
}
