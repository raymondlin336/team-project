package gui.edit_task;
import gui.task.TaskView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EditTaskView extends TaskView {
    private EditTaskViewModel editTaskViewModel;
    private EditTaskController editTaskController;
    private JButton save;
    private JButton delete;

    public EditTaskView(EditTaskViewModel editTaskViewModel, EditTaskController editTaskController) {
        super("Edit Task",  editTaskController);
        this.editTaskViewModel = editTaskViewModel;
        this.editTaskController = editTaskController;
        createUI(800, 600, view_name);
        addSaveButton();
        addCancelButton();
        addDeleteButton();
        showHabitInfo();
    }

    private void addSaveButton(){
        save = getSizedButton(100, 36, "Save", new Color(67, 182, 240));
        save.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int habit_id = editTaskViewModel.getID();
                String habit_name = habitNameTF.getText();
                String habit_desc = habitDescTF.getText();
                String habit_repeat = habitRepeatCB.getSelectedItem().toString();
                editTaskController.save_habit_info(habit_id, habit_name, habit_desc, habit_repeat);
            }
        });
        buttonsRow.add(save);
    }

    private void addDeleteButton(){
        delete = getSizedButton(100, 36, "Delete", new Color(240, 102, 67));
        delete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int habit_id = editTaskViewModel.getID();
                editTaskController.delete_habit(habit_id);
                mainframe.dispose();
            }
        });
        buttonsRow.add(delete);
    }

    private void showHabitInfo(){
        habitNameTF.setText(editTaskViewModel.getName());
        habitDescTF.setText(editTaskViewModel.getDesc());
        habitRepeatCB.setSelectedItem(editTaskViewModel.getRepeat());
        habitDueLB.setText(editTaskViewModel.getDueDate());
    }
}