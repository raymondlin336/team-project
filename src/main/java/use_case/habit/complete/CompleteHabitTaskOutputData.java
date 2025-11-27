package use_case.habit.complete;

import entity.Habit;

/**
 * Output data returned after successfully completing the current task of a habit.
 */
public class CompleteHabitTaskOutputData {

    private final Habit habit;

    public CompleteHabitTaskOutputData(Habit habit) {
        this.habit = habit;
    }

    /**
     * @return the updated habit, including its updated task list
     */
    public Habit getHabit() {
        return habit;
    }
}
