package use_case.habit.create;

import entity.Date;
import entity.Habit;
import use_case.habit.HabitDataAccessInterface;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Interactor handling the create habit use case.
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
        List<String> errors = validate(inputData);
        if (!errors.isEmpty()) {
            presenter.prepareFailView(String.join("; ", errors));
            return;
        }

        // Get today's date for the first task's due date
        LocalDate now = LocalDate.now();
        Date today = new Date(now.getDayOfMonth(), now.getMonthValue(), now.getYear());

        int newId = habitDataAccessObject.getNextId();
        Habit habit = new Habit(
                inputData.getName().trim(),
                inputData.getDescription(),
                inputData.getFrequency(),
                today,
                newId);

        Habit persisted = habitDataAccessObject.save(habit);
        presenter.prepareSuccessView(new CreateHabitOutputData(persisted));
    }

    private List<String> validate(CreateHabitInputData inputData) {
        List<String> errors = new ArrayList<>();
        if (inputData == null) {
            errors.add("Request cannot be null.");
            return errors;
        }
        if (inputData.getName() == null || inputData.getName().trim().isEmpty()) {
            errors.add("Habit name cannot be blank.");
        }
        if (inputData.getFrequency() == null) {
            errors.add("Frequency is required.");
        }
        return errors;
    }
}
