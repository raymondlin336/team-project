package use_case.task;

import entity.Task;

import java.util.List;
import java.util.Optional;

/**
 * Gateway interface for persisting and retrieving tasks.
 */
public interface TaskDataAccessInterface {
    Task save(Task task);

    Optional<Task> findById(int id);

    List<Task> findAll();

    void deleteById(int id);
}
