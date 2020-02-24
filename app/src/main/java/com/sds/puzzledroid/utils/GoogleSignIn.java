package com.sds.puzzledroid.utils;

import android.content.Context;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

public class GoogleSignIn {

    private static GoogleSignInOptions signInOptions;
    private static GoogleSignInClient signInClient;

    public static void configGoogleSignIn(Context context) {
        if(signInOptions == null) {
            // Configure Google Sign In
            signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken("306601866279-2uv278reiucvvb3trfopffc16n575jt5.apps.googleusercontent.com")
                    .requestEmail()
                    .build();
        }

        if(signInClient == null) {
            signInClient = com.google.android.gms.auth.api.signin.GoogleSignIn.getClient(context, signInOptions);
        }
    }

    public static GoogleSignInClient getSignInClient() {
        return signInClient;
    }

}
