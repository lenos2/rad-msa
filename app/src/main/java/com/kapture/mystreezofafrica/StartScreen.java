package com.kapture.mystreezofafrica;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.kapture.mystreezofafrica.fragments.SignInFragment;
import com.kapture.mystreezofafrica.fragments.SignUpOptionsFragment;
import com.kapture.mystreezofafrica.fragments.StartFragment;
import com.kapture.mystreezofafrica.fragments.TourDetailsFragment;
import com.kapture.mystreezofafrica.fragments.ToursFragment;
import com.kapture.mystreezofafrica.fragments.WebViewFragment;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class StartScreen extends AppCompatActivity {

    SignInFragment sif;
    StartFragment sf;
    ToursFragment tf;
    TourDetailsFragment tdf;
    WebViewFragment wvf;
    SignUpOptionsFragment suof;

    FragmentManager fm = getFragmentManager();
    FragmentTransaction fragmentTransaction;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_start_screen);


        //Code to get hash key
        // Add code to print out the key hash
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.kapture.mystreezofafrica",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
                //Toast.makeText(StartScreen.this, Base64.encodeToString(md.digest(), Base64.DEFAULT),Toast.LENGTH_LONG).show();
            }
        } catch (PackageManager.NameNotFoundException e) {


        } catch (NoSuchAlgorithmException e) {

        }


        sif = new SignInFragment();
        sf = new StartFragment();
        tf = new ToursFragment();
        tdf = new TourDetailsFragment();
        wvf = new WebViewFragment();
        suof = new SignUpOptionsFragment();

        sf.setOnClickSignIn(new StartFragment.StartFragmentListener() {
            @Override
            public void onClickSignIn() {

                //Shows the sign in Screen
                fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.start_screen,tf);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }

            @Override
            public void onClickSignUp() {

                //SHows the Sign Up Options Screen
                fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.start_screen,suof);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });

        tf.setDetailsListener(new ToursFragment.DetailsListener() {
            @Override
            public void onClick() {

                //Shows the Event Details
                fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.start_screen,tdf);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.start_screen, sf);
        fragmentTransaction.commit();

    }


    @Override
    public void onBackPressed() {
        if (fm.getBackStackEntryCount()!=0) {
            fm.popBackStack();
            //invalidateOptionsMenu();
        } else {
            super.onBackPressed();
        }
    }




}
