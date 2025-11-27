package use_case.habit.get;

import entity.Freq;
import entity.Habit;
import use_case.habit.HabitDataAccessInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Interactor for getting habits for the Home (Habits Overview) screen.
 *
 * Responsibilities:
 *  - Load all habits from the data access layer.
 *  - Group them into Daily / Weekly / Monthly based on the frequency
 *    of the *next* task in each habit.
 *  - Ignore Freq.Once, since the UI only has Daily / Weekly / Monthly tabs.
 */
public class GetHabitsInteractor implements GetHabitsInputBoundary {

    private final HabitDataAccessInterface habitDataAccess;
    private final GetHabitsOutputBoundary presenter;

    public GetHabitsInteractor(HabitDataAccessInterface habitDataAccess,
                               GetHabitsOutputBoundary presenter) {
        this.habitDataAccess = Objects.requireNonNull(habitDataAccess, "habitDataAccess");
        this.presenter = Objects.requireNonNull(presenter, "presenter");
    }

    @Override
    public void execute(GetHabitsInputData inputData) {
        // 1. Fetch all habits from data store
        List<Habit> loadedHabits = habitDataAccess.findAll();
        ArrayList<Habit> allHabits = new ArrayList<>(loadedHabits);

        if (allHabits.isEmpty()) {
            presenter.prepareFailView("No habits found.");
            return;
        }

        // 2. Group by frequency of the *next* task
        ArrayList<Habit> dailyHabits = new ArrayList<>();
        ArrayList<Habit> weeklyHabits = new ArrayList<>();
        ArrayList<Habit> monthlyHabits = new ArrayList<>();

        for (Habit habit : allHabits) {
            // We assume each habit has at least one task and we use the "next" task’s freq.
            Freq freq = habit.get_next().freq;

            switch (freq) {
                case Daily:
                    dailyHabits.add(habit);
                    break;
                case Weekly:
                    weeklyHabits.add(habit);
                    break;
                case Monthly:
                    monthlyHabits.add(habit);
                    break;
                case Once:
                default:
                    // UI has no tab for "Once"; skip it for Home Overview.
                    break;
            }
        }

        // 3. Build output and send to presenter
        GetHabitsOutputData outputData =
                new GetHabitsOutputData(dailyHabits, weeklyHabits, monthlyHabits, allHabits);

        presenter.prepareSuccessView(outputData);
    }
}
