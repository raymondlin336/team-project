package application.habit.create;

/**
 * Input boundary for the create habit use case.
 */
public interface CreateHabitInputBoundary {
    void createHabit(CreateHabitRequestModel requestModel);
}
