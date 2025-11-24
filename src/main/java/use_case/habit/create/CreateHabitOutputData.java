package use_case.habit.create;

import entity.Habit;

/**
 * Output data returned when a habit is created.
 */
public class CreateHabitOutputData {
    private final Habit habit;

    public CreateHabitOutputData(Habit habit) {
        this.habit = habit;
    }

    public Habit getHabit() {
        return habit;
    }
}
