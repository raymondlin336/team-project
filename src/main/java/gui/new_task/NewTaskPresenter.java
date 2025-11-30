package gui.new_task;

import gui.edit_task.EditTaskController;
import use_case.habit.create.CreateHabitOutputBoundary;
import use_case.habit.create.CreateHabitOutputData;
import use_case.habit.overview.get.GetHabitsOutputData;

public class NewTaskPresenter implements CreateHabitOutputBoundary {
    private NewTaskViewModel newTaskViewModel;
    public NewTaskPresenter(NewTaskViewModel newTaskViewModel) {
        this.newTaskViewModel = newTaskViewModel;
    }

    @Override
    public void prepareSuccessView(CreateHabitOutputData outputData) {
        newTaskViewModel.updateNewTask(outputData);
    }

    @Override
    public void prepareFailView(String errorMessage) {
        newTaskViewModel.updateNewTaskFailed(errorMessage);
        System.out.println("Error: " + errorMessage);
    }
}
