package gui.home;

import entity.Freq;
import entity.Habit;
import use_case.habit.complete.CompleteHabitTaskOutputBoundary;
import use_case.habit.complete.CompleteHabitTaskOutputData;
import use_case.habit.overview.get.GetHabitsOutputBoundary;
import use_case.habit.overview.get.GetHabitsOutputData;

import java.util.ArrayList;
import java.util.List;

public class HomePresenter implements GetHabitsOutputBoundary, CompleteHabitTaskOutputBoundary {
    private final HomeViewModel viewModel;

    public HomePresenter(HomeViewModel viewModel) {
        this.viewModel = viewModel;
    }

    public void presentHabits(List<Habit> habits) {
        viewModel.dailyHabits.clear();
        viewModel.weeklyHabits.clear();
        viewModel.monthlyHabits.clear();

        for (Habit habit : habits) {
            var next = habit.get_next();
            if (next.freq == Freq.Daily) {
                viewModel.dailyHabits.add(habit);
            } else if (next.freq == Freq.Weekly) {
                viewModel.weeklyHabits.add(habit);
            } else if (next.freq == Freq.Monthly) {
                viewModel.monthlyHabits.add(habit);
            }
        }
    }

    @Override
    public void prepareSuccessView(GetHabitsOutputData outputData) {
        viewModel.updateHabits(
                new ArrayList<Habit>(outputData.getDailyHabits()),
                new ArrayList<Habit>(outputData.getWeeklyHabits()),
                new ArrayList<Habit>(outputData.getMonthlyHabits())
        );
    }

    @Override
    public void prepareFailView(String errorMessage) {
        System.out.println("Error completing task: " + errorMessage);
    }

    @Override
    public void prepareSuccessView(CompleteHabitTaskOutputData outputData) {
        // Update the specific habit in the ViewModel
        viewModel.updateSingleHabit(outputData.getHabit());
    }
}


