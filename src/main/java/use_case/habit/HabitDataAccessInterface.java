package use_case.habit;

import entity.UserData;
import main.Habit;

import java.util.List;
import java.util.Optional;

/**
 * Contract required by the habit-related use cases for loading and saving user data.
 */
public interface HabitDataAccessInterface {
    UserData loadUserData();

    void saveUserData(UserData userData);

    Habit saveHabit(Habit habit);

    List<Habit> findHabits();

    Optional<Habit> findHabitById(int habitId);
}
