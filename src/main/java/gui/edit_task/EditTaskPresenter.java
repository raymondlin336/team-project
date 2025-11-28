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
        editTaskViewModel.updateEditTask(outputData);
    }

    @Override
    public void prepareFailView(String errorMessage) {
        // TODO: prepare a failed view
    }
}
