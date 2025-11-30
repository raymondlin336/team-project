package use_case.statistics.get;

import entity.Date;
import entity.Freq;
import entity.Habit;
import org.junit.Before;
import org.junit.Test;
import use_case.habit.HabitDataAccessInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.*;

/**
 * Unit tests for GetStatisticsInteractor.
 */
public class GetStatisticsInteractorTests {

    private FakeHabitDataAccess fakeDataAccess;
    private MockGetStatisticsPresenter mockPresenter;
    private GetStatisticsInteractor interactor;

    @Before
    public void setUp() {
        fakeDataAccess = new FakeHabitDataAccess();
        mockPresenter = new MockGetStatisticsPresenter();
        interactor = new GetStatisticsInteractor(fakeDataAccess, mockPresenter);
    }

    @Test
    public void testExecute_withRecurringHabits_filtersOutOnceFrequency() {
        // Create mix of recurring and once habits
        Habit dailyHabit = new Habit("Exercise", "Daily workout", Freq.Daily, new Date(1, 1, 2024), 1);
        Habit weeklyHabit = new Habit("Shopping", "Weekly groceries", Freq.Weekly, new Date(1, 1, 2024), 2);
        Habit monthlyHabit = new Habit("Bills", "Pay monthly bills", Freq.Monthly, new Date(1, 1, 2024), 3);
        Habit onceHabit = new Habit("Project", "One-time task", Freq.Once, new Date(1, 1, 2024), 4);

        fakeDataAccess.addHabit(dailyHabit);
        fakeDataAccess.addHabit(weeklyHabit);
        fakeDataAccess.addHabit(monthlyHabit);
        fakeDataAccess.addHabit(onceHabit);

        GetStatisticsInputData inputData = new GetStatisticsInputData();

        interactor.execute(inputData);

        // Assert
        assertTrue("prepareSuccessView should have been called", mockPresenter.wasSuccessCalled);
        assertFalse("prepareFailView should not have been called", mockPresenter.wasFailCalled);
        assertNotNull("Output data should not be null", mockPresenter.lastSuccessData);

        List<Habit> recurringHabits = mockPresenter.lastSuccessData.getRecurringHabits();
        assertEquals("Should return 3 recurring habits", 3, recurringHabits.size());

        // Verify only recurring habits are included (no Freq.Once)
        for (Habit habit : recurringHabits) {
            assertNotEquals("Habit should not have Freq.Once", Freq.Once, habit.get_next().freq);
        }
    }

    @Test
    public void testExecute_withNoHabits_callsPresenterFail() {
        // No habits in data access
        GetStatisticsInputData inputData = new GetStatisticsInputData();

        interactor.execute(inputData);

        // Assert
        assertFalse("prepareSuccessView should not have been called", mockPresenter.wasSuccessCalled);
        assertTrue("prepareFailView should have been called", mockPresenter.wasFailCalled);
        assertTrue("Error message should mention no habits found",
                mockPresenter.lastFailMessage.contains("No habits found"));
    }

    @Test
    public void testExecute_withOnlyOnceHabits_callsPresenterSuccess() {
        // Only Freq.Once habits
        Habit onceHabit1 = new Habit("Project A", "One-time task", Freq.Once, new Date(1, 1, 2024), 1);
        Habit onceHabit2 = new Habit("Project B", "Another one-time task", Freq.Once, new Date(1, 1, 2024), 2);

        fakeDataAccess.addHabit(onceHabit1);
        fakeDataAccess.addHabit(onceHabit2);

        GetStatisticsInputData inputData = new GetStatisticsInputData();

        interactor.execute(inputData);

        // Assert
        // After filtering, list is empty but prepareSuccessView is still called with empty list
        assertTrue("prepareSuccessView should have been called", mockPresenter.wasSuccessCalled);
        assertFalse("prepareFailView should not have been called", mockPresenter.wasFailCalled);
        assertNotNull("Output data should not be null", mockPresenter.lastSuccessData);

        List<Habit> recurringHabits = mockPresenter.lastSuccessData.getRecurringHabits();
        assertEquals("Should return empty list after filtering", 0, recurringHabits.size());
    }

