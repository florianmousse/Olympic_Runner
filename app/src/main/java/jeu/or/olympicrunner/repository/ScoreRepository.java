package jeu.or.olympicrunner.repository;

import jeu.or.olympicrunner.model.Score;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public final class ScoreRepository {

    private static final String COLLECTION_NAME = "scores";
    private static final String SCORE1_FIELD = "score1";
    private static final String USER1_FIELD = "user1";
    private static final String TIME1_FIELD = "time1";
    private static final String SCORE2_FIELD = "score2";
    private static final String USER2_FIELD = "user2";
    private static final String TIME2_FIELD = "time2";
    private static final String SCORE3_FIELD = "score3";
    private static final String USER3_FIELD = "user3";
    private static final String TIME3_FIELD = "time3";

    private static volatile ScoreRepository instance;

    private ScoreRepository() {
    }

    public static ScoreRepository getInstance() {
        ScoreRepository result = instance;
        if (result != null) {
            return result;
        }
        synchronized (ScoreRepository.class) {
            if (instance == null) {
                instance = new ScoreRepository();
            }
            return instance;
        }
    }

    /* FIRESTORE */

    // Get the Collection Reference
    private CollectionReference getScoreCollection() {
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    // Create User in Firestore
    public void createScore() {
        Score scoreToCreate = new Score("0", "", "", "0", "", "", "0", "", "");

        Task<DocumentSnapshot> scoreData = getScoreData();

        scoreData.addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.contains(SCORE1_FIELD)) {
                scoreToCreate.setScore1((String) documentSnapshot.get(SCORE1_FIELD));
            }
            if (documentSnapshot.contains(USER1_FIELD)) {
                scoreToCreate.setUser1((String) documentSnapshot.get(USER1_FIELD));
            }
            if (documentSnapshot.contains(TIME1_FIELD)) {
                scoreToCreate.setTime1((String) documentSnapshot.get(TIME1_FIELD));
            }
            if (documentSnapshot.contains(SCORE2_FIELD)) {
                scoreToCreate.setScore2((String) documentSnapshot.get(SCORE2_FIELD));
            }
            if (documentSnapshot.contains(USER2_FIELD)) {
                scoreToCreate.setUser2((String) documentSnapshot.get(USER2_FIELD));
            }
            if (documentSnapshot.contains(TIME2_FIELD)) {
                scoreToCreate.setTime2((String) documentSnapshot.get(TIME2_FIELD));
            }
            if (documentSnapshot.contains(SCORE3_FIELD)) {
                scoreToCreate.setScore3((String) documentSnapshot.get(SCORE3_FIELD));
            }
            if (documentSnapshot.contains(USER3_FIELD)) {
                scoreToCreate.setUser3((String) documentSnapshot.get(USER3_FIELD));
            }
            if (documentSnapshot.contains(TIME3_FIELD)) {
                scoreToCreate.setTime3((String) documentSnapshot.get(TIME3_FIELD));
            }
            this.getScoreCollection().document(COLLECTION_NAME).set(scoreToCreate);
        });
    }

    // Get Score Data from Firestore
    public Task<DocumentSnapshot> getScoreData() {
        return this.getScoreCollection().document(COLLECTION_NAME).get();
    }

    public Task<Void> updateScore1(String score) {
        return this.getScoreCollection().document(COLLECTION_NAME).update(SCORE1_FIELD, score);
    }

    public Task<Void> updateUser1(String user) {
        return this.getScoreCollection().document(COLLECTION_NAME).update(USER1_FIELD, user);
    }

    public Task<Void> updateTime1(String time) {
        return this.getScoreCollection().document(COLLECTION_NAME).update(TIME1_FIELD, time);
    }

    public Task<Void> updateScore2(String score) {
        return this.getScoreCollection().document(COLLECTION_NAME).update(SCORE2_FIELD, score);
    }

    public Task<Void> updateUser2(String user) {
        return this.getScoreCollection().document(COLLECTION_NAME).update(USER2_FIELD, user);
    }

    public Task<Void> updateTime2(String time) {
        return this.getScoreCollection().document(COLLECTION_NAME).update(TIME2_FIELD, time);
    }

    public Task<Void> updateScore3(String score) {
        return this.getScoreCollection().document(COLLECTION_NAME).update(SCORE3_FIELD, score);
    }

    public Task<Void> updateUser3(String user) {
        return this.getScoreCollection().document(COLLECTION_NAME).update(USER3_FIELD, user);
    }

    public Task<Void> updateTime3(String time) {
        return this.getScoreCollection().document(COLLECTION_NAME).update(TIME3_FIELD, time);
    }

}