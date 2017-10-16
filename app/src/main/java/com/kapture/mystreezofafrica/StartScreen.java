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
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentTransaction;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.firebase.auth.FirebaseAuth;
import com.kapture.mystreezofafrica.fragments.AboutUsFragment;
import com.kapture.mystreezofafrica.fragments.AllToursFragment;
import com.kapture.mystreezofafrica.fragments.ComingSoonFragment;
import com.kapture.mystreezofafrica.fragments.ContactUsFragment;
import com.kapture.mystreezofafrica.fragments.PackageDetailsFragment;
import com.kapture.mystreezofafrica.fragments.PackagesFragment;
import com.kapture.mystreezofafrica.fragments.SignUpOptionsFragment;
import com.kapture.mystreezofafrica.fragments.StartFragment;
import com.kapture.mystreezofafrica.fragments.TourDetailsFragment;
import com.kapture.mystreezofafrica.fragments.ToursFragment;
import com.kapture.mystreezofafrica.fragments.WebViewFragment;

import com.kapture.mystreezofafrica.pojos.TourType;
import com.transitionseverywhere.Slide;
import com.transitionseverywhere.TransitionManager;
import com.transitionseverywhere.TransitionSet;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class StartScreen extends AppCompatActivity {

    //Fragments Used in the application
    //SignInFragment sif;
    StartFragment sf;
    ToursFragment tf;
    TourDetailsFragment tdf;
    WebViewFragment wvf;
    SignUpOptionsFragment suof;
    AllToursFragment atf;
    Fragment targFrag;
    ContactUsFragment cuf;
    AboutUsFragment auf;
    PackagesFragment pf;
    ComingSoonFragment csf;
    PackageDetailsFragment pdf;

    //Transition Values
    ViewGroup vgMenu;
    View vMenu;
    ImageView ivMenuOpener;
    static boolean menuVisible;
    TransitionSet set;

    //Menu Buttons
    Button btnHome,
    btnTours,
    btnAboutUs,
    btnContactUs,
    btnLogOut;

    //Facebook Callback manager
    CallbackManager callbackManager;

    //Fragment Manager and Transaction
    FragmentManager fm = getFragmentManager();
    FragmentTransaction fragmentTransaction;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Fresco.initialize(this);

        setContentView(R.layout.activity_start_screen);

        /*FacebookSdk.sdkInitialize(this.getApplicationContext());
        callbackManager = CallbackManager.Factory.create();*/




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

        /**
         * Setting Up the menu buttons
         * */
        vgMenu = (ViewGroup)findViewById(R.id.llmenu);
        vMenu = vgMenu.findViewById(R.id.includeMenu);
        ivMenuOpener = (ImageView)vgMenu.findViewById(R.id.ivMenu);

        //Setup the transition for the Menu
        menuVisible = false;
        set = new TransitionSet()
                .addTransition(new Slide(Gravity.BOTTOM))
                .setInterpolator(menuVisible ? new LinearOutSlowInInterpolator() :
                        new FastOutLinearInInterpolator());


        ivMenuOpener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Hide or show the menu
                menuVisible = !menuVisible;

                TransitionManager.beginDelayedTransition(vgMenu, set);
                vMenu.setVisibility(menuVisible ? View.VISIBLE : View.GONE);
                ivMenuOpener.setImageResource(menuVisible ? R.mipmap.ic_down : R.mipmap.ic_up);
            }
        });

        btnHome = (Button)findViewById(R.id.btnHome);
        btnTours = (Button)findViewById(R.id.btnTours);
        btnAboutUs = (Button)findViewById(R.id.btnAboutUs);
        btnContactUs = (Button)findViewById(R.id.btnContactUs);
        btnLogOut = (Button)findViewById(R.id.btnLogOut);

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Shows the start fragment
                fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.start_screen,tf);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

                //Hide The Menu Bar
                menuVisible = !menuVisible;
                TransitionManager.beginDelayedTransition(vgMenu, set);
                vMenu.setVisibility(menuVisible ? View.VISIBLE : View.GONE);
                ivMenuOpener.setImageResource(menuVisible ? R.mipmap.ic_down : R.mipmap.ic_up);
            }
        });

        btnTours.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Opens Tours fragment
                fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.start_screen,atf);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

                //Hide The Menu Bar
                menuVisible = !menuVisible;
                TransitionManager.beginDelayedTransition(vgMenu, set);
                vMenu.setVisibility(menuVisible ? View.VISIBLE : View.GONE);
                ivMenuOpener.setImageResource(menuVisible ? R.mipmap.ic_down : R.mipmap.ic_up);
            }
        });

        btnAboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Opens About Us Fragment
                fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.start_screen,auf);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

                //Hide The Menu Bar
                menuVisible = !menuVisible;
                TransitionManager.beginDelayedTransition(vgMenu, set);
                vMenu.setVisibility(menuVisible ? View.VISIBLE : View.GONE);
                ivMenuOpener.setImageResource(menuVisible ? R.mipmap.ic_down : R.mipmap.ic_up);
            }
        });

        btnContactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Opens Contact Us Fragment
                fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.start_screen,cuf);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

                //Hide The Menu Bar
                menuVisible = !menuVisible;
                TransitionManager.beginDelayedTransition(vgMenu, set);
                vMenu.setVisibility(menuVisible ? View.VISIBLE : View.GONE);
                ivMenuOpener.setImageResource(menuVisible ? R.mipmap.ic_down : R.mipmap.ic_up);
            }
        });

        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Log out of the application

                //sign out of firebase
                FirebaseAuth.getInstance().signOut();
                fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.start_screen,sf);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                //sign out of facebook
                /**TODO: Fix the Facebook logout button*/
                //LoginManager.getInstance().logOut();

                //Hide The Menu Bar
                menuVisible = !menuVisible;
                TransitionManager.beginDelayedTransition(vgMenu, set);
                vMenu.setVisibility(menuVisible ? View.VISIBLE : View.GONE);
                ivMenuOpener.setImageResource(menuVisible ? R.mipmap.ic_down : R.mipmap.ic_up);
            }
        });

        /**
         * Setting Up the Fragments
         * */

        //sif = new SignInFragment();
        sf = new StartFragment();
        tf = new ToursFragment();

        //tdf = new TourDetailsFragment();
        wvf = new WebViewFragment();
        suof = new SignUpOptionsFragment();

        atf = new AllToursFragment();
        cuf = new ContactUsFragment();
        auf = new AboutUsFragment();

        atf.setTourpackageListener(new AllToursFragment.TourpackageListener() {
            @Override
            public void onPackageClick(TourType type) {
                //Display the next List of Packages of a specific type
                //Shows the Event Details
                pf = setPf(type);

                fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.start_screen,pf);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });
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
            public void onClickSignIn() {

                //saveCredentials("user",user.getName());
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
            public void onClick(String tour) {

                if (tour.equals("")){
                    csf = new ComingSoonFragment();

                    fragmentTransaction = fm.beginTransaction();
                    fragmentTransaction.replace(R.id.start_screen,csf);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }else{
                    tdf = setTdf(tour);
                    fragmentTransaction = fm.beginTransaction();
                    fragmentTransaction.replace(R.id.start_screen,tdf);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }

            }
        });

        isLoggedIn();
