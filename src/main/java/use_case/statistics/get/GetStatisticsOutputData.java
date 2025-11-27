package use_case.statistics.get;

import entity.Habit;

import java.util.List;

/**
 * Output data returned when statistics are retrieved.
 */
public class GetStatisticsOutputData {
    private final List<Habit> recurringHabits;

    public GetStatisticsOutputData(List<Habit> recurringHabits) {
        this.recurringHabits = recurringHabits;
    }

    public List<Habit> getRecurringHabits() {
        return recurringHabits;
    }
}
