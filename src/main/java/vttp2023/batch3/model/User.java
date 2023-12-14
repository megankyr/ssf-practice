package vttp2023.batch3.model;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.validation.constraints.Size;

// add validation login in user class + bind user class to form in view0.html
public class User {

    public User() {
    }

    public User(@Size(min = 2, message = "Username must be at least 2 characters long") String username,
            @Size(min = 2, message = "Password must be at least 2 characters long") String password) {
        this.username = username;
        this.password = password;
    }

    @Size(min = 2, message = "Username must be at least 2 characters long")
    private String username;

    @Size(min = 2, message = "Password must be at least 2 characters long")
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // change the user object into a json object so it can be used to send POST api
    public JsonObject toJSON() {
        return Json.createObjectBuilder()
        .add("username", this.getUsername())
        .add("password", this.getPassword())
        .build();
    }
}
