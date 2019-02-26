package com.hoversoftsoln.estafortdriver.profile;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.SetOptions;
import com.hoversoftsoln.estafortdriver.core.data.Driver;

import java.util.HashMap;
import java.util.Map;

public class ProfileViewModel extends ViewModel {

    private MutableLiveData<Driver> estadriver;
    private MutableLiveData<Boolean> loadingService;

    private DocumentReference driverReference;
    private ListenerRegistration userDocListenerReg;

    public ProfileViewModel() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        this.driverReference = db.collection("Drivers").document(uid);
    }

    public LiveData<Driver> getUserAccount() {
        if (estadriver == null) {
            estadriver = new MutableLiveData<>();
            if (loadingService == null) {
                loadingService = new MutableLiveData<>();
            }
            loadEstaUser();
        }
        return estadriver;
    }

    public LiveData<Boolean> loadingService() {
        if (loadingService == null) {
            loadingService = new MutableLiveData<>();
        }
        return loadingService;
    }

    private void loadEstaUser() {
        this.loadingService.postValue(true);
        this.userDocListenerReg = this.driverReference.addSnapshotListener((documentSnapshot, e) -> {
            this.loadingService.postValue(false);
            if (documentSnapshot != null) {
                Driver driver= documentSnapshot.toObject(Driver.class);
                if (driver != null) {
                    this.estadriver.postValue(driver);
                }
            }
        });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        this.userDocListenerReg.remove();
    }

    void updateUserAccount(Driver driver) {
        this.loadingService.postValue(true);
        Map<String, Object> datamap = new HashMap<>();
        datamap.put("username", driver.getUsername());
        datamap.put("email", driver.getEmail());
        datamap.put("telephone", driver.getTelephone());
        datamap.put("location", driver.getLocation());
        driverReference.set(datamap, SetOptions.merge()).addOnCompleteListener(task -> {
            this.loadingService.postValue(false);
            if (task.isSuccessful()){
                this.estadriver.postValue(driver);
            }
        });
    }

    void setStatus(int status) {
        this.loadingService.postValue(true);
        Map<String, Object> datamap = new HashMap<>();
        datamap.put("status", status);
        driverReference.set(datamap, SetOptions.merge()).addOnCompleteListener(task -> {
            this.loadingService.postValue(false);
        });
    }
}
