package gui.Home;

import javax.swing.*;

public class HomeView {
    private HomeViewModel homeViewModel;
    private JFrame frame;


    private JPanel MainPanel;
    private JPanel ModalPanel; /// Day, Week, Month
    private JPanel SettingsPanel;
    private JPanel TasksPanel;
    private JPanel ButtonsPanel;

    public HomeView(HomeViewModel homeViewModel) {
        // Assign viewmodel
        this.homeViewModel = homeViewModel;
        frame = new JFrame("Home");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void createUIComponents() {}

}
