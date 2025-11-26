package use_case.task.create;

import entity.Date;
import entity.Task;
import use_case.task.TaskDataAccessInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Interactor handling the create task use case.
 */
public class CreateTaskInteractor implements CreateTaskInputBoundary {

    private final TaskDataAccessInterface taskDataAccessObject;
    private final CreateTaskOutputBoundary presenter;

    public CreateTaskInteractor(TaskDataAccessInterface taskDataAccessObject,
                                CreateTaskOutputBoundary presenter) {
        this.taskDataAccessObject = Objects.requireNonNull(taskDataAccessObject, "taskDataAccessObject");
        this.presenter = Objects.requireNonNull(presenter, "presenter");
    }

    @Override
    public void execute(CreateTaskInputData inputData) {
        List<String> errors = validate(inputData);
        if (!errors.isEmpty()) {
            presenter.prepareFailView(String.join("; ", errors));
            return;
        }

        // Task task = new Task(
        //         -1,
        //         inputData.getName().trim(),
        //         inputData.getRepeatCount(),
        //         inputData.getFrequency());

        //TODO: Not sure if these parameters are correct especially deadline, description, id and completed
        var task = new Task(inputData.getName().trim(), "taskDescription", inputData.getFrequency(), null, -1, false);

        Task persisted = taskDataAccessObject.save(task);
        presenter.prepareSuccessView(new CreateTaskOutputData(persisted));
    }

    private List<String> validate(CreateTaskInputData inputData) {
        List<String> errors = new ArrayList<>();
        if (inputData == null) {
            errors.add("Request cannot be null.");
            return errors;
        }
        if (inputData.getName() == null || inputData.getName().trim().isEmpty()) {
            errors.add("Task name cannot be blank.");
        }
        if (inputData.getRepeatCount() <= 0) {
            errors.add("Repeat count must be positive.");
        }
        if (inputData.getFrequency() == null) {
            errors.add("Frequency is required.");
        }
        return errors;
    }
}

