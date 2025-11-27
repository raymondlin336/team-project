package use_case.task.edit;

import entity.Freq;

/**
 * Input data required to edit an existing task.
 */
public class EditTaskInputData {

    private final int taskId;
    private final String name;
    private final String description;
    private final Freq frequency;

    /**
     * Constructs the input data required to edit a task.
     *
     * @param taskId      id of the task to edit
     * @param name        new (or existing) name for the task
     * @param description new (or existing) description for the task
     * @param frequency   new (or existing) frequency for the task
     */
    public EditTaskInputData(int taskId,
                             String name,
                             String description,
                             Freq frequency) {
        this.taskId = taskId;
        this.name = name;
        this.description = description;
        this.frequency = frequency;
    }

    public int getTaskId() {
        return taskId;
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
