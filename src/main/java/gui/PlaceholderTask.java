package gui;

public class PlaceholderTask {
    public String name;
    public Boolean repeat;
    public PlaceholderTask(String name, Boolean repeat) {
        this.name = name;
        this.repeat = repeat;
    }
    public void updateName(String name){
        this.name = name;
    }
}
