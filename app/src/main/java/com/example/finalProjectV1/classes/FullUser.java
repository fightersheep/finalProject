package com.example.finalProjectV1.classes;
public class FullUser extends ShortUser {



    private String profileImage;
    private String email;
    private String firstName;
    private String lastName;
    private String discriminator;
    private String location;
    private String dateOfBirth;
    private String experience;
    private String gender;
    private String country;

    // Constructor
    public FullUser(){ super();}

    public FullUser(String userId, String email, String firstName, String lastName, String location,
                    String dateOfBirth, String experience, String gender, String country, String profileImage) {
        super(userId,(firstName + " " + lastName).trim());
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName != null ? lastName : "";
        this.discriminator = generateDiscriminator();
        this.location = location;
        this.dateOfBirth = dateOfBirth;
        this.experience = experience;
        this.gender = gender;
        this.country = country;
        this.profileImage = profileImage;
    }

    // Added getter and setter for userId
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    // Rest of the getters and setters remain the same
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
        this.name = (this.firstName + " " + this.lastName).trim();
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
        this.name = (this.firstName + " " + this.lastName).trim();
    }

    public String getName() {
        return name;
    }

    public String getDiscriminator() {
        return discriminator;
    }

    public void setDiscriminator(String discriminator) {
        this.discriminator = discriminator;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    private String generateDiscriminator() {
        return String.format("%04d", (int)(Math.random() * 10000));
    }

    public FullUser Clone() {
        FullUser cloned = new FullUser();

        cloned.userId = this.userId;
        cloned.email = this.email;
        cloned.firstName = this.firstName;
        cloned.lastName = this.lastName;
        cloned.name = this.name;
        cloned.discriminator = this.discriminator;
        cloned.location = this.location;
        cloned.dateOfBirth = this.dateOfBirth;
        cloned.experience = this.experience;
        cloned.gender = this.gender;
        cloned.country = this.country;
        cloned.profileImage = this.profileImage;

        return cloned;
    }
    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
}
