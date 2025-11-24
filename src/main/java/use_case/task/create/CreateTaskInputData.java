package use_case.task.create;

import entity.*;
import entity.Freq;

/**
 * Input data required to add a task.
 */
public class CreateTaskInputData {
    private final String name;
    private final int repeatCount;
    private final Freq frequency;

    public CreateTaskInputData(String name, int repeatCount, Freq frequency) {
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

    public Freq getFrequency() {
        return frequency;
    }
}
