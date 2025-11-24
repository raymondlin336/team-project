package use_case.habit.delete;

/**
 * Output data returned when a habit is deleted.
 */
public class DeleteHabitOutputData {
    private final int habitId;

    public DeleteHabitOutputData(int habitId) {
        this.habitId = habitId;
    }

    public int getHabitId() {
        return habitId;
    }
}
