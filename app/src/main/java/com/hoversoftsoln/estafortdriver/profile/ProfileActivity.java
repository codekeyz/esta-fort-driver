package com.hoversoftsoln.estafortdriver.profile;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.glomadrian.materialanimatedswitch.MaterialAnimatedSwitch;
import com.google.firebase.auth.FirebaseAuth;
import com.hoversoftsoln.estafortdriver.R;
import com.hoversoftsoln.estafortdriver.core.BaseActivity;
import com.hoversoftsoln.estafortdriver.core.data.Driver;
import com.hoversoftsoln.estafortdriver.core.data.EstaUser;


import butterknife.BindView;
import butterknife.ButterKnife;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

public class ProfileActivity extends BaseActivity implements MaterialAnimatedSwitch.OnCheckedChangeListener {

    @BindView(R.id.etTelephone)
    EditText etTelephone;
    @BindView(R.id.etUsername)
    EditText etUsername;
    @BindView(R.id.etEmail)
    EditText etEmail;
    @BindView(R.id.etLocation)
    EditText etLocation;
    @BindView(R.id.status)
    TextView status;
    @BindView(R.id.pin)
    MaterialAnimatedSwitch statusSwitch;
    @BindView(R.id.saveProfile)
    Button saveProfile;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private ProfileViewModel profileViewModel;

    @BindView(R.id.editLayout)
    LinearLayout editLayout;

    @BindView(R.id.layoutTwoTwoTT)
    LinearLayout statusLayout;

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

        statusSwitch.setOnCheckedChangeListener(this);

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

                if (!isEmpty(user)) {
                    this.statusLayout.setVisibility(View.VISIBLE);
                }else {
                    this.statusLayout.setVisibility(View.GONE);
                }

                if (user.getStatus() == 0) {
                    if (statusSwitch.isChecked()){
                        statusSwitch.toggle();
                    }
                }else {
                    if (!statusSwitch.isChecked()){
                        statusSwitch.toggle();
                    }
                }

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
            } else {
                statusLayout.setVisibility(View.GONE);
            }
        });

        this.saveProfile.setOnClickListener(v -> {
            Driver estaUser = new Driver();
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

    @Override
    public void onCheckedChanged(boolean b) {
        int status = b ? 1 : 0;
        this.profileViewModel.setStatus(status);
    }

    private boolean isEmpty(Driver estaDriver) {
        if (estaDriver == null) {
            return true;
        } else return estaDriver.getUsername().trim().isEmpty() ||
                estaDriver.getEmail().trim().isEmpty() ||
                estaDriver.getTelephone().trim().isEmpty() ||
                estaDriver.getLocation().trim().isEmpty();
    }
}
