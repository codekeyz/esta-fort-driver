package com.hoversoftsoln.estafortdriver.request;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.hoversoftsoln.estafortdriver.core.data.Request;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequestsViewModel extends ViewModel {

    private MutableLiveData<List<Request>> requests;
    private MutableLiveData<Boolean> loadingService;
    private Query requestsQuery;
    private CollectionReference requesCollection;
    private ListenerRegistration requestsRegistration;

    public RequestsViewModel() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        this.requestsQuery = db.collection("Requests").whereEqualTo("driverID", user.getUid());
        this.requesCollection = db.collection("Requests");
    }

    LiveData<List<Request>> getRequests() {
        if (requests == null) {
            requests = new MutableLiveData<>();
            if (loadingService == null) {
                loadingService = new MutableLiveData<>();
            }
            loadRequests();
        }
        return requests;
    }

    LiveData<Boolean> loadingService() {
        if (loadingService == null) {
            loadingService = new MutableLiveData<>();
        }
        return loadingService;
    }

    private void loadRequests() {
        this.loadingService.postValue(true);
        List<Request> requestList = new ArrayList<>();
        this.requestsRegistration = this.requestsQuery.addSnapshotListener((qdocs, e) -> {
            this.loadingService.postValue(false);
            if (qdocs != null) {
                requestList.clear();
                for (DocumentSnapshot d :
                        qdocs.getDocuments()) {
                    Request request = d.toObject(Request.class);
                    if (request != null) {
                        request.setId(d.getId());
                        requestList.add(request);
                    }
                }
                this.requests.postValue(requestList);
            }
        });
    }

    @Override
    protected void onCleared() {
        if (this.requestsRegistration != null) {
            this.requestsRegistration.remove();
        }
    }

    void updateRequest(String id, int status) {
        Map<String, Object> data  = new HashMap<>();
        data.put("status", status);
        if (status == 3){
            data.put("is_completed", true);
        }
        if (status == -1){
            data.put("is_cancelled", true);
        }
        requesCollection.document(id).update(data);
    }
}
