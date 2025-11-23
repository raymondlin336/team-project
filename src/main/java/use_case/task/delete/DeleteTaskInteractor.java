package use_case.task.delete;

import use_case.task.TaskDataAccessInterface;

import java.util.Objects;

/**
 * Interactor for deleting a task.
 */
public class DeleteTaskInteractor implements DeleteTaskInputBoundary {

    private final TaskDataAccessInterface taskDataAccessObject;
    private final DeleteTaskOutputBoundary presenter;

    public DeleteTaskInteractor(TaskDataAccessInterface taskDataAccessObject,
                                DeleteTaskOutputBoundary presenter) {
        this.taskDataAccessObject = Objects.requireNonNull(taskDataAccessObject, "taskDataAccessObject");
        this.presenter = Objects.requireNonNull(presenter, "presenter");
    }

    @Override
    public void execute(DeleteTaskInputData inputData) {
        if (inputData == null || inputData.getTaskId() <= 0) {
            presenter.prepareFailView("A valid task id must be provided.");
            return;
        }

        if (taskDataAccessObject.findById(inputData.getTaskId()).isEmpty()) {
            presenter.prepareFailView("Task " + inputData.getTaskId() + " does not exist.");
            return;
        }

        taskDataAccessObject.deleteById(inputData.getTaskId());
        presenter.prepareSuccessView(new DeleteTaskOutputData(inputData.getTaskId()));
    }
}
