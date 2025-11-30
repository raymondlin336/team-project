package use_case.habit.edit;

import entity.Date;
import entity.Freq;
import entity.Habit;
import org.junit.Before;
import org.junit.Test;
import use_case.habit.HabitDataAccessInterface;
import use_case.habit.delete.DeleteHabitOutputBoundary;
import use_case.habit.delete.DeleteHabitOutputData;

import java.util.*;

import static org.junit.Assert.*;

/**
 * Unit tests for EditHabitInteractor.
 */
public class EditHabitInteractorTests {

    private FakeHabitDataAccess fakeDataAccess;
    private MockEditHabitPresenter mockEditPresenter;
    private MockDeleteHabitPresenter mockDeletePresenter;
    private EditHabitInteractor interactor;

    @Before
    public void setUp() {
        fakeDataAccess = new FakeHabitDataAccess();
        mockEditPresenter = new MockEditHabitPresenter();
        mockDeletePresenter = new MockDeleteHabitPresenter();

        // NOTE: EditHabitInteractor currently expects THREE arguments
        interactor = new EditHabitInteractor(
                fakeDataAccess,
                mockEditPresenter,
                mockDeletePresenter
        );
    }

    @Test
    public void testExecute_withValidInput_updatesHabit_andCallsSuccess() {
        // Arrange: existing habit stored in the fake DAO
        Date due = new Date(1, 1, 2025);
        Habit existingHabit = new Habit("Workout", "Run 20 mins", Freq.Daily, due, 1);
        // In current Habit implementation id is not set in constructor, so ensure it here:
        existingHabit.id = 1;
        fakeDataAccess.save(existingHabit);

        EditHabitInputData inputData = new EditHabitInputData(
                1,
                "Study",
                "Read textbook",
                Freq.Weekly
        );

        // Act
        interactor.execute(inputData);

        // Assert: success called, fail not called
        assertTrue("prepareSuccessView should have been called", mockEditPresenter.wasSuccessCalled);
        assertFalse("prepareFailView should not have been called", mockEditPresenter.wasFailCalled);
        assertNotNull("Success output data should not be null", mockEditPresenter.lastSuccessData);

        Habit updatedFromPresenter = mockEditPresenter.lastSuccessData.getHabit();
        assertEquals("Study", updatedFromPresenter.get_next().name);
        assertEquals("Read textbook", updatedFromPresenter.get_next().desc);
        assertEquals(Freq.Weekly, updatedFromPresenter.get_next().freq);

        // Also verify DAO was updated
        Habit fromDao = fakeDataAccess.findById(1).orElse(null);
        assertNotNull("Habit should still exist in DAO", fromDao);
        assertEquals("Study", fromDao.get_next().name);
        assertEquals("Read textbook", fromDao.get_next().desc);
        assertEquals(Freq.Weekly, fromDao.get_next().freq);
    }



    @Test
    public void testExecute_withBlankName_callsFail() {
        // Habit exists so we don’t hit the “not found” branch
        Date due = new Date(1, 1, 2025);
        Habit existingHabit = new Habit("Workout", "Run", Freq.Daily, due, 1);
        existingHabit.id = 1;
        fakeDataAccess.save(existingHabit);

        EditHabitInputData inputData = new EditHabitInputData(
                1,
                "   ",        // blank name
                "Some desc",
                Freq.Daily
        );

        interactor.execute(inputData);

        assertFalse(mockEditPresenter.wasSuccessCalled);
        assertTrue(mockEditPresenter.wasFailCalled);
        assertTrue(mockEditPresenter.lastFailMessage.contains("Habit name cannot be blank"));
    }

    @Test
    public void testExecute_withNullFrequency_callsFail() {
        Date due = new Date(1, 1, 2025);
        Habit existingHabit = new Habit("Workout", "Run", Freq.Daily, due, 1);
        existingHabit.id = 1;
        fakeDataAccess.save(existingHabit);

        EditHabitInputData inputData = new EditHabitInputData(
                1,
                "Workout",
                "Run",
                null          // null freq
        );

        interactor.execute(inputData);

        assertFalse(mockEditPresenter.wasSuccessCalled);
        assertTrue(mockEditPresenter.wasFailCalled);
        assertTrue(mockEditPresenter.lastFailMessage.contains("Habit frequency is required"));
    }

    // ===================== Fakes & Mocks =====================

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
            // no-op in tests
        }
    }

    /**
     * Mock presenter for tracking EditHabit output boundary calls.
     */
    private static class MockEditHabitPresenter implements EditHabitOutputBoundary {
        boolean wasSuccessCalled = false;
        boolean wasFailCalled = false;
        EditHabitOutputData lastSuccessData = null;
        EditHabitOutputData lastFailData = null;
        String lastFailMessage = null;

        @Override
        public void prepareSuccessView(EditHabitOutputData outputData) {
            wasSuccessCalled = true;
            lastSuccessData = outputData;
        }

        @Override
        public void prepareFailView(EditHabitOutputData outputData, String errorMessage) {
            wasFailCalled = true;
            lastFailData = outputData;
            lastFailMessage = errorMessage;
        }
    }

    /**
     * Mock presenter for DeleteHabit output boundary calls.
     * (Interactor expects this dependency but we don't assert on it here.)
     */
    private static class MockDeleteHabitPresenter implements DeleteHabitOutputBoundary {
        boolean wasSuccessCalled = false;
        boolean wasFailCalled = false;

        @Override
        public void prepareSuccessView(DeleteHabitOutputData outputData) {
            wasSuccessCalled = true;
        }

        @Override
        public void prepareFailView(String errorMessage) {
            wasFailCalled = true;
        }
    }
}
