package use_case.statistics.get;

import entity.Freq;
import entity.Habit;
import use_case.habit.HabitDataAccessInterface;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Interactor for getting statistics.
 * Fetches all habits and filters to only recurring habits for statistics display.
 */
public class GetStatisticsInteractor implements GetStatisticsInputBoundary {

    private final HabitDataAccessInterface habitDataAccess;
    private final GetStatisticsOutputBoundary presenter;

    public GetStatisticsInteractor(HabitDataAccessInterface habitDataAccess,
                                    GetStatisticsOutputBoundary presenter) {
        this.habitDataAccess = Objects.requireNonNull(habitDataAccess, "habitDataAccess");
        this.presenter = Objects.requireNonNull(presenter, "presenter");
    }

    @Override
    public void execute(GetStatisticsInputData inputData) {
        // Fetch all habits from data store
        List<Habit> allHabits = habitDataAccess.findAll();

        if (allHabits.isEmpty()) {
            presenter.prepareFailView("No habits found.");
            return;
        }

        // Filter to only recurring habits
        // Exclude Freq.Once because they don't have meaningful completion trends for statistics
        List<Habit> recurringHabits = allHabits.stream()
                .filter(habit -> habit.get_next().freq != Freq.Once)
                .collect(Collectors.toList());

        // Return filtered list to presenter
        GetStatisticsOutputData outputData = new GetStatisticsOutputData(recurringHabits);
        presenter.prepareSuccessView(outputData);
    }
}
