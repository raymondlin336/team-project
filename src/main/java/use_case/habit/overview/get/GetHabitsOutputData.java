package use_case.habit.overview.get;

import entity.Habit;

import java.util.ArrayList;

/**
 * output data for home-overview use case.
 * has habits grouped by frequency for daily/weekly/monthly habits
 */
public class GetHabitsOutputData {

    private final ArrayList<Habit> dailyHabits;
    private final ArrayList<Habit> weeklyHabits;
    private final ArrayList<Habit> monthlyHabits;
    private final ArrayList<Habit> allHabits;

    public GetHabitsOutputData(ArrayList<Habit> dailyHabits,
                               ArrayList<Habit> weeklyHabits,
                               ArrayList<Habit> monthlyHabits,
                               ArrayList<Habit> allHabits) {
        // defensive copies so presenter/GUI can’t mutate our internal collections accidentally
        this.dailyHabits = new ArrayList<>(dailyHabits);
        this.weeklyHabits = new ArrayList<>(weeklyHabits);
        this.monthlyHabits = new ArrayList<>(monthlyHabits);
        this.allHabits = new ArrayList<>(allHabits);
    }

    public ArrayList<Habit> getDailyHabits() {
        return new ArrayList<>(dailyHabits);
    }

    public ArrayList<Habit> getWeeklyHabits() {
        return new ArrayList<>(weeklyHabits);
    }

    public ArrayList<Habit> getMonthlyHabits() {
        return new ArrayList<>(monthlyHabits);
    }

    /**
     * all of them together if in case you need a complete list altogether Raymond.
     */
    public ArrayList<Habit> getAllHabits() {
        return new ArrayList<>(allHabits);
    }
}
