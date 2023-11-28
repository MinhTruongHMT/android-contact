package stu.edu.phuthuan.model;

public class SocialNetwork {
    private String key;
    private String value;

    public SocialNetwork(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public SocialNetwork() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
