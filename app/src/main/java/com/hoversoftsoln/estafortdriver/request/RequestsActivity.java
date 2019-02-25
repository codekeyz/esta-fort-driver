package com.hoversoftsoln.estafortdriver.request;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.hoversoftsoln.estafortdriver.R;
import com.hoversoftsoln.estafortdriver.core.BaseActivity;
import com.hoversoftsoln.estafortdriver.profile.ProfileActivity;
import com.hoversoftsoln.estafortdriver.splash.SplashActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RequestsActivity extends BaseActivity {

    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;

    @BindView(R.id.recycler_requests)
    RecyclerView requestsRV;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.view_empty)
    LinearLayout emptyView;

    private RequestAdapter requestAdapter;
    private RequestsViewModel requestsViewModel;
    private AlertDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        requestsViewModel = ViewModelProviders.of(this).get(RequestsViewModel.class);

        init();

        requestsViewModel.loadingService().observe(this, loading -> refreshLayout.setRefreshing(loading));

        requestsViewModel.getRequests().observe(this, requests ->  {
            if (requests == null || requests.isEmpty()){
                emptyView.setVisibility(View.VISIBLE);
                requestsRV.setVisibility(View.GONE);
            }else {
                emptyView.setVisibility(View.GONE);
                requestsRV.setVisibility(View.VISIBLE);
                requestAdapter.setRequestList(requests);
            }
        });
    }

    private void init() {
        refreshLayout.setOnRefreshListener(() -> requestsViewModel.getRequests().observe(this, requests -> {
            if (requests == null || requests.isEmpty()){
                emptyView.setVisibility(View.VISIBLE);
                requestsRV.setVisibility(View.GONE);
            }else {
                Toast.makeText(this, requests.toArray().toString(), Toast.LENGTH_SHORT).show();
                emptyView.setVisibility(View.GONE);
                requestsRV.setVisibility(View.VISIBLE);
                requestAdapter.setRequestList(requests);
            }
        }));

        requestsRV.setHasFixedSize(true);
        requestsRV.setLayoutManager(new LinearLayoutManager(this));

        requestAdapter = new RequestAdapter();
        requestsRV.setAdapter(requestAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_profile:
                openProfile();
                break;
            case R.id.menu_sign_out:
                signout();
                break ;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openProfile() {
        Intent intent = new Intent(RequestsActivity.this, ProfileActivity.class);
        startActivity(intent);
    }

    private void signout() {
        if (dialog == null) {
            dialog =new AlertDialog.Builder(this)
                    .setTitle("Sign Out")
                    .setMessage("Are you sure you want to sign out ?")
                    .setPositiveButton("YES", (dialog, which) -> {
                        AuthUI.getInstance().signOut(RequestsActivity.this);
                        dialog.dismiss();
                        Intent iii = new Intent(RequestsActivity.this, SplashActivity.class);
                        iii.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(iii);
                    }).setNegativeButton("CANCEL", (dialog, which) -> dialog.dismiss())
                    .create();
        }
        if (!dialog.isShowing()) {
            dialog.show();
        }
    }

}
