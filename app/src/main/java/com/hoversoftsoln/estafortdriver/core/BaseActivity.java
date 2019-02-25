package com.hoversoftsoln.estafortdriver.core;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.HashMap;
import java.util.Map;

public abstract class BaseActivity extends AppCompatActivity implements OnCompleteListener<InstanceIdResult> {

    private FirebaseUser user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = FirebaseAuth.getInstance().getCurrentUser();

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(this);
    }

    @Override
    public void onComplete(@NonNull Task<InstanceIdResult> task) {
        if (task.isSuccessful() && user != null) {
            sendTokentoServer(user.getUid(), task.getResult().getToken());
        }
    }

    protected void sendTokentoServer(String userID, String token) {
        Map<String, Object> datamap = new HashMap<>();
        datamap.put("token", token);
        FirebaseFirestore.getInstance().document("Users/" + userID).set(datamap, SetOptions.merge());
    }
}
