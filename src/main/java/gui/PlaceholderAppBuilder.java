package gui;

public class PlaceholderAppBuilder {
    public static void main(String[] args) {
        IndividualTaskView view = new IndividualTaskView(new IndividualTaskViewModel(new PlaceholderTask("task1", true)));
        view.setVisible();
    }
}
