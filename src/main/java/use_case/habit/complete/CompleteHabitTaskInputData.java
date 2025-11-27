package use_case.habit.complete;

/**
 * Input data required to complete the current task of a habit.
 */
public class CompleteHabitTaskInputData {

    private final int habitId;

    /**
     * @param habitId id of the habit whose current task should be completed
     */
    public CompleteHabitTaskInputData(int habitId) {
        this.habitId = habitId;
    }

    public int getHabitId() {
        return habitId;
    }
}
