package gui.edit_task;

import use_case.habit.edit.EditHabitOutputBoundary;
import use_case.habit.edit.EditHabitOutputData;

public class EditTaskPresenter implements EditHabitOutputBoundary {
    private EditTaskViewModel editTaskViewModel;
    public  EditTaskPresenter(EditTaskViewModel editTaskViewModel) {
        this.editTaskViewModel = editTaskViewModel;
    }
    @Override
    public void prepareSuccessView(EditHabitOutputData outputData) {
        System.out.println("Edit Task success view called");
        editTaskViewModel.updateEditTask(outputData);
    }

    @Override
    public void prepareFailView(String errorMessage) {
        System.out.println("Edit Task fail view called: " + errorMessage);
        // TODO: prepare a failed view
    }
}
