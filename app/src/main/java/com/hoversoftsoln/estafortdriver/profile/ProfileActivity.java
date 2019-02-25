package com.hoversoftsoln.esta_fort.profile;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hoversoftsoln.esta_fort.R;
import com.hoversoftsoln.esta_fort.core.BaseActivity;
import com.hoversoftsoln.esta_fort.data.EstaUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

public class ProfileActivity extends BaseActivity {

    @BindView(R.id.etTelephone)
    EditText etTelephone;
    @BindView(R.id.etUsername)
    EditText etUsername;
    @BindView(R.id.etEmail)
    EditText etEmail;
    @BindView(R.id.etLocation)
    EditText etLocation;
    @BindView(R.id.saveProfile)
    Button saveProfile;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private ProfileViewModel profileViewModel;

    @BindView(R.id.editLayout)
    LinearLayout editLayout;

    @BindView(R.id.loading)
    MaterialProgressBar loader;
    private FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mFirebaseAuth = FirebaseAuth.getInstance();
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        profileViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);

        profileViewModel.loadingService().observe(this, loading -> {
            if (loading) {
                loader.setVisibility(View.VISIBLE);
            }else {
                loader.setVisibility(View.GONE);
            }
        });

        profileViewModel.getUserAccount().observe(this, user -> {
            if (user != null) {
                this.editLayout.setVisibility(View.VISIBLE);
                if (user.getUsername().trim().isEmpty()){
                    if (mFirebaseAuth.getCurrentUser() != null && mFirebaseAuth.getCurrentUser().getDisplayName() != null){
                        etUsername.setText(mFirebaseAuth.getCurrentUser().getDisplayName());
                    }
                }else {
                    etUsername.setText(user.getUsername());
                }

                if (user.getTelephone().trim().isEmpty()){
                    if (mFirebaseAuth.getCurrentUser() != null && mFirebaseAuth.getCurrentUser().getPhoneNumber() != null){
                        this.etTelephone.setText(mFirebaseAuth.getCurrentUser().getPhoneNumber());
                    }
                }else {
                    etTelephone.setText(user.getTelephone());
                }

                if (user.getEmail().trim().isEmpty()){
                    if (mFirebaseAuth.getCurrentUser() != null && mFirebaseAuth.getCurrentUser().getEmail() != null) {
                        this.etEmail.setText(mFirebaseAuth.getCurrentUser().getEmail());
                    }
                }else {
                    etEmail.setText(user.getEmail());
                }

                this.etLocation.setText(user.getLocation());
            }
        });

        this.saveProfile.setOnClickListener(v -> {
            EstaUser estaUser = new EstaUser();
            estaUser.setEmail(etEmail.getText().toString());
            estaUser.setUsername(etUsername.getText().toString());
            estaUser.setLocation(etLocation.getText().toString());
            estaUser.setTelephone(etTelephone.getText().toString());
            this.profileViewModel.updateUserAccount(estaUser);
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
