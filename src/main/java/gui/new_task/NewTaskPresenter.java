package gui.new_task;

import use_case.habit.create.CreateHabitOutputBoundary;
import use_case.habit.create.CreateHabitOutputData;

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
        // TODO: prepare a failed view
        System.out.println("Error: " + errorMessage);
    }
}