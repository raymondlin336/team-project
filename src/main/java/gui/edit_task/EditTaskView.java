package gui.edit_task;
import gui.home.HomeViewComponents;
import gui.task.TaskView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class EditTaskView extends TaskView implements PropertyChangeListener {
    private EditTaskViewModel editTaskViewModel;
    private EditTaskController editTaskController;
    private JButton save;
    private JButton delete;

    public EditTaskView(EditTaskViewModel editTaskViewModel, EditTaskController editTaskController) {
        super("Edit Task",  editTaskController);
        this.editTaskViewModel = editTaskViewModel;
        this.editTaskViewModel.addPropertyChangeListener(this);
        this.editTaskController = editTaskController;
        createUI(800, 600, view_name);
        addSaveButton();
        addCancelButton();
        addDeleteButton();
    }

    private void addSaveButton(){
        save = new HomeViewComponents.PillButton("Save");
        save.setBorderPainted(false);
        save.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int habit_id = editTaskViewModel.getID();
                String habit_name = habitNameTF.getText();
                String habit_desc = habitDescTF.getText();
                String habit_repeat = habitRepeatCB.getSelectedItem().toString();
                editTaskController.save_habit_info(habit_id, habit_name, habit_desc, habit_repeat);
                mainframe.dispose();
            }
        });
        buttonsRow.add(save);
    }

    private void addDeleteButton(){
        delete = new HomeViewComponents.PillButton("Delete");
        delete.setBorderPainted(false);
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

    public EditTaskController getEditTaskController() {
        return editTaskController;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("AddSuccess")) {
            editTaskController.showHomeWindow();
        }
    }
}