package use_case.habit.edit;

import entity.Habit;

import java.time.Instant;

/**
 * Output data returned to the presenter after editing a habit.
 */
public class EditHabitOutputData {

    private final Habit habit;
    private final Instant updatedAt;

    /**
     * Constructs the output data for a successfully edited habit.
     *
     * @param habit     the updated habit
     * @param updatedAt the time at which the edit was completed
     */
    public EditHabitOutputData(Habit habit, Instant updatedAt) {
        this.habit = habit;
        this.updatedAt = updatedAt;
    }

    /**
     * @return the updated habit entity
     */
    public Habit getHabit() {
        return habit;
    }

    /**
     * @return the timestamp when the habit was updated
     */
    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
