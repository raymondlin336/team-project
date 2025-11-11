package placeholders;

public class PlaceholderTask {
    public String name;
    public String description;
    public String repeat;
    public String due_date;
    public PlaceholderTask(String name, String description, String repeat, String due_date) {
        this.description = description;
        this.name = name;
        this.repeat = repeat;
        this.due_date = due_date;
    }
}
