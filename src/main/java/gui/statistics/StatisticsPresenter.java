package gui.statistics;

import use_case.habit.overview.get.GetHabitsOutputBoundary;
import use_case.habit.overview.get.GetHabitsOutputData;

public class StatisticsPresenter implements GetHabitsOutputBoundary {
    private StatisticsViewModel statisticsViewModel;
    public StatisticsPresenter(StatisticsViewModel statisticsViewModel) {
        this.statisticsViewModel = statisticsViewModel;
    }

    @Override
    public void prepareSuccessView(GetHabitsOutputData outputData) {
        statisticsViewModel.updateHabits(outputData.getAllHabits());
    }

    @Override
    public void prepareFailView(String errorMessage) {
        // TODO: prepare a failed view
    }
}
