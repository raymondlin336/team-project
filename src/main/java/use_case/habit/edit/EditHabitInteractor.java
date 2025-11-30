package use_case.habit.edit;

import entity.Habit;
import use_case.habit.HabitDataAccessInterface;
import use_case.habit.delete.DeleteHabitOutputBoundary;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Interactor responsible for the edit habit use case.
 */
public class EditHabitInteractor implements EditHabitInputBoundary {

    private final HabitDataAccessInterface habitDataAccessObject;
    private final EditHabitOutputBoundary editHabitPresenter;
    private final DeleteHabitOutputBoundary deleteHabitPresenter;

    /**
     * Constructs an interactor for editing habits.
     *
     * @param habitDataAccessObject the data access object used to load and save habits
     * @param editHabitPresenter
     * @param deleteHabitPresenter
     */
    public EditHabitInteractor(HabitDataAccessInterface habitDataAccessObject,
                               EditHabitOutputBoundary editHabitPresenter, DeleteHabitOutputBoundary deleteHabitPresenter) {
        this.habitDataAccessObject = Objects.requireNonNull(habitDataAccessObject, "habitDataAccessObject");
        this.editHabitPresenter = Objects.requireNonNull(editHabitPresenter, "presenter");
        this.deleteHabitPresenter = Objects.requireNonNull(deleteHabitPresenter, "presenter");
    }

    @Override
    public void execute(EditHabitInputData inputData) {
        // 2. Load the existing habit
        Optional<Habit> habitOptional = habitDataAccessObject.findById(inputData.getHabitId());
        if (habitOptional.isEmpty()) {
            deleteHabitPresenter.prepareFailView("Habit " + inputData.getHabitId() + " not found");
            return;
        }

        Habit habit = habitDataAccessObject.findById(inputData.getHabitId()).get();

        // 1. Validate basic input
        List<String> validationErrors = validate(inputData);
        if (!validationErrors.isEmpty()) {
            EditHabitOutputData outputData = new EditHabitOutputData(habit, Instant.now());
            editHabitPresenter.prepareFailView(outputData, String.join("; ", validationErrors));
            return;
        }

        // 3. Apply edits to the habit using entity.Habit's methods
        habit.change_name(inputData.getName().trim());
        habit.change_desc(inputData.getDescription().trim());
        habit.change_freq(inputData.getFrequency());

        // 4. Persist the updated habit and prepare output
        habitDataAccessObject.save_file();
        EditHabitOutputData outputData = new EditHabitOutputData(habit, Instant.now());
        editHabitPresenter.prepareSuccessView(outputData);
    }

    public void first_execute(int HabitId){
        Optional<Habit> habitOptional = habitDataAccessObject.findById(HabitId);
        if (habitOptional.isEmpty()) {
            editHabitPresenter.prepareFailView(new EditHabitOutputData(habitOptional.get(), Instant.now()), "Habit " + HabitId + " does not exist.");
        }
        else{
            editHabitPresenter.prepareSuccessView(new EditHabitOutputData(habitOptional.get(), Instant.now()));
        }
    }

    /**
     * Validates the incoming edit request.
     *
     * @param inputData the input data to validate
     * @return a list of validation error messages (empty if valid)
     */
    private List<String> validate(EditHabitInputData inputData) {
        List<String> errors = new ArrayList<>();

        if (inputData == null) {
            errors.add("Request cannot be null.");
            return errors;
        }

        if (inputData.getHabitId() <= 0) {
            errors.add("A valid habit id must be provided.");
        }

        if (inputData.getName() == null || inputData.getName().trim().isEmpty()) {
            errors.add("Habit name cannot be blank.");
        }

        if (inputData.getDescription() == null || inputData.getDescription().trim().isEmpty()) {
            errors.add("Habit description cannot be blank.");
        }

        if (inputData.getFrequency() == null) {
            errors.add("Habit frequency is required.");
        }

        return errors;
    }
}
