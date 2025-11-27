package use_case.habit.delete;

import use_case.habit.HabitDataAccessInterface;

import java.util.Objects;

/**
 * Interactor for deleting a habit.
 */
public class DeleteHabitInteractor implements DeleteHabitInputBoundary {

    private final HabitDataAccessInterface habitDataAccessObject;
    private final DeleteHabitOutputBoundary presenter;

    public DeleteHabitInteractor(HabitDataAccessInterface habitDataAccessObject,
                                 DeleteHabitOutputBoundary presenter) {
        this.habitDataAccessObject = Objects.requireNonNull(habitDataAccessObject, "habitDataAccessObject");
        this.presenter = Objects.requireNonNull(presenter, "presenter");
    }

    @Override
    public void execute(DeleteHabitInputData inputData) {
        if (inputData == null || inputData.getHabitId() <= 0) {
            presenter.prepareFailView("A valid habit id must be provided.");
            return;
        }

        if (habitDataAccessObject.findById(inputData.getHabitId()).isEmpty()) {
            presenter.prepareFailView("Habit " + inputData.getHabitId() + " does not exist.");
            return;
        }

        habitDataAccessObject.deleteById(inputData.getHabitId());
        presenter.prepareSuccessView(new DeleteHabitOutputData(inputData.getHabitId()));
    }
}
