package jeu.or.olympicrunner.repository;

import android.content.Context;
import android.util.Log;

import com.firebase.ui.auth.AuthUI;
import jeu.or.olympicrunner.model.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.reflect.Field;

// Cherche les informations de l'utilisateur dans Firebase
public final class UserRepository {

    private static final String COLLECTION_NAME = "users";
    private static final String USERNAME_FIELD = "username";
    private static final String SCOREPERSO1_FIELD = "scoreperso1";
    private static final String SCOREPERSO2_FIELD = "scoreperso2";
    private static final String SCOREPERSO3_FIELD = "scoreperso3";
    private static final String DATEPERSO1_FIELD = "dateperso1";
    private static final String DATEPERSO2_FIELD = "dateperso2";
    private static final String DATEPERSO3_FIELD = "dateperso3";
    private static final String TOTAL_GAME_TIME_FIELD = "totalGameTime";

    private static volatile UserRepository instance;

    private UserRepository() { }

    public static UserRepository getInstance() {
        UserRepository result = instance;
        if (result != null) {
            return result;
        }
        synchronized(UserRepository.class) {
            if (instance == null) {
                instance = new UserRepository();
            }
            return instance;
        }
    }

    public FirebaseUser getCurrentUser(){
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    public String getCurrentUserUID(){
        FirebaseUser user = getCurrentUser();
        return (user != null)? user.getUid() : null;
    }

    public Task<Void> signOut(Context context){
        return AuthUI.getInstance().signOut(context);
    }

    public Task<Void> deleteUser(Context context){
        return AuthUI.getInstance().delete(context);
    }


    /* FIRESTORE */

    // Get the Collection Reference
    private CollectionReference getUsersCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    // Create User in Firestore
    public void createUser() {
        FirebaseUser user = getCurrentUser();
        if (user != null){
            String urlPicture = (user.getPhotoUrl() != null) ? user.getPhotoUrl().toString() : null;
            String uid = user.getUid();
            String username = user.getDisplayName();

            User userToCreate = new User(uid, username, urlPicture, "0", "0", "0", "", "", "", "0");

            Task<DocumentSnapshot> userData = getUserData();
            // If the user already exist in Firestore, we get his data
            userData.addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.contains(USERNAME_FIELD)){
                    userToCreate.setUsername((String) documentSnapshot.get(USERNAME_FIELD));
                }

                if (documentSnapshot.contains(SCOREPERSO1_FIELD)){
                    userToCreate.setScoreperso1((String) documentSnapshot.get(SCOREPERSO1_FIELD));
                }
                if (documentSnapshot.contains(SCOREPERSO2_FIELD)){
                    userToCreate.setScoreperso2((String) documentSnapshot.get(SCOREPERSO2_FIELD));
                }
                if (documentSnapshot.contains(SCOREPERSO3_FIELD)){
                    userToCreate.setScoreperso3((String) documentSnapshot.get(SCOREPERSO3_FIELD));
                }
                if (documentSnapshot.contains(DATEPERSO1_FIELD)){
                    userToCreate.setDateperso1((String) documentSnapshot.get(DATEPERSO1_FIELD));
                }
                if (documentSnapshot.contains(DATEPERSO2_FIELD)){
                    userToCreate.setDateperso2((String) documentSnapshot.get(DATEPERSO2_FIELD));
                }
                if (documentSnapshot.contains(DATEPERSO3_FIELD)){
                    userToCreate.setDateperso3((String) documentSnapshot.get(DATEPERSO3_FIELD));
                }

                if (documentSnapshot.contains(TOTAL_GAME_TIME_FIELD)) {
                    userToCreate.setTotalGameTime((String) documentSnapshot.get(TOTAL_GAME_TIME_FIELD));
                }
                this.getUsersCollection().document(uid).set(userToCreate);
            });
        }
    }

    // Get User Data from Firestore
    public Task<DocumentSnapshot> getUserData(){
        String uid = this.getCurrentUserUID();
        if(uid != null){
            return this.getUsersCollection().document(uid).get();
        }else{
            return null;
        }
    }

    // Update User Username
    public Task<Void> updateUsername(String username) {
        String uid = this.getCurrentUserUID();
        if(uid != null){
            return this.getUsersCollection().document(uid).update(USERNAME_FIELD, username);
        }else{
            return null;
        }
    }

    // Update User Username
    public Task<Void> updateScorePerso1(String scoreperso) {
        String uid = this.getCurrentUserUID();
        if(uid != null){
            return this.getUsersCollection().document(uid).update(SCOREPERSO1_FIELD, scoreperso);
        }else{
            return null;
        }
    }

    // Update User Username
    public Task<Void> updateScorePerso2(String scoreperso) {
        String uid = this.getCurrentUserUID();
        if(uid != null){
            return this.getUsersCollection().document(uid).update(SCOREPERSO2_FIELD, scoreperso);
        }else{
            return null;
        }
    }

    // Update User Username
    public Task<Void> updateScorePerso3(String scoreperso) {
        String uid = this.getCurrentUserUID();
        if(uid != null){
            return this.getUsersCollection().document(uid).update(SCOREPERSO3_FIELD, scoreperso);
        }else{
            return null;
        }
    }


    // Update User Username
    public Task<Void> updateDatePerso1(String dateperso) {
        String uid = this.getCurrentUserUID();
        if(uid != null){
            return this.getUsersCollection().document(uid).update(DATEPERSO1_FIELD, dateperso);
        }else{
            return null;
        }
    }

    // Update User Username
    public Task<Void> updateDatePerso2(String dateperso) {
        String uid = this.getCurrentUserUID();
        if(uid != null){
            return this.getUsersCollection().document(uid).update(DATEPERSO2_FIELD, dateperso);
        }else{
            return null;
        }
    }

    // Update User Username
    public Task<Void> updateDatePerso3(String dateperso) {
        String uid = this.getCurrentUserUID();
        if(uid != null){
            return this.getUsersCollection().document(uid).update(DATEPERSO3_FIELD, dateperso);
        }else{
            return null;
        }
    }


    public Task<Void> updateTotalGameTime(String totalGameTime) {
        String uid = this.getCurrentUserUID();
        if(uid != null){
            return this.getUsersCollection().document(uid).update(TOTAL_GAME_TIME_FIELD, totalGameTime);
        }else{
            return null;
        }
    }

    // Delete the User from Firestore
    public void deleteUserFromFirestore(String uid) {
        if (uid != null){
            this.getUsersCollection().document(uid).delete();
        }
    }

}
