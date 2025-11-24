package use_case.task.create;

import entity.Task;

/**
 * Input data required to add a task.
 */
public class CreateTaskInputData {
    private final String name;
    private final int repeatCount;
    private final Task.Frequency frequency;

    public CreateTaskInputData(String name, int repeatCount, Task.Frequency frequency) {
        this.name = name;
        this.repeatCount = repeatCount;
        this.frequency = frequency;
    }

    public String getName() {
        return name;
    }

    public int getRepeatCount() {
        return repeatCount;
    }

    public Task.Frequency getFrequency() {
        return frequency;
    }
}
