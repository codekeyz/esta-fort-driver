package com.hoversoftsoln.esta_fort.profile;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.SetOptions;
import com.hoversoftsoln.esta_fort.data.EstaUser;

import java.util.HashMap;
import java.util.Map;

public class ProfileViewModel extends ViewModel {

    private MutableLiveData<EstaUser> estaUser;
    private MutableLiveData<Boolean> loadingService;

    private DocumentReference userRefence;
    private ListenerRegistration userDocListenerReg;

    public ProfileViewModel() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        this.userRefence = db.collection("Users").document(uid);
    }

    public LiveData<EstaUser> getUserAccount() {
        if (estaUser == null) {
            estaUser = new MutableLiveData<>();
            if (loadingService == null) {
                loadingService = new MutableLiveData<>();
            }
            loadEstaUser();
        }
        return estaUser;
    }

    public LiveData<Boolean> loadingService() {
        if (loadingService == null) {
            loadingService = new MutableLiveData<>();
        }
        return loadingService;
    }

    private void loadEstaUser() {
        this.loadingService.postValue(true);
        this.userDocListenerReg = this.userRefence.addSnapshotListener((documentSnapshot, e) -> {
            this.loadingService.postValue(false);
            if (documentSnapshot != null) {
                EstaUser user= documentSnapshot.toObject(EstaUser.class);
                if (user != null) {
                    this.estaUser.postValue(user);
                }
            }
        });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        this.userDocListenerReg.remove();
    }

    void updateUserAccount(EstaUser estaUser) {
        this.loadingService.postValue(true);
        Map<String, Object> datamap = new HashMap<>();
        datamap.put("username", estaUser.getUsername());
        datamap.put("email", estaUser.getEmail());
        datamap.put("telephone", estaUser.getTelephone());
        datamap.put("location", estaUser.getLocation());
        userRefence.set(datamap, SetOptions.merge()).addOnCompleteListener(task -> {
            this.loadingService.postValue(false);
            if (task.isSuccessful()){
                this.estaUser.postValue(estaUser);
            }
        });
    }
}
