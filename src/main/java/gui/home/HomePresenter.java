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
            Date weekStart = new Date(startOfWeek.getDayOfMonth(), startOfWeek.getMonthValue() - 1, startOfWeek.getYear());
            Date weekEnd = new Date(endOfWeek.getDayOfMonth(), endOfWeek.getMonthValue() - 1, endOfWeek.getYear());


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

        // 2. Aggregate all habits
        List<Habit> allHabits = new ArrayList<>();
        if (outputData.getDailyHabits() != null) allHabits.addAll(outputData.getDailyHabits());
        if (outputData.getWeeklyHabits() != null) allHabits.addAll(outputData.getWeeklyHabits());
        if (outputData.getMonthlyHabits() != null) allHabits.addAll(outputData.getMonthlyHabits());

        // 3. Date Setup
        LocalDate now = LocalDate.now();
        // FIX 1: Subtract 1 from month
        Date today = new Date(now.getDayOfMonth(), now.getMonthValue() - 1, now.getYear());

        LocalDate startOfWeek = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate endOfWeek = startOfWeek.plusDays(6);
        Date weekStart = new Date(startOfWeek.getDayOfMonth(), startOfWeek.getMonthValue() - 1, startOfWeek.getYear());
        Date weekEnd = new Date(endOfWeek.getDayOfMonth(), endOfWeek.getMonthValue() - 1, endOfWeek.getYear());

        // 4. Run Filter Logic
        for (Habit habit : allHabits) {
            categorizeHabit(habit, newDaily, newWeekly, newMonthly, today, weekStart, weekEnd);
        }

        // 5. Update ViewModel
        viewModel.updateHabits(newDaily, newWeekly, newMonthly);
    }

    @Override
    public void prepareFailView(String errorMessage) {
        System.out.println("Error completing task: " + errorMessage);
    }

    @Override
    public void prepareSuccessView(CompleteHabitTaskOutputData outputData) {
        // FIX 2: Do NOT just use updateSingleHabit.
        // We must re-evaluate which list the habit belongs to.
        // If a Daily task is completed, its next deadline is tomorrow, so it should be REMOVED from the Daily list.

        Habit updatedHabit = outputData.getHabit();

        // 1. Get current lists (make copies to modify)
        ArrayList<Habit> newDaily = new ArrayList<>(viewModel.dailyHabits);
        ArrayList<Habit> newWeekly = new ArrayList<>(viewModel.weeklyHabits);
        ArrayList<Habit> newMonthly = new ArrayList<>(viewModel.monthlyHabits);

        // 2. Remove the OLD version of this habit from all lists (based on ID)
        newDaily.removeIf(h -> h.id == updatedHabit.id);
        newWeekly.removeIf(h -> h.id == updatedHabit.id);
        newMonthly.removeIf(h -> h.id == updatedHabit.id);

        // 3. Re-calculate dates
        LocalDate now = LocalDate.now();
        Date today = new Date(now.getDayOfMonth(), now.getMonthValue() - 1, now.getYear());

        LocalDate startOfWeek = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate endOfWeek = startOfWeek.plusDays(6);
        Date weekStart = new Date(startOfWeek.getDayOfMonth(), startOfWeek.getMonthValue() - 1, startOfWeek.getYear());
        Date weekEnd = new Date(endOfWeek.getDayOfMonth(), endOfWeek.getMonthValue() - 1, endOfWeek.getYear());

        // 4. Check where the UPDATED habit belongs now
        categorizeHabit(updatedHabit, newDaily, newWeekly, newMonthly, today, weekStart, weekEnd);

        // 5. Push full update to ViewModel (this will refresh the UI)
        viewModel.updateHabits(newDaily, newWeekly, newMonthly);
    }

    /**
     * Helper to avoid duplicate logic.
     * Decides which list(s) the habit belongs to based on its NEXT task deadline.
     */
    private void categorizeHabit(Habit habit,
                                 ArrayList<Habit> dailyList,
                                 ArrayList<Habit> weeklyList,
                                 ArrayList<Habit> monthlyList,
                                 Date today, Date weekStart, Date weekEnd) {

        var next = habit.get_task_by_date(today);

        if (next == null || next.deadline == null) return;

        if ((next.freq == Freq.Daily || next.freq == Freq.Once) && today.day == next.deadline.day) {
            dailyList.add(habit);
        }
        else if (next.freq == Freq.Weekly && (weekStart.day <= next.deadline.day && weekEnd.day >= next.deadline.day)) {
            weeklyList.add(habit);
        }
        else if (next.freq == Freq.Monthly && today.month == next.deadline.month) {
            monthlyList.add(habit);
        }
        System.out.println("Today's date is: " + today);
        System.out.println(today.day);
        System.out.println(next.deadline.day);
        System.out.println(dailyList);
    }
}


