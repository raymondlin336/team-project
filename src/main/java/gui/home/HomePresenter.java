package gui.home;

import entity.Date;
import entity.Freq;
import entity.Habit;
import use_case.habit.complete.CompleteHabitTaskOutputBoundary;
import use_case.habit.complete.CompleteHabitTaskOutputData;
import use_case.habit.overview.get.GetHabitsOutputBoundary;
import use_case.habit.overview.get.GetHabitsOutputData;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
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
            LocalDate now = LocalDate.now();
            Date today = new Date(now.getDayOfMonth(), now.getMonthValue(), now.getYear());

            ///  for weekly
            LocalDate startOfWeek = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
            LocalDate endOfWeek = startOfWeek.plusDays(6);
            Date weekStart = new Date(startOfWeek.getDayOfMonth(), startOfWeek.getMonthValue(), startOfWeek.getYear());
            Date weekEnd = new Date(endOfWeek.getDayOfMonth(), endOfWeek.getMonthValue(), endOfWeek.getYear());


            var next = habit.get_next();
            if ((next.freq == Freq.Daily || next.freq == Freq.Once) && today.day == habit.get_next().deadline.day) {
                viewModel.dailyHabits.add(habit);
            } else if (next.freq == Freq.Weekly && (weekStart.day <= habit.get_next().deadline.day && weekEnd.day > habit.get_next().deadline.day)) {
                viewModel.weeklyHabits.add(habit);
            } else if (next.freq == Freq.Monthly && today.month == habit.get_next().deadline.month) {
                viewModel.monthlyHabits.add(habit);
            }
        }
    }

    @Override
    public void prepareSuccessView(GetHabitsOutputData outputData) {
        // 1. Create new temporary lists
        ArrayList<Habit> newDaily = new ArrayList<>();
        ArrayList<Habit> newWeekly = new ArrayList<>();
        ArrayList<Habit> newMonthly = new ArrayList<>();

        // 2. Aggregate all habits from outputData so we can run YOUR filter logic on them.
        // (Assuming outputData might return them unsorted or we want to re-sort them)
        List<Habit> allHabits = new ArrayList<>();
        if (outputData.getDailyHabits() != null) allHabits.addAll(outputData.getDailyHabits());
        if (outputData.getWeeklyHabits() != null) allHabits.addAll(outputData.getWeeklyHabits());
        if (outputData.getMonthlyHabits() != null) allHabits.addAll(outputData.getMonthlyHabits());

        // 3. Date Setup
        LocalDate now = LocalDate.now();
        Date today = new Date(now.getDayOfMonth(), now.getMonthValue(), now.getYear());

        LocalDate startOfWeek = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate endOfWeek = startOfWeek.plusDays(6);
        Date weekStart = new Date(startOfWeek.getDayOfMonth(), startOfWeek.getMonthValue(), startOfWeek.getYear());
        Date weekEnd = new Date(endOfWeek.getDayOfMonth(), endOfWeek.getMonthValue(), endOfWeek.getYear());

        // 4. Run your Filter Logic
        for (Habit habit : allHabits) {
            var next = habit.get_next();

            // Safety check for nulls if necessary
            if (next == null || next.deadline == null) continue;

            if ((next.freq == Freq.Daily || next.freq == Freq.Once) && today.day == next.deadline.day) {
                newDaily.add(habit);
            }
            else if (next.freq == Freq.Weekly && (weekStart.day <= next.deadline.day && weekEnd.day >= next.deadline.day)) {
                // Note: I changed > to >= for weekEnd to be inclusive
                newWeekly.add(habit);
            }
            else if (next.freq == Freq.Monthly && today.month == next.deadline.month) {
                newMonthly.add(habit);
            }
        }

        // 5. CRITICAL: Update ViewModel via the method that fires PropertyChange
        viewModel.updateHabits(newDaily, newWeekly, newMonthly);
    }

    @Override
    public void prepareFailView(String errorMessage) {
        System.out.println("Error completing task: " + errorMessage);
    }

    @Override
    public void prepareSuccessView(CompleteHabitTaskOutputData outputData) {
        viewModel.updateSingleHabit(outputData.getHabit());
    }
}