    @Test
    public void testExecute_withOnlyDailyHabits_returnsAllHabits() {
        // Only Daily habits
        Habit habit1 = new Habit("Exercise", "Morning workout", Freq.Daily, new Date(1, 1, 2024), 1);
        Habit habit2 = new Habit("Reading", "Read 30 minutes", Freq.Daily, new Date(1, 1, 2024), 2);
        Habit habit3 = new Habit("Meditation", "Evening meditation", Freq.Daily, new Date(1, 1, 2024), 3);

        fakeDataAccess.addHabit(habit1);
        fakeDataAccess.addHabit(habit2);
        fakeDataAccess.addHabit(habit3);

        GetStatisticsInputData inputData = new GetStatisticsInputData();

        interactor.execute(inputData);

        // Assert
        assertTrue("prepareSuccessView should have been called", mockPresenter.wasSuccessCalled);
        assertFalse("prepareFailView should not have been called", mockPresenter.wasFailCalled);

        List<Habit> recurringHabits = mockPresenter.lastSuccessData.getRecurringHabits();
        assertEquals("Should return all 3 daily habits", 3, recurringHabits.size());
    }

    @Test
    public void testExecute_withMixedFrequencies_includesOnlyRecurring() {
        // Mix of all frequency types
        Habit dailyHabit = new Habit("Daily Task", "Daily", Freq.Daily, new Date(1, 1, 2024), 1);
        Habit weeklyHabit = new Habit("Weekly Task", "Weekly", Freq.Weekly, new Date(1, 1, 2024), 2);
        Habit monthlyHabit = new Habit("Monthly Task", "Monthly", Freq.Monthly, new Date(1, 1, 2024), 3);
        Habit onceHabit1 = new Habit("Once Task 1", "Once", Freq.Once, new Date(1, 1, 2024), 4);
        Habit onceHabit2 = new Habit("Once Task 2", "Once", Freq.Once, new Date(1, 1, 2024), 5);

        fakeDataAccess.addHabit(dailyHabit);
        fakeDataAccess.addHabit(weeklyHabit);
        fakeDataAccess.addHabit(monthlyHabit);
        fakeDataAccess.addHabit(onceHabit1);
        fakeDataAccess.addHabit(onceHabit2);

        GetStatisticsInputData inputData = new GetStatisticsInputData();

        // Act
        interactor.execute(inputData);

        // Assert
        assertTrue("prepareSuccessView should have been called", mockPresenter.wasSuccessCalled);

        List<Habit> recurringHabits = mockPresenter.lastSuccessData.getRecurringHabits();
        assertEquals("Should return 3 recurring habits (Daily, Weekly, Monthly)", 3, recurringHabits.size());

        // Verify returned habits include Daily, Weekly, and Monthly
        boolean hasDaily = false;
        boolean hasWeekly = false;
        boolean hasMonthly = false;

        for (Habit habit : recurringHabits) {
            Freq freq = habit.get_next().freq;
            if (freq == Freq.Daily) hasDaily = true;
            if (freq == Freq.Weekly) hasWeekly = true;
            if (freq == Freq.Monthly) hasMonthly = true;
        }

        assertTrue("Should include Daily habit", hasDaily);
        assertTrue("Should include Weekly habit", hasWeekly);
        assertTrue("Should include Monthly habit", hasMonthly);
    }

    // ========== Test ==========

    /**
     * Fake implementation of HabitDataAccessInterface for testing.
     */
    private static class FakeHabitDataAccess implements HabitDataAccessInterface {
        private final Map<Integer, Habit> habits = new HashMap<>();
        private int nextId = 1;

        @Override
        public Habit save(Habit habit) {
            habits.put(habit.id, habit);
            return habit;
        }

        @Override
        public Optional<Habit> findById(int id) {
            return Optional.ofNullable(habits.get(id));
        }

        @Override
        public List<Habit> findAll() {
            return new ArrayList<>(habits.values());
        }

        @Override
        public void deleteById(int id) {
            habits.remove(id);
        }

        @Override
        public int getNextId() {
            return nextId++;
        }

        @Override
        public void save_file() {
        }

        // Helper method for tests
        public void addHabit(Habit habit) {
            habits.put(habit.id, habit);
        }
    }

    /**
     * Mock presenter for tracking GetStatistics output boundary calls.
     */
    private static class MockGetStatisticsPresenter implements GetStatisticsOutputBoundary {
        private boolean wasSuccessCalled = false;
        private boolean wasFailCalled = false;
        private GetStatisticsOutputData lastSuccessData = null;
        private String lastFailMessage = null;

        @Override
        public void prepareSuccessView(GetStatisticsOutputData outputData) {
            wasSuccessCalled = true;
            lastSuccessData = outputData;
        }

        @Override
        public void prepareFailView(String errorMessage) {
            wasFailCalled = true;
            lastFailMessage = errorMessage;
        }
    }
}
