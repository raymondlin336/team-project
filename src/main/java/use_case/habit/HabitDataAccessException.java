package use_case.habit;

/**
 * Runtime exception thrown when the habit data access object cannot complete an operation.
 */
public class HabitDataAccessException extends RuntimeException {
    public HabitDataAccessException(String message) {
        super(message);
    }

    public HabitDataAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
