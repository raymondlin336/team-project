package use_case.habit.complete;

import entity.Habit;
import entity.Task;
import use_case.habit.HabitDataAccessInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Interactor for completing the current task of a habit.
 *
 * This use case:
 *  - finds the Habit by id,
 *  - calls habit.complete_most_recent() to mark the current task complete
 *    and create the next task (according to the entity logic),
 *  - saves the updated habit,
 *  - returns the updated habit to the presenter.
 */
public class CompleteHabitTaskInteractor implements CompleteHabitTaskInputBoundary {

    private final HabitDataAccessInterface habitDataAccessObject;
    private final CompleteHabitTaskOutputBoundary presenter;

    public CompleteHabitTaskInteractor(HabitDataAccessInterface habitDataAccessObject,
                                       CompleteHabitTaskOutputBoundary presenter) {
        this.habitDataAccessObject = Objects.requireNonNull(habitDataAccessObject, "habitDataAccessObject");
        this.presenter = Objects.requireNonNull(presenter, "presenter");
    }

    @Override
    public void execute(CompleteHabitTaskInputData inputData) {
        // 1. Validate the request
        List<String> errors = validate(inputData);
        if (!errors.isEmpty()) {
            presenter.prepareFailView(String.join("; ", errors));
            return;
        }

        // 2. Load the existing habit
        Optional<Habit> habitOptional = habitDataAccessObject.findById(inputData.getHabitId());
        if (habitOptional.isEmpty()) {
            presenter.prepareFailView("Habit " + inputData.getHabitId() + " does not exist.");
            return;
        }

        Habit habit = habitOptional.get();

        // 3. If the current task is already completed, you can either:
        //    - treat it as a no-op, or
        //    - still call complete_most_recent() to advance.
        // Here we only advance if the current task is not completed.
        Task current = habit.get_next();
        if (!current.completed) {
            habit.complete_most_recent();
        }

        // 4. Persist and notify presenter
        Habit saved = habitDataAccessObject.save(habit);
        presenter.prepareSuccessView(new CompleteHabitTaskOutputData(saved));
    }

    private List<String> validate(CompleteHabitTaskInputData inputData) {
        List<String> errors = new ArrayList<>();

        if (inputData == null) {
            errors.add("Request cannot be null.");
            return errors;
        }

        if (inputData.getHabitId() < 0) {
            errors.add("A valid habit id must be provided.");
        }

        return errors;
    }
}

// UI only needs to pass a habitId when the checkbox is clicked... However unchecking the box has not been implemented yet.