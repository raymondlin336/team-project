package use_case.habit.create;

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
 * Unit tests for CreateHabitInteractor.
 */
public class CreateHabitInteractorTests {

    private FakeHabitDataAccess fakeDataAccess;
    private MockCreateHabitPresenter mockPresenter;
    private CreateHabitInteractor interactor;

    @Before
    public void setUp() {
        fakeDataAccess = new FakeHabitDataAccess();
        mockPresenter = new MockCreateHabitPresenter();
        interactor = new CreateHabitInteractor(fakeDataAccess, mockPresenter);
    }

    @Test
    public void testExecute_withValidInput_callsPresenterSuccess() {
        CreateHabitInputData inputData = new CreateHabitInputData(
                "Morning Exercise",
                "Daily workout routine",
                Freq.Daily
        );

        interactor.execute(inputData);

        // Assert
        assertTrue("prepareSuccessView should have been called", mockPresenter.wasSuccessCalled);
        assertFalse("prepareFailView should not have been called", mockPresenter.wasFailCalled);
        assertNotNull("Output data should not be null", mockPresenter.lastSuccessData);

        Habit createdHabit = mockPresenter.lastSuccessData.getHabit();
        assertEquals("Habit name should match", "Morning Exercise", createdHabit.get_next().name);
        assertEquals("Habit description should match", "Daily workout routine", createdHabit.get_next().desc);
        assertEquals("Habit frequency should match", Freq.Daily, createdHabit.get_next().freq);
        assertNotNull("Habit should have a due date", createdHabit.get_next().deadline);

        // Verify save was called
        assertEquals("One habit should be saved", 1, fakeDataAccess.findAll().size());
    }

    @Test
    public void testExecute_withBlankName_callsPresenterFail() {
        CreateHabitInputData inputData = new CreateHabitInputData(
                "   ",
                "Description",
                Freq.Weekly
        );

        interactor.execute(inputData);

        // Assert
        assertFalse("prepareSuccessView should not have been called", mockPresenter.wasSuccessCalled);
        assertTrue("prepareFailView should have been called", mockPresenter.wasFailCalled);
        assertTrue("Error message should mention blank name",
                mockPresenter.lastFailMessage.contains("Habit name cannot be blank"));
    }

    @Test
    public void testExecute_withNullFrequency_callsPresenterFail() {
        CreateHabitInputData inputData = new CreateHabitInputData(
                "Valid Name",
                "Description",
                null  // Null frequency
        );

        interactor.execute(inputData);

        // Assert
        assertFalse("prepareSuccessView should not have been called", mockPresenter.wasSuccessCalled);
        assertTrue("prepareFailView should have been called", mockPresenter.wasFailCalled);
        assertTrue("Error message should mention frequency required",
                mockPresenter.lastFailMessage.contains("Frequency is required"));
    }

    @Test
    public void testExecute_withMultipleErrors_joinsErrors() {
        CreateHabitInputData inputData = new CreateHabitInputData(
                "",    // Blank name
                "Description",
                null   // Null frequency
        );

        interactor.execute(inputData);

        // Assert
        assertFalse("prepareSuccessView should not have been called", mockPresenter.wasSuccessCalled);
        assertTrue("prepareFailView should have been called", mockPresenter.wasFailCalled);
        assertTrue("Error message should mention blank name",
                mockPresenter.lastFailMessage.contains("Habit name cannot be blank"));
        assertTrue("Error message should mention frequency",
                mockPresenter.lastFailMessage.contains("Frequency is required"));
        assertTrue("Errors should be joined with semicolon",
                mockPresenter.lastFailMessage.contains(";"));
    }

    @Test
    public void testExecute_withNullInputData_callsPresenterFail() {
        // Act
        interactor.execute(null);

        // Assert
        assertFalse("prepareSuccessView should not have been called", mockPresenter.wasSuccessCalled);
        assertTrue("prepareFailView should have been called", mockPresenter.wasFailCalled);
        assertTrue("Error message should mention null request",
                mockPresenter.lastFailMessage.contains("Request cannot be null"));
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
    }

    /**
     * Mock presenter for tracking CreateHabit output boundary calls.
     */
    private static class MockCreateHabitPresenter implements CreateHabitOutputBoundary {
        private boolean wasSuccessCalled = false;
        private boolean wasFailCalled = false;
        private CreateHabitOutputData lastSuccessData = null;
        private String lastFailMessage = null;

        @Override
        public void prepareSuccessView(CreateHabitOutputData outputData) {
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
