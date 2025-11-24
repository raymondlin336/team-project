package use_case.habit.edit;


import main.Habit;


/**
 * Input data provided by the controller to edit an existing habit.
 */
public class EditHabitInputData {


    private final int habitId;
    private final String name;
    private final String description;
    private final Habit.Freq frequency;


    /**
     * Constructs the input data required to edit a habit.
     *
     * @param habitId     the unique identifier of the habit to edit
     * @param name        the new (or existing) name of the habit
     * @param description the new (or existing) description of the habit
     * @param frequency   the new (or existing) frequency of the habit
     */
    public EditHabitInputData(int habitId,
                              String name,
                              String description,
                              Habit.Freq frequency) {
        this.habitId = habitId;
        this.name = name;
        this.description = description;
        this.frequency = frequency;
    }


    public int getHabitId() {
        return habitId;
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
}
