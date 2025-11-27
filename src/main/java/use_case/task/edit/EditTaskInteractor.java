package use_case.task.edit;

import entity.Task;
import use_case.task.TaskDataAccessInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Interactor for editing an existing task.
 */
public class EditTaskInteractor implements EditTaskInputBoundary {

    private final TaskDataAccessInterface taskDataAccessObject;
    private final EditTaskOutputBoundary presenter;

    public EditTaskInteractor(TaskDataAccessInterface taskDataAccessObject,
                              EditTaskOutputBoundary presenter) {
        this.taskDataAccessObject = Objects.requireNonNull(taskDataAccessObject, "taskDataAccessObject");
        this.presenter = Objects.requireNonNull(presenter, "presenter");
    }

    @Override
    public void execute(EditTaskInputData inputData) {
        // 1. Validate the request
        List<String> errors = validate(inputData);
        if (!errors.isEmpty()) {
            presenter.prepareFailView(String.join("; ", errors));
            return;
        }

        // 2. Load the existing task
        Optional<Task> optionalTask = taskDataAccessObject.findById(inputData.getTaskId());
        if (optionalTask.isEmpty()) {
            presenter.prepareFailView("Task " + inputData.getTaskId() + " does not exist.");
            return;
        }

        Task task = optionalTask.get();

        // 3. Apply edits
        task.name = inputData.getName().trim();
        task.desc = inputData.getDescription().trim();
        task.freq = inputData.getFrequency();
        // Note: deadline and completed flag are left unchanged here.

        // 4. Persist and notify presenter
        Task saved = taskDataAccessObject.save(task);
        presenter.prepareSuccessView(new EditTaskOutputData(saved));
    }

    private List<String> validate(EditTaskInputData inputData) {
        List<String> errors = new ArrayList<>();

        if (inputData == null) {
            errors.add("Request cannot be null.");
            return errors;
        }

        if (inputData.getTaskId() <= 0) {
            errors.add("A valid task id must be provided.");
        }

        if (inputData.getName() == null || inputData.getName().trim().isEmpty()) {
            errors.add("Task name cannot be blank.");
        }

        if (inputData.getDescription() == null || inputData.getDescription().trim().isEmpty()) {
            errors.add("Task description cannot be blank.");
        }

        if (inputData.getFrequency() == null) {
            errors.add("Task frequency is required.");
        }

        return errors;
    }
}
