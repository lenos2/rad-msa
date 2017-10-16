package com.kapture.mystreezofafrica.fragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kapture.mystreezofafrica.R;
import com.kapture.mystreezofafrica.pojos.User;
import com.transitionseverywhere.Fade;
import com.transitionseverywhere.TransitionManager;
import com.transitionseverywhere.TransitionSet;
import com.transitionseverywhere.extra.Scale;

import static com.facebook.internal.FacebookDialogFragment.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class StartFragment extends Fragment {


    ViewGroup transitionsContainer;
    View items1,items2;
    Boolean visible;

    EditText etEmail,etPassword;

    TextView tvSignUp;
    Button btnSignIn;
    Button btnEnter;
    static View v;

    ProgressDialog progressDialog;

    CallbackManager mCallbackManager;
    LoginManager loginManager;
    private FirebaseAuth mAuth;


    public StartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_start, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        v = view;
        transitionsContainer = (ViewGroup)v.findViewById(R.id.transition_container);

        tvSignUp = (TextView) v.findViewById(R.id.tvSignUp);
        btnSignIn = (Button)v.findViewById(R.id.btnSignIn);
        btnEnter = (Button)v.findViewById(R.id.btnEnter);
        etEmail = (EditText)v.findViewById(R.id.email);
        etPassword = (EditText)v.findViewById(R.id.password);

        items1 = transitionsContainer.findViewById(R.id.startitems);
        items2 = transitionsContainer.findViewById(R.id.signinitems);
        visible = true;
        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClickSignUp();
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //listener.onClickSignIn();



                visible = !visible;

                TransitionSet set = new TransitionSet()
                        .addTransition(new Scale(0.2f))
                        .addTransition(new Fade())
                        .setInterpolator(visible ? new LinearOutSlowInInterpolator() :
                                new FastOutLinearInInterpolator());

                TransitionManager.beginDelayedTransition(transitionsContainer, set);
                items1.setVisibility(visible ? View.VISIBLE : View.GONE);
                items2.setVisibility(visible ? View.GONE : View.VISIBLE);
            }
        });

        btnEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Login User
                String email,password;

                email = etEmail.getText().toString().trim();
                password = etPassword.getText().toString();

                if (email.equals("") || password.equals("")){
                    Toast.makeText(v.getContext(),"Please fill in all required information",Toast.LENGTH_LONG).show();
                    return;
                }
                mLogin(email,password);

            }
        });



        // Initialize Facebook Login button
        mCallbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = (LoginButton) v.findViewById(R.id.facebook_login);
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.setFragment(this);

        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
                Toast.makeText(v.getContext(),"Log in cancelled",Toast.LENGTH_SHORT).show();
                // ...
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);

                // ...
            }
        });
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        progressDialog = ProgressDialog.show(v.getContext(),null,"Logging in...");
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            progressDialog.dismiss();
                            Toast.makeText(v.getContext(),"You are now logged in",Toast.LENGTH_SHORT).show();

                            FirebaseDatabase.getInstance().getReference("users/"+user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    //User user = dataSnapshot.getValue(User.class);
                                    //Toast.makeText(v.getContext(),"Welcome " + user.getName(),Toast.LENGTH_SHORT).show();
                                    User.Builder builder = new User.Builder();
                                    User user = builder.withName("Test").build();
                                    listener.onClickSignIn();
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    Toast.makeText(v.getContext(),"An Error has Occured : " + databaseError.getDetails(),Toast.LENGTH_SHORT).show();
                                }
                            });
                            //updateUI(user);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            progressDialog.dismiss();
                            Toast.makeText(v.getContext(), "Authentication failed."  + task.getException().getLocalizedMessage(),
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                            //loginManager.logOut();
                            //listener.onResult(false);
                        }

                    }
                });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }
    private void mLogin(String email, String password){
        doAuthentication(email,password);
    }

    private void doAuthentication(String email, String password){
        progressDialog = ProgressDialog.show(v.getContext(),null,"Please wait...");

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(v.getContext(),e.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
            }
        }).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                if (task.isSuccessful()){
                    Toast.makeText(v.getContext(),"Authentication Failed : " + task.getException().getLocalizedMessage(),Toast.LENGTH_SHORT).show();

                }else {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    FirebaseDatabase.getInstance().getReference("users/"+user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            User user = dataSnapshot.getValue(User.class);
                            Toast.makeText(v.getContext(),"Welcome " + user.getName(),Toast.LENGTH_SHORT).show();
                            listener.onClickSignIn();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Toast.makeText(v.getContext(),"An Error has Occured : " + databaseError.getDetails(),Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }



    public interface StartFragmentListener{
        void onClickSignIn();
        void onClickSignUp();
    }

    private StartFragmentListener listener;
    public void setOnClickSignIn(StartFragmentListener listener){
        this.listener = listener;
    }
    public void setOnClickSignUp(StartFragmentListener listener){
        this.listener = listener;
    }
}
