package stu.edu.phuthuan.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class User implements Serializable {
    private int id;
    private String fullName;
    private String nickName;
    private Date year;
    private List<SocialNetwork> socialNetwork;
    private String email;
    private byte[] image;

    public User() {
        this.socialNetwork = new ArrayList<>();
    }

//    public User(String fullName, String nickName, Date year, List<SocialNetwork> socialNetwork, String email, String avatar) {
//        this.fullName = fullName;
//        this.nickName = nickName;
//        this.year = year;
//        this.socialNetwork = socialNetwork;
//        this.email = email;
//        this.avatar = avatar;
//    }
    public User(String fullName, String nickName, String email) {
        this.id = 0;
        this.fullName = fullName;
        this.nickName = nickName;
        this.email = email;

    }


    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Date getYear() {
        return year;
    }

    public void setYear(Date year) {
        this.year = year;
    }

    public List<SocialNetwork> getSocialNetwork() {
        return socialNetwork;
    }

    public void setSocialNetwork(List<SocialNetwork> socialNetwork) {
        this.socialNetwork = socialNetwork;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public byte[] getAvatar() {
        return image;
    }

    public void setAvatar(byte[] avatar) {
        this.image = avatar;
    }
    public  boolean addSocialNetWord(SocialNetwork scnw){
        return this.socialNetwork.add(scnw);
    }

    @Override
    public String toString() {
        return this.fullName + "\\" + this.nickName + "\\"+
                this.email ;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}


