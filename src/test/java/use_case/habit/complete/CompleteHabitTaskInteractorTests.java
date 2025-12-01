package use_case.habit.complete;

import entity.Date;
import entity.Freq;
import entity.Habit;
import entity.Task;
import org.junit.Before;
import org.junit.Test;
import use_case.habit.HabitDataAccessInterface;

import java.util.*;

import static org.junit.Assert.*;

/**
 * Unit tests for CompleteHabitTaskInteractor.
 */
public class CompleteHabitTaskInteractorTests {

    private FakeHabitDataAccess fakeDataAccess;
    private MockCompleteHabitTaskPresenter mockPresenter;
    private CompleteHabitTaskInteractor interactor;

    @Before
    public void setUp() {
        fakeDataAccess = new FakeHabitDataAccess();
        mockPresenter = new MockCompleteHabitTaskPresenter();
        interactor = new CompleteHabitTaskInteractor(fakeDataAccess, mockPresenter);
    }

    // ---------- Happy path: habit exists, current task incomplete ----------

    @Test
    public void testExecute_withValidInput_completesAndAddsNextTask() {
        // Arrange: habit with one incomplete task
        Date due = new Date(1, 1, 2025);
        Habit habit = new Habit("Workout", "Run", Freq.Daily, due, 1);
        habit.id = 1;                       // make sure id matches DAO key
        fakeDataAccess.save(habit);

        int beforeSize = habit.tasks.size();
        assertEquals(1, beforeSize);
        assertFalse("Current task should start incomplete", habit.get_next().completed);

        CompleteHabitTaskInputData inputData =
                new CompleteHabitTaskInputData(1);

        // Act
        interactor.execute(inputData);

        // Assert presenter success
        assertTrue("Success view should be called", mockPresenter.wasSuccessCalled);
        assertFalse("Fail view should not be called", mockPresenter.wasFailCalled);
        assertNotNull("Output data should not be null", mockPresenter.lastSuccessData);

        // *** Use the getter so CompleteHabitTaskOutputData is fully covered ***
        Habit outputHabit = mockPresenter.lastSuccessData.getHabit();
        assertNotNull("Habit in output data should not be null", outputHabit);

        Habit updated = fakeDataAccess.findById(1).orElse(null);
        assertNotNull(updated);

        // After completion, previous task should be completed
        assertTrue("Most recent task should be completed",
                updated.tasks.get(0).completed);
        // And a new next task should be added (since freq is recurring)
        assertEquals("A new task should be added",
                beforeSize + 1, updated.tasks.size());
        Task newTask = updated.get_next();
        assertFalse("New next task should be incomplete", newTask.completed);
    }

    // ---------- Current task already completed: no-op but success ----------

    @Test
    public void testExecute_whenCurrentTaskAlreadyCompleted_doesNotAddNewTask() {
        // Arrange: habit with one already-completed task
        Date due = new Date(1, 1, 2025);
        Habit habit = new Habit("Workout", "Run", Freq.Daily, due, 2);
        habit.id = 2;
        habit.get_next().completed = true;   // simulate user already completed it
        fakeDataAccess.save(habit);

        int beforeSize = habit.tasks.size();

        CompleteHabitTaskInputData inputData =
                new CompleteHabitTaskInputData(2);

        // Act
        interactor.execute(inputData);

        // Assert: still success, but no new tasks added
        assertTrue(mockPresenter.wasSuccessCalled);
        assertFalse(mockPresenter.wasFailCalled);

        Habit updated = fakeDataAccess.findById(2).orElse(null);
        assertNotNull(updated);
        assertEquals("No new task should be added when already complete",
                beforeSize, updated.tasks.size());
    }

    // ---------- Validation: null input ----------

    @Test
    public void testExecute_withNullInput_callsFail() {
        interactor.execute(null);

        assertFalse("Success should not be called", mockPresenter.wasSuccessCalled);
        assertTrue("Fail should be called", mockPresenter.wasFailCalled);
        assertNotNull(mockPresenter.lastFailMessage);
        assertTrue(mockPresenter.lastFailMessage.contains("Request cannot be null"));
    }

    // ---------- Validation: invalid habit id (< 0) ----------

    @Test
    public void testExecute_withInvalidHabitId_callsFail() {
        CompleteHabitTaskInputData inputData =
                new CompleteHabitTaskInputData(-1);

        interactor.execute(inputData);

        assertFalse(mockPresenter.wasSuccessCalled);
        assertTrue(mockPresenter.wasFailCalled);
        assertTrue(mockPresenter.lastFailMessage.contains("A valid habit id must be provided"));
    }

    // ---------- Habit not found in DAO ----------

    @Test
    public void testExecute_whenHabitNotFound_callsFail() {
        // id is non-negative so we pass validation, but DAO has no habit
        CompleteHabitTaskInputData inputData =
                new CompleteHabitTaskInputData(99);

        interactor.execute(inputData);

        assertFalse(mockPresenter.wasSuccessCalled);
        assertTrue(mockPresenter.wasFailCalled);
        assertTrue(mockPresenter.lastFailMessage.contains("Habit 99 does not exist"));
    }

    // ===================== Fakes & Mocks =====================

    /**
     * In-memory fake DAO for habits.
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
            // no-op for tests
        }
    }

    /**
     * Mock presenter implementing the CompleteHabitTaskOutputBoundary.
     */
    private static class MockCompleteHabitTaskPresenter
            implements CompleteHabitTaskOutputBoundary {

        boolean wasSuccessCalled = false;
        boolean wasFailCalled = false;
        CompleteHabitTaskOutputData lastSuccessData = null;
        String lastFailMessage = null;

        @Override
        public void prepareSuccessView(CompleteHabitTaskOutputData outputData) {
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
