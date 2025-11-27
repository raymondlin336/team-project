package entity;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import org.json.JSONObject;

public class JSONIO {
    public String filename;

    public JSONIO(String filename) {
        this.filename = filename;
    }

    public User getUser() {
        try {
            String jsonString = Files.readString(Path.of(this.filename));
            JSONObject json = new JSONObject(jsonString);
            return User.fromJSON(json);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void saveUser(User u) {
        JSONObject json = u.toJSON();
        String string = json.toString();
        try {
            Files.writeString(Path.of(this.filename), string,
                    StandardOpenOption.CREATE);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
    }
}
