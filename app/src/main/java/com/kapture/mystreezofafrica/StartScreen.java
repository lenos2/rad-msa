package com.kapture.mystreezofafrica;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v4.app.FragmentActivity;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentTransaction;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.kapture.mystreezofafrica.fragments.SignInFragment;
import com.kapture.mystreezofafrica.fragments.SignUpOptionsFragment;
import com.kapture.mystreezofafrica.fragments.StartFragment;
import com.kapture.mystreezofafrica.fragments.TourDetailsFragment;
import com.kapture.mystreezofafrica.fragments.ToursFragment;
import com.kapture.mystreezofafrica.fragments.WebViewFragment;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.kapture.mystreezofafrica.pojos.User;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class StartScreen extends AppCompatActivity {

    SignInFragment sif;
    StartFragment sf;
    ToursFragment tf;
    TourDetailsFragment tdf;
    WebViewFragment wvf;
    SignUpOptionsFragment suof;
    Fragment targFrag;

    CallbackManager mCallbackManager;

    FragmentManager fm = getFragmentManager();
    FragmentTransaction fragmentTransaction;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Fresco.initialize(this);

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

        suof.setOnSignUpListener(new SignUpOptionsFragment.OnSignUpListener() {
            @Override
            public void onResult(Boolean logInState) {
                if (logInState){
                    fragmentTransaction = fm.beginTransaction();
                    fragmentTransaction.replace(R.id.start_screen,tf);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            }
        });
        sf.setOnClickSignIn(new StartFragment.StartFragmentListener() {
            @Override
            public void onClickSignIn(User user) {

                saveCredentials("user",user.getName());
                //Shows the start screen
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


    private void saveCredentials(String key,String value){
        SharedPreferences prefs = getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key,value);
        editor.commit();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (targFrag != null){
            targFrag.onActivityResult(requestCode, resultCode, data);
        }else{
            Toast.makeText(StartScreen.this,"frag = null",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);

        String fragmentName = fragment.getClass().getSimpleName();

        switch (fragmentName){
            case "SignUpOptionsFragment" :
                targFrag = fragment;
                break;
            case "StartFragment":
                targFrag = fragment;
                break;
            default:
        }
    }
}
