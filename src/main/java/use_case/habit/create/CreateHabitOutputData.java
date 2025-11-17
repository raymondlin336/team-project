package use_case.habit.create;

import main.Habit;

import java.time.Instant;

/**
 * Output data returned to the presenter after creating a habit.
 */
public class CreateHabitOutputData {
    private final Habit habit;
    private final Instant createdAt;

    public CreateHabitOutputData(Habit habit, Instant createdAt) {
        this.habit = habit;
        this.createdAt = createdAt;
    }

    public Habit getHabit() {
        return habit;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
