package use_case.habit.create;

/**
 * Input boundary for creating habits.
 */
public interface CreateHabitInputBoundary {
    void execute(CreateHabitInputData inputData);
}
