package use_case.habit.create;

import entity.Freq;

/**
 * Input data required to create a habit.
 */
public class CreateHabitInputData {
    private final String name;
    private final String description;
    private final Freq frequency;

    public CreateHabitInputData(String name, String description, Freq frequency) {
        this.name = name;
        this.description = description;
        this.frequency = frequency;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Freq getFrequency() {
        return frequency;
    }
}
