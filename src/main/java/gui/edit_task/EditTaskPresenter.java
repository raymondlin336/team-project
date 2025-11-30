package gui.edit_task;

import use_case.habit.delete.DeleteHabitOutputBoundary;
import use_case.habit.delete.DeleteHabitOutputData;
import use_case.habit.edit.EditHabitOutputBoundary;
import use_case.habit.edit.EditHabitOutputData;

public class EditTaskPresenter implements EditHabitOutputBoundary, DeleteHabitOutputBoundary {
    private EditTaskViewModel editTaskViewModel;
    public  EditTaskPresenter(EditTaskViewModel editTaskViewModel) {
        this.editTaskViewModel = editTaskViewModel;
    }
    @Override
    public void prepareSuccessView(EditHabitOutputData outputData) {
        System.out.println("Edit Task success view called: update");
        editTaskViewModel.updateEditTask(outputData);
    }

    @Override
    public void prepareSuccessView(DeleteHabitOutputData outputData) {
        System.out.println("Edit Task success view called: delete");
        editTaskViewModel.updateEditTask(outputData);
    }

    @Override
    public void prepareFailView(String errorMessage) {
        System.out.println("Edit Task fail view called: " + errorMessage);
        editTaskViewModel.updateEditTask(errorMessage);
    }

    @Override
    public void prepareFailView(EditHabitOutputData outputData, String errorMessage) {
        System.out.println("Edit Task fail view called: " + errorMessage);
        editTaskViewModel.updateEditTask(outputData, errorMessage);
    }
}
