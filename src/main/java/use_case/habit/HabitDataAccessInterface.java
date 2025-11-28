package use_case.habit;

import entity.Habit;

import java.util.List;
import java.util.Optional;

/**
 * Data Access Interface for Habit entities.
 * Defines the contract for persisting and retrieving habits.
 */
public interface HabitDataAccessInterface {
    Habit save(Habit habit);

    void save_file();

    Optional<Habit> findById(int id);

    List<Habit> findAll();

    void deleteById(int id);

    int getNextId();
}
