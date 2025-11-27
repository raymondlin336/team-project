package use_case.habit.overview.get;

import entity.Freq;
import entity.Habit;
import use_case.habit.HabitDataAccessInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * interactor for getting habits for the HomeView screen.
 * responsbilities:
 *  - load all habits from  data access layer.
 *  - group them into daily, monthly and weekly... based on the frequency
 *    of the ***NEXT*** task in each habit. since we can like edit it in the middle and change freq.
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
        // get all habits from data store
        List<Habit> loadedHabits = habitDataAccess.findAll();
        ArrayList<Habit> allHabits = new ArrayList<>(loadedHabits);

        if (allHabits.isEmpty()) {
            presenter.prepareFailView("No habits found.");
            return;
        }

        // group by frequency by the NEXT task
        ArrayList<Habit> dailyHabits = new ArrayList<>();
        ArrayList<Habit> weeklyHabits = new ArrayList<>();
        ArrayList<Habit> monthlyHabits = new ArrayList<>();

        for (Habit habit : allHabits) {
            // We assume each habit has at least one task and we use the NEXT task’s freq.
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
                default:
                    break;
            }
        }

        // build output and send to presenter
        GetHabitsOutputData outputData =
                new GetHabitsOutputData(dailyHabits, weeklyHabits, monthlyHabits, allHabits);

        presenter.prepareSuccessView(outputData);
    }
}
