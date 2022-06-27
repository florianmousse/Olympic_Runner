package jeu.or.olympicrunner.manager;

import android.content.Context;
import android.util.Log;

import jeu.or.olympicrunner.model.User;
import jeu.or.olympicrunner.repository.UserRepository;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;

// Formate les données récupérées de UserRepository dans le format qui nous convient
public class UserManager {

    private static volatile UserManager instance;
    private  UserRepository userRepository;

    private UserManager() {
        userRepository = UserRepository.getInstance();
    }

    public static UserManager getInstance() {
        UserManager result = instance;
        if (result != null) {
            return result;
        }
        synchronized(UserRepository.class) {
            if (instance == null) {
                instance = new UserManager();
            }
            return instance;
        }
    }

    public FirebaseUser getCurrentUser(){
        return userRepository.getCurrentUser();
    }

    public String getCurrentUserUID(){
        return userRepository.getCurrentUserUID();
    }

    public Boolean isCurrentUserLogged(){
        return (this.getCurrentUser() != null);
    }

    public Task<Void> signOut(Context context){
        return userRepository.signOut(context);
    }

    public void createUser(){
        userRepository.createUser();
    }

    public Task<User> getUserData(){
        // Get the user from Firestore and cast it to a User model Object
        return userRepository.getUserData().continueWith(task -> task.getResult().toObject(User.class)) ;
    }

    public Task<Void> updateUsername(String username){
        return userRepository.updateUsername(username);
    }

    public Task<Void> updateScorePerso1(String scoreperso){
        return userRepository.updateScorePerso1(scoreperso);
    }

    public Task<Void> updateScorePerso2(String scoreperso){
        return userRepository.updateScorePerso2(scoreperso);
    }

    public Task<Void> updateScorePerso3(String scoreperso){
        return userRepository.updateScorePerso3(scoreperso);
    }

    public Task<Void> updateDatePerso1(String dateperso){
        return userRepository.updateDatePerso1(dateperso);
    }

    public Task<Void> updateDatePerso2(String dateperso){
        return userRepository.updateDatePerso2(dateperso);
    }

    public Task<Void> updateDatePerso3(String dateperso){
        return userRepository.updateDatePerso3(dateperso);
    }

    public Task<Void> updateTotalGameTime(String totalGameTime){
        return userRepository.updateTotalGameTime(totalGameTime);
    }

    public Task<Void> deleteUser(Context context){
        String uid = this.getCurrentUserUID();
        // Delete the user account from the Auth
        // Enlevé car pas dans le bon ordre
        /*return userRepository.deleteUser(context).addOnCompleteListener(task -> {
            // Once done, delete the user datas from Firestore
            userRepository.deleteUserFromFirestore(uid);
        });*/
        userRepository.deleteUserFromFirestore(uid);
        return userRepository.deleteUser(context);
    }

}
