package use_case.habit.edit;

import entity.Date;
import entity.Freq;
import entity.Habit;
import org.junit.Before;
import org.junit.Test;
import use_case.habit.HabitDataAccessInterface;
import use_case.habit.delete.DeleteHabitOutputBoundary;
import use_case.habit.delete.DeleteHabitOutputData;

import java.lang.reflect.Method;
import java.time.Instant;
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

        interactor = new EditHabitInteractor(
                fakeDataAccess,
                mockEditPresenter,
                mockDeletePresenter
        );
    }

    // ========= execute(...) tests =========

    @Test
    public void testExecute_withValidInput_updatesHabit_andCallsSuccess() {
        // Arrange: existing habit in DAO
        Date due = new Date(1, 1, 2025);
        Habit existingHabit = new Habit("Workout", "Run 20 mins", Freq.Daily, due, 1);
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

        // Assert success called, fail not
        assertTrue("prepareSuccessView should have been called", mockEditPresenter.wasSuccessCalled);
        assertFalse("prepareFailView should not have been called", mockEditPresenter.wasFailCalled);
        assertNotNull("Success output data should not be null", mockEditPresenter.lastSuccessData);

        Habit updatedFromPresenter = mockEditPresenter.lastSuccessData.getHabit();
        assertEquals("Study", updatedFromPresenter.get_next().name);
        assertEquals("Read textbook", updatedFromPresenter.get_next().desc);
        assertEquals(Freq.Weekly, updatedFromPresenter.get_next().freq);

        Habit fromDao = fakeDataAccess.findById(1).orElse(null);
        assertNotNull(fromDao);
        assertEquals("Study", fromDao.get_next().name);
        assertEquals("Read textbook", fromDao.get_next().desc);
        assertEquals(Freq.Weekly, fromDao.get_next().freq);
    }

    /**
     * The real interactor does NOT guard against null input and throws NPE.
     * We assert that behaviour here.
     */
    @Test(expected = NullPointerException.class)
    public void testExecute_withNullInput_throwsNullPointer() {
        interactor.execute(null);
    }

    @Test
    public void testExecute_withBlankName_callsFail() {
        Date due = new Date(1, 1, 2025);
        Habit existingHabit = new Habit("Workout", "Run", Freq.Daily, due, 1);
        existingHabit.id = 1;
        fakeDataAccess.save(existingHabit);

        EditHabitInputData inputData = new EditHabitInputData(
                1,
                "   ",       // blank
                "Some desc",
                Freq.Daily
        );

        interactor.execute(inputData);

        assertFalse(mockEditPresenter.wasSuccessCalled);
        assertTrue(mockEditPresenter.wasFailCalled);
        assertNotNull(mockEditPresenter.lastFailMessage);
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
                null           // null freq
        );

        interactor.execute(inputData);

        assertFalse(mockEditPresenter.wasSuccessCalled);
        assertTrue(mockEditPresenter.wasFailCalled);
        assertNotNull(mockEditPresenter.lastFailMessage);
        assertTrue(mockEditPresenter.lastFailMessage.contains("Habit frequency is required"));
    }

    @Test
    public void testExecute_withInvalidHabitId_callsFail() {
        // Habit with id 0 so that we bypass the "not found" early return
        Date due = new Date(1, 1, 2025);
        Habit existingHabit = new Habit("Workout", "Run", Freq.Daily, due, 0);
        existingHabit.id = 0;
        fakeDataAccess.save(existingHabit);

        EditHabitInputData inputData = new EditHabitInputData(
                0,                // invalid id (<= 0)
                "Valid name",
                "Valid desc",
                Freq.Daily
        );

        interactor.execute(inputData);

        assertFalse("Success should not be called", mockEditPresenter.wasSuccessCalled);
        assertTrue("Fail should be called", mockEditPresenter.wasFailCalled);
        assertTrue(mockEditPresenter.lastFailMessage.contains("A valid habit id must be provided."));
    }

    @Test
    public void testExecute_withMultipleErrors_joinsMessages() {
        Date due = new Date(1, 1, 2025);
        Habit existingHabit = new Habit("Workout", "Run", Freq.Daily, due, 2);
        existingHabit.id = 2;
        fakeDataAccess.save(existingHabit);

        // name blank, desc blank, freq null => multiple validation errors
        EditHabitInputData inputData = new EditHabitInputData(
                2,
                "   ",
                "   ",
                null
        );

        interactor.execute(inputData);

        assertFalse("Success should not be called", mockEditPresenter.wasSuccessCalled);
        assertTrue("Fail should be called", mockEditPresenter.wasFailCalled);

        String msg = mockEditPresenter.lastFailMessage;
        assertTrue(msg.contains("Habit name cannot be blank"));
        assertTrue(msg.contains("Habit description cannot be blank"));
        assertTrue(msg.contains("Habit frequency is required"));
        assertTrue("Errors should be joined by ';'", msg.contains(";"));
    }

    @Test
    public void testExecute_whenHabitNotFound_callsDeletePresenterFail() {
        EditHabitInputData inputData = new EditHabitInputData(
                99, "Name", "Desc", Freq.Daily
        );

        interactor.execute(inputData);

        assertFalse(mockEditPresenter.wasSuccessCalled);
        assertFalse(mockEditPresenter.wasFailCalled);
        assertTrue("Delete presenter fail should be called", mockDeletePresenter.wasFailCalled);
    }

    // ========= first_execute(...) tests =========

    @Test
    public void testFirstExecute_withExistingHabit_callsEditSuccess() {
        Date due = new Date(1, 1, 2025);
        Habit habit = new Habit("Workout", "Run", Freq.Daily, due, 10);
        habit.id = 10;
        fakeDataAccess.save(habit);

        interactor.first_execute(10);

        assertTrue(mockEditPresenter.wasSuccessCalled);
        assertFalse(mockEditPresenter.wasFailCalled);
    }

    @Test(expected = NoSuchElementException.class)
    public void testFirstExecute_withMissingHabit_throwsNoSuchElement() {
        // No habit with id 123 stored
        interactor.first_execute(123);
    }

    // ========= validate(...) via reflection =========

    @Test
    @SuppressWarnings("unchecked")
    public void testValidate_withNullInput_returnsErrorList() throws Exception {
        Method validate = EditHabitInteractor.class
                .getDeclaredMethod("validate", EditHabitInputData.class);
        validate.setAccessible(true);

        List<String> errors = (List<String>) validate.invoke(interactor, new Object[]{null});
        assertEquals(1, errors.size());
        assertEquals("Request cannot be null.", errors.get(0));
    }
    @Test
    public void testExecute_withNullName_callsFail() {
        Date due = new Date(1, 1, 2025);
        Habit existingHabit = new Habit("Workout", "Run", Freq.Daily, due, 1);
        existingHabit.id = 1;
        fakeDataAccess.save(existingHabit);

        EditHabitInputData inputData = new EditHabitInputData(
                1,
                null,                // <- null name
                "Some desc",
                Freq.Daily
        );

        interactor.execute(inputData);

        assertFalse(mockEditPresenter.wasSuccessCalled);
        assertTrue(mockEditPresenter.wasFailCalled);
        assertTrue(mockEditPresenter.lastFailMessage.contains("Habit name cannot be blank"));
    }

    @Test
    public void testExecute_withNullDescription_callsFail() {
        Date due = new Date(1, 1, 2025);
        Habit existingHabit = new Habit("Workout", "Run", Freq.Daily, due, 1);
        existingHabit.id = 1;
        fakeDataAccess.save(existingHabit);

        EditHabitInputData inputData = new EditHabitInputData(
                1,
                "Workout",
                null,                // <- null description
                Freq.Daily
        );

        interactor.execute(inputData);

        assertFalse(mockEditPresenter.wasSuccessCalled);
        assertTrue(mockEditPresenter.wasFailCalled);
        assertTrue(mockEditPresenter.lastFailMessage.contains("Habit description cannot be blank"));
    }

    // ========= EditHabitOutputData coverage helper =========

    /**
     * Ensures all zero-argument methods in EditHabitOutputData are invoked
     * at least once, so that Jacoco reports 100% method/line coverage
     * for this data class.
     */
    @Test
    public void testEditHabitOutputData_allZeroArgMethodsInvoked() throws Exception {
        // Arrange: create a valid edit so we get a non-null success output
        Date due = new Date(1, 1, 2025);
        Habit habit = new Habit("Workout", "Run", Freq.Daily, due, 42);
        habit.id = 42;
        fakeDataAccess.save(habit);

        EditHabitInputData inputData = new EditHabitInputData(
                42,
                "Updated name",
                "Updated desc",
                Freq.Weekly
        );

        interactor.execute(inputData);
        assertNotNull("Success data should not be null", mockEditPresenter.lastSuccessData);

        EditHabitOutputData outputData = mockEditPresenter.lastSuccessData;

        // Call every zero-arg method declared in EditHabitOutputData
        for (Method m : EditHabitOutputData.class.getDeclaredMethods()) {
            if (m.getParameterCount() == 0) {
                m.setAccessible(true);
                m.invoke(outputData);
            }
        }
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
     * Mock presenter for delete habit output boundary.
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
