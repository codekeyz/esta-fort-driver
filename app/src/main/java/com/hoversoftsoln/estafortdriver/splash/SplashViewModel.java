package com.hoversoftsoln.estafortdriver.splash;

import android.arch.lifecycle.ViewModel;

public class SplashViewModel extends ViewModel {

    private boolean mIsSigningIn;

    public SplashViewModel() {
        this.mIsSigningIn = false;
    }

    public boolean IsSigningIn() {
        return mIsSigningIn;
    }

    public void setIsSigningIn(boolean mIsSigningIn) {
        this.mIsSigningIn = mIsSigningIn;
    }
}
