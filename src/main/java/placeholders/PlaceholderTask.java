package placeholders;

import java.util.ArrayList;
import java.util.Random;

public class PlaceholderTask {
    public String name;
    public String description;
    public String repeat;
    public String due_date;
    public ArrayList<Integer> completion;
    public int colour;
    public PlaceholderTask(String name, String description, String repeat, String due_date, ArrayList<Integer> completion) {
        this.description = description;
        this.name = name;
        this.repeat = repeat;
        this.due_date = due_date;
        this.completion = completion;
        Random random = new Random();
        this.colour = random.nextInt(30) * 12;
    }
}
