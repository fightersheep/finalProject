package com.example.finalProjectV1;

public class Friend {
    private String userId;
    private String name;
    private String email;

    public Friend() {
        // Default constructor required for Firebase
    }

    public Friend(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
