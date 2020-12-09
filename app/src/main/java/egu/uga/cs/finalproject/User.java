package egu.uga.cs.finalproject;

public class User {

    private String userName;
    private String email;
    private String groupID;


    public User() {

        userName="";
        email="";
        groupID="";
    }

    public User(String userName, String email) {
        this.userName = userName;
        this.email = email;
        //this.groupID = groupID;
    }

    public String getName() {
        return userName;
    }


    public String getEmail() {
        return email;
    }

    public String getGroupID() {
        return groupID;
    }

    public void setName(String username) {
        this.userName = username;
    }


    public void setEmail(String email) {
        this.email = email;
    }

    public void setGroupID(String groupID) {
        this.groupID = groupID;
    }
}


