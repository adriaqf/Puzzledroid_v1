package com.sds.puzzledroid.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.sds.puzzledroid.R;
import com.sds.puzzledroid.utils.FBFirestore;
import com.sds.puzzledroid.utils.GoogleSignIn;
import com.sds.puzzledroid.utils.Permissions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LogInActivity extends AppCompatActivity {

    public static FirebaseAuth auth;
    private static int RC_SIGN_IN = 10001;
    private TextView tvGPS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        auth = FirebaseAuth.getInstance();
        GoogleSignIn.configGoogleSignIn(this);


        tvGPS = findViewById(R.id.tvGPS);

        Permissions permissions = new Permissions(this);
        permissions.verifyPermissions();

       if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1000);
        } else {
        LocationManager locationManager = (LocationManager) getSystemService((Context.LOCATION_SERVICE));
        Location location = locationManager.getLastKnownLocation((LocationManager.GPS_PROVIDER));
        double longitude = location.getLongitude();
        double latitude = location.getLatitude();
            try {
                Geocoder gcd = new Geocoder(this, Locale.getDefault());
                List<Address> addresses = gcd.getFromLocation(latitude, longitude, 1);
                if (addresses.size() > 0) {
                    System.out.println(addresses.get(0).getLocality());
                }
              tvGPS.setText(addresses.get(0).getCountryName());
            } catch (Exception e){
                System.out.println(e.getMessage());
                Toast.makeText(this, "Not 55found!", Toast.LENGTH_SHORT).show();
            }

        }
        //String all = getResources().getConfiguration().locale.getCountry();
        //tvGPS.setText(all);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode){
            case 1000:
                if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    LocationManager locationManager = (LocationManager) getSystemService((Context.LOCATION_SERVICE));
                    Location location = locationManager.getLastKnownLocation((LocationManager.GPS_PROVIDER));
                    try {
                        double longitude = location.getLongitude();
                        double latitude = location.getLatitude();
                        Geocoder gcd = new Geocoder(this, Locale.getDefault());
                        List<Address> addresses = gcd.getFromLocation(latitude, longitude, 1);
                        if (addresses.size() > 0) {
                            System.out.println(addresses.get(0).getLocality());
                        }
                        tvGPS.setText(addresses.get(0).getCountryName());
                    }catch (Exception e){
                        e.printStackTrace();
                        Toast.makeText(this, "Not found!", Toast.LENGTH_SHORT).show();
                    } }else{
                    Toast.makeText(this, "Permission not granted", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private String hereLocation(double lat, double lon) {
        String cityName = "";
        String algo = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> adresses;
        try {
            adresses = geocoder.getFromLocation(lat, lon, 10);
            if(adresses.size()>0){
                for (Address adr: adresses){
                    if(adr.getLocality()!= null && adr.getLocality().length()>0){
                        cityName = adr.getLocality();
                        algo = adr.getCountryName();
                        break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();

        }
        return cityName;
    }



    public void onClickLogIn(View view) {
        signIn();
    }

    private void signIn() {
        Intent signInIntent = GoogleSignIn.getSignInClient().getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = com.google.android.gms.auth.api.signin.GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("[EXCEPTION]", "Google sign in failed", e);
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d("[USER]", "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("[SUCCESS]", "signInWithCredential:success");
                            // Prepares user DB collection (if it's not there yet)
                            FBFirestore fb = new FBFirestore();
                            fb.prepareUserDoc();
                            // Starts MainActivity
                            Intent i = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(i);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("[FAILURE]", "signInWithCredential:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Error al iniciar sesión", Toast.LENGTH_LONG).show();
                        }

                        // ...
                    }
                });
    }

}
