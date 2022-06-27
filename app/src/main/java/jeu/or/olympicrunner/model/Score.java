package jeu.or.olympicrunner.model;

public class Score {
    private String score1, user1, time1;

    private String score2, user2, time2;

    private String score3, user3, time3;

    public Score() { }

    public Score(String score1, String user1, String time1, String score2, String user2, String time2, String score3, String user3, String time3) {
        this.score1 = score1;
        this.user1 = user1;
        this.time1 = time1;
        this.score2 = score2;
        this.user2 = user2;
        this.time2 = time2;
        this.score3 = score3;
        this.user3 = user3;
        this.time3 = time3;
    }


    public String getScore1() {
        return score1;
    }

    public void setScore1(String score1) {
        this.score1 = score1;
    }

    public String getUser1() {
        return user1;
    }

    public void setUser1(String user1) {
        this.user1 = user1;
    }

    public String getTime1() {
        return time1;
    }

    public void setTime1(String time1) {
        this.time1 = time1;
    }

    public String getScore2() {
        return score2;
    }

    public void setScore2(String score2) {
        this.score2 = score2;
    }

    public String getUser2() {
        return user2;
    }

    public void setUser2(String user2) {
        this.user2 = user2;
    }

    public String getTime2() {
        return time2;
    }

    public void setTime2(String time2) {
        this.time2 = time2;
    }

    public String getScore3() {
        return score3;
    }

    public void setScore3(String score3) {
        this.score3 = score3;
    }

    public String getUser3() {
        return user3;
    }

    public void setUser3(String user3) {
        this.user3 = user3;
    }

    public String getTime3() {
        return time3;
    }

    public void setTime3(String time3) {
        this.time3 = time3;
    }
}