//        FragmentTransaction fragmentTransaction = fm.beginTransaction();
//        fragmentTransaction.replace(R.id.start_screen, sf);
//        fragmentTransaction.commit();
    }

    private PackageDetailsFragment setPdf(String packageName){
        PackageDetailsFragment frag = new PackageDetailsFragment();
        Bundle bundle  = new Bundle();
        bundle.putString("packageName", packageName);

        frag.setArguments(bundle);
        return frag;
    }

    private PackagesFragment setPf(TourType tourType){
        PackagesFragment frag = new PackagesFragment();
        Bundle bundle  = new Bundle();
        bundle.putString("type", tourType.getName());

        frag.setArguments(bundle);

        frag.setOnPackageSelectListener(new PackagesFragment.OnPackageSelectListener() {
            @Override
            public void onClick(String tour) {
                tdf = setTdf(tour);

                fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.start_screen,tdf);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });


        return frag;
    }
    private TourDetailsFragment setTdf(String tourName){

        TourDetailsFragment frag = new TourDetailsFragment();
        Bundle bundle  = new Bundle();
        bundle.putString("tour",tourName);
        frag.setOnPackageSelectListener(new TourDetailsFragment.OnPackageSelectListener() {
            @Override
            public void onSelect(String packageName) {
                //csf = new ComingSoonFragment();
                pdf = setPdf(packageName);
                fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.start_screen,pdf);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        frag.setArguments(bundle);

        return frag;
    }
    public void isLoggedIn() {

        /**
         * Check if the application is Logged in to firebase servers
         * */

        if (FirebaseAuth.getInstance().getCurrentUser() != null){
            //tf = new ToursFragment();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.replace(R.id.start_screen, tf);
            //fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else {
            //sf = new StartFragment();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.replace(R.id.start_screen, sf);
            //fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }

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
            case "StartFragment" :
                targFrag = fragment;
                ivMenuOpener.setVisibility(View.GONE);
                break;

            case "SignUpOptionsFragment":
                targFrag = fragment;
                ivMenuOpener.setVisibility(View.GONE);
                break;
            default:
                ivMenuOpener.setVisibility(View.VISIBLE);
        }
    }
}
