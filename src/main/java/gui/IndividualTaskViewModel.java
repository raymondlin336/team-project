package gui;

public class IndividualTaskViewModel {
    private PlaceholderTask task;
    public IndividualTaskViewModel(PlaceholderTask task) {
        this.task = task;
    }
    public void updateName(String name){
        task.updateName(name);
    }
}
