package gui.statistics;

import entity.Habit;
import use_case.statistics.get.GetStatisticsOutputBoundary;
import use_case.statistics.get.GetStatisticsOutputData;

import java.util.ArrayList;

public class StatisticsPresenter implements GetStatisticsOutputBoundary {
    private StatisticsViewModel statisticsViewModel;
    public StatisticsPresenter(StatisticsViewModel statisticsViewModel) {
        this.statisticsViewModel = statisticsViewModel;
    }


    @Override
    public void prepareSuccessView(GetStatisticsOutputData outputData) {
        statisticsViewModel.updateHabits(new ArrayList<Habit>(outputData.getRecurringHabits()));
    }

    @Override
    public void prepareFailView(String errorMessage) {
        // TODO: prepare a failed view
    }
}
