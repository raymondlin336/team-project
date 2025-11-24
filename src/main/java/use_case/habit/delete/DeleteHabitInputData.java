package use_case.habit.delete;

/**
 * Input data required to delete a habit.
 */
public class DeleteHabitInputData {
    private final int habitId;

    public DeleteHabitInputData(int habitId) {
        this.habitId = habitId;
    }

    public int getHabitId() {
        return habitId;
    }
}
