package com.example.finalProjectV1;

public class User {
    private String userId;
    private String name;
    private String discriminator; // 4-digit ID
    private String age;
    private String location;

    // Default constructor for Firebase
    public User() {}

    public String getDiscriminator() {
        return discriminator;
    }

    public void setDiscriminator(String discriminator) {
        this.discriminator = discriminator;
    }

    public String getUserId() {
        return userId;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    // Getters and setters
    // Add your getters and setters here
}
