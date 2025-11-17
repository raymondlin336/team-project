package use_case.habit.create;

import main.Habit;

import java.util.ArrayList;
import java.util.List;

/**
 * Input data provided by the controller to create a habit.
 */
public class CreateHabitInputData {
    private final String username;
    private final String name;
    private final String description;
    private final Habit.Freq frequency;
    private final Habit.Date firstDeadline;
    private final List<Integer> completionHistory;

    public CreateHabitInputData(String username,
                                String name,
                                String description,
                                Habit.Freq frequency,
                                Habit.Date firstDeadline,
                                List<Integer> completionHistory) {
        this.username = username;
        this.name = name;
        this.description = description;
        this.frequency = frequency;
        this.firstDeadline = firstDeadline;
        this.completionHistory = new ArrayList<>(completionHistory == null ? List.of() : completionHistory);
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Habit.Freq getFrequency() {
        return frequency;
    }

    public Habit.Date getFirstDeadline() {
        return firstDeadline;
    }

    public List<Integer> getCompletionHistory() {
        return completionHistory;
    }
}
