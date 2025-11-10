package gui;

import javax.swing.*;

public class IndividualTaskView {
    private IndividualTaskViewModel individualTaskViewModel;
    private JPanel panel;
    private JFrame frame;
    public IndividualTaskView(IndividualTaskViewModel individualTaskViewModel) {
        // Assign viewmodel
        this.individualTaskViewModel = individualTaskViewModel;
        // Activate Jpanels
        panel = new JPanel();
        panel.add(new JLabel("Task:" + individualTaskViewModel.getTaskName()));
        panel.add(new JLabel("Repeat:" + individualTaskViewModel.getRepeat()));
        // Activate Jframes
        frame = new JFrame(individualTaskViewModel.getTaskName());
        frame.setMinimumSize(new java.awt.Dimension(500, 350));
        // default frame settings: closing on exit, packing, etc
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);
        frame.pack();
    }
    public void setVisible(){
        frame.setVisible(true);
    }
    public void setInvisible(){
        frame.setVisible(false);
    }

}