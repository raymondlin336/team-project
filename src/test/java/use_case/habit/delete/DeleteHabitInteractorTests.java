package use_case.habit.delete;

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
 * Unit tests for DeleteHabitInteractor.
 */
public class DeleteHabitInteractorTests {

    private FakeHabitDataAccess fakeDataAccess;
    private MockDeleteHabitPresenter mockPresenter;
    private DeleteHabitInteractor interactor;

    @Before
    public void setUp() {
        fakeDataAccess = new FakeHabitDataAccess();
        mockPresenter = new MockDeleteHabitPresenter();
        interactor = new DeleteHabitInteractor(fakeDataAccess, mockPresenter);
    }

    @Test
    public void testExecute_withValidId_deletesHabitAndCallsSuccess() {
        Habit habit = new Habit("Exercise", "Daily workout", Freq.Daily, new Date(1, 1, 2024), 1);
        fakeDataAccess.addHabit(habit);

        DeleteHabitInputData inputData = new DeleteHabitInputData(1);

        interactor.execute(inputData);

        // Assert
        assertTrue("prepareSuccessView should have been called", mockPresenter.wasSuccessCalled);
        assertFalse("prepareFailView should not have been called", mockPresenter.wasFailCalled);
        assertNotNull("Output data should not be null", mockPresenter.lastSuccessData);
        assertEquals("Output data should contain correct habit id", 1, mockPresenter.lastSuccessData.getHabitId());

        // Verify habit was deleted
        assertTrue("Habit should be deleted from data access", fakeDataAccess.findById(1).isEmpty());
        assertEquals("No habits should remain", 0, fakeDataAccess.findAll().size());
    }

    @Test
    public void testExecute_withInvalidIdZero_callsPresenterFail() {
        DeleteHabitInputData inputData = new DeleteHabitInputData(0);

        interactor.execute(inputData);

        // Assert
        assertFalse("prepareSuccessView should not have been called", mockPresenter.wasSuccessCalled);
        assertTrue("prepareFailView should have been called", mockPresenter.wasFailCalled);
        assertTrue("Error message should mention valid habit id",
                mockPresenter.lastFailMessage.contains("valid habit id must be provided"));
    }

    @Test
    public void testExecute_withInvalidIdNegative_callsPresenterFail() {
        DeleteHabitInputData inputData = new DeleteHabitInputData(-1);

        interactor.execute(inputData);

        // Assert
        assertFalse("prepareSuccessView should not have been called", mockPresenter.wasSuccessCalled);
        assertTrue("prepareFailView should have been called", mockPresenter.wasFailCalled);
        assertTrue("Error message should mention valid habit id",
                mockPresenter.lastFailMessage.contains("valid habit id must be provided"));
    }

    @Test
    public void testExecute_withNonExistentHabit_callsPresenterFail() {
        DeleteHabitInputData inputData = new DeleteHabitInputData(999);

        interactor.execute(inputData);

        // Assert
        assertFalse("prepareSuccessView should not have been called", mockPresenter.wasSuccessCalled);
        assertTrue("prepareFailView should have been called", mockPresenter.wasFailCalled);
        assertTrue("Error message should mention habit does not exist",
                mockPresenter.lastFailMessage.contains("Habit 999 does not exist"));
    }

    @Test
    public void testExecute_withNullInputData_callsPresenterFail() {
        interactor.execute(null);

        // Assert
        assertFalse("prepareSuccessView should not have been called", mockPresenter.wasSuccessCalled);
        assertTrue("prepareFailView should have been called", mockPresenter.wasFailCalled);
        assertTrue("Error message should mention valid habit id",
                mockPresenter.lastFailMessage.contains("valid habit id must be provided"));
    }

    @Test
    public void testExecute_deletesCorrectHabit_leavesOthersIntact() {
        // Create multiple habits
        Habit habit1 = new Habit("Exercise", "Daily workout", Freq.Daily, new Date(1, 1, 2024), 1);
        Habit habit2 = new Habit("Reading", "Read 30 minutes", Freq.Daily, new Date(1, 1, 2024), 2);
        Habit habit3 = new Habit("Meditation", "Morning meditation", Freq.Daily, new Date(1, 1, 2024), 3);
        fakeDataAccess.addHabit(habit1);
        fakeDataAccess.addHabit(habit2);
        fakeDataAccess.addHabit(habit3);

        DeleteHabitInputData inputData = new DeleteHabitInputData(2);

        interactor.execute(inputData);

        // Assert
        assertTrue("prepareSuccessView should have been called", mockPresenter.wasSuccessCalled);
        assertTrue("Habit 2 should be deleted", fakeDataAccess.findById(2).isEmpty());
        assertTrue("Habit 1 should still exist", fakeDataAccess.findById(1).isPresent());
        assertTrue("Habit 3 should still exist", fakeDataAccess.findById(3).isPresent());
        assertEquals("Two habits should remain", 2, fakeDataAccess.findAll().size());
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
            // No-op for in-memory fake implementation
        }

        // Helper method for tests
        public void addHabit(Habit habit) {
            habits.put(habit.id, habit);
        }
    }

    /**
     * Mock presenter for tracking DeleteHabit output boundary calls.
     */
    private static class MockDeleteHabitPresenter implements DeleteHabitOutputBoundary {
        private boolean wasSuccessCalled = false;
        private boolean wasFailCalled = false;
        private DeleteHabitOutputData lastSuccessData = null;
        private String lastFailMessage = null;

        @Override
        public void prepareSuccessView(DeleteHabitOutputData outputData) {
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
