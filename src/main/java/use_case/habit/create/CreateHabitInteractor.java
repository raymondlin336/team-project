package use_case.habit.create;

import main.Habit;
import use_case.habit.HabitDataAccessException;
import use_case.habit.HabitDataAccessInterface;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Interactor responsible for the create habit use case.
 */
public class CreateHabitInteractor implements CreateHabitInputBoundary {
    private final HabitDataAccessInterface habitDataAccessObject;
    private final CreateHabitOutputBoundary presenter;

    public CreateHabitInteractor(HabitDataAccessInterface habitDataAccessObject,
                                 CreateHabitOutputBoundary presenter) {
        this.habitDataAccessObject = Objects.requireNonNull(habitDataAccessObject, "habitDataAccessObject");
        this.presenter = Objects.requireNonNull(presenter, "presenter");
    }

    @Override
    public void execute(CreateHabitInputData inputData) {
        List<String> validationErrors = validate(inputData);
        if (!validationErrors.isEmpty()) {
            presenter.prepareFailView(String.join("; ", validationErrors));
            return;
        }

        Habit habit = new Habit(
                -1,
                inputData.getName().trim(),
                inputData.getDescription().trim(),
                inputData.getFrequency(),
                inputData.getFirstDeadline(),
                new ArrayList<>(inputData.getCompletionHistory()));

        try {
            Habit persisted = habitDataAccessObject.saveHabit(habit);
            CreateHabitOutputData outputData = new CreateHabitOutputData(persisted, Instant.now());
            presenter.prepareSuccessView(outputData);
        } catch (HabitDataAccessException repositoryException) {
            presenter.prepareFailView(repositoryException.getMessage());
        }
    }

    private List<String> validate(CreateHabitInputData inputData) {
        List<String> errors = new ArrayList<>();
        if (inputData == null) {
            errors.add("Request cannot be null");
            return errors;
        }
        if (inputData.getName() == null || inputData.getName().trim().isEmpty()) {
            errors.add("Habit name cannot be blank");
        }
        if (inputData.getDescription() == null || inputData.getDescription().trim().isEmpty()) {
            errors.add("Habit description cannot be blank");
        }
        if (inputData.getFrequency() == null) {
            errors.add("Habit frequency is required");
        }
        if (inputData.getFirstDeadline() == null) {
            errors.add("First deadline must be provided");
        }
        return errors;
    }
}
