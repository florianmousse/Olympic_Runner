package jeu.or.olympicrunner.model;

import androidx.annotation.Nullable;

public class User {

    private String uid;
    private String username;
    @Nullable
    private String urlPicture;

    private String scoreperso1, scoreperso2, scoreperso3, dateperso1, dateperso2, dateperso3;

    private String totalGameTime;

    public User() { }

    public User(String uid, String username, @Nullable String urlPicture, String scoreperso1, String scoreperso2, String scoreperso3, String dateperso1, String dateperso2, String dateperso3, String totalGameTime) {
        this.uid = uid;
        this.username = username;
        this.urlPicture = urlPicture;
        this.scoreperso1 = scoreperso1;
        this.scoreperso2 = scoreperso2;
        this.scoreperso3 = scoreperso3;
        this.dateperso1 = dateperso1;
        this.dateperso2 = dateperso2;
        this.dateperso3 = dateperso3;
        this.totalGameTime = totalGameTime;
    }

    // --- GETTERS ---
    public String getUid() { return uid; }
    public String getUsername() { return username; }
    @Nullable
    public String getUrlPicture() { return urlPicture; }

    // --- SETTERS ---
    public void setUsername(String username) { this.username = username; }
    public void setUid(String uid) { this.uid = uid; }
    public void setUrlPicture(@Nullable String urlPicture) { this.urlPicture = urlPicture; }






    public String getScoreperso1() {
        return scoreperso1;
    }

    public void setScoreperso1(String scoreperso1) {
        this.scoreperso1 = scoreperso1;
    }

    public String getScoreperso2() {
        return scoreperso2;
    }

    public void setScoreperso2(String scoreperso2) {
        this.scoreperso2 = scoreperso2;
    }

    public String getScoreperso3() {
        return scoreperso3;
    }

    public void setScoreperso3(String scoreperso3) {
        this.scoreperso3 = scoreperso3;
    }

    public String getDateperso1() {
        return dateperso1;
    }

    public void setDateperso1(String dateperso1) {
        this.dateperso1 = dateperso1;
    }

    public String getDateperso2() {
        return dateperso2;
    }

    public void setDateperso2(String dateperso2) {
        this.dateperso2 = dateperso2;
    }

    public String getDateperso3() {
        return dateperso3;
    }

    public void setDateperso3(String dateperso3) {
        this.dateperso3 = dateperso3;
    }

    public String getTotalGameTime() {
        return totalGameTime;
    }

    public void setTotalGameTime(String totalGameTime) {
        this.totalGameTime = totalGameTime;
    }
}
