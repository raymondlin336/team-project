package gui.home;

import entity.Freq;
import entity.Habit;

import java.util.List;

public class HomePresenter {
    private final HomeViewModel viewModel;
    private HomeViewInterface view;  // set later

    public HomePresenter(HomeViewModel viewModel) {
        this.viewModel = viewModel;
    }

    public void attachView(HomeViewInterface view) {
        this.view = view;
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

        if (view != null) {
            // e.g. default to daily
            view.showTasks(viewModel.dailyHabits);
        }
    }

    public void onDailyTabSelected() {
        if (view != null) {
            view.showTasks(viewModel.dailyHabits);
        }
    }

    public void onWeeklyTabSelected() {
        if (view != null) {
            view.showTasks(viewModel.weeklyHabits);
        }
    }

    public void onMonthlyTabSelected() {
        if (view != null) {
            view.showTasks(viewModel.monthlyHabits);
        }
    }
}


