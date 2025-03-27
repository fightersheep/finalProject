package com.example.finalProjectV1.classes;

public class ShortUser {
    protected String userId;
    protected String name;

    public ShortUser() {} // Needed for Firebase

    public ShortUser(String key, String value) {
        this.userId = key;
        this.name = value;
    }
    public ShortUser(ShortUser shortUser) {
        this.userId = shortUser.userId;
        this.name = shortUser.name;
    }

    // Getters and setters
    public String getId() { return userId; }
    public void setId(String key) { this.userId = key; }
    public String getName() { return name; }
    public void setName(String value) { this.name = value; }

    public void setUser(ShortUser user1) {
        this.userId =user1.userId;
        this.name = user1.name;
    }
}
