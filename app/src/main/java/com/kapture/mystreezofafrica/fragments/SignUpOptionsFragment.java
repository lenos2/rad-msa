package com.kapture.mystreezofafrica.fragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.api.BooleanResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kapture.mystreezofafrica.R;
import com.kapture.mystreezofafrica.pojos.User;

import static com.facebook.internal.FacebookDialogFragment.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpOptionsFragment extends Fragment {


    static CallbackManager mCallbackManager;
    private FirebaseAuth mAuth;
    LoginManager loginManager;
    EditText etEmail,etPassword,etReenterPassword,etUserName;
    Button btnRegister;
    ProgressDialog progressDialog;

    View v;

    public SignUpOptionsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_sign_up_options, container, false);



        // Initialize Facebook Login button
        mCallbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = (LoginButton) v.findViewById(R.id.facebook_login);
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.setFragment(SignUpOptionsFragment.this);

        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                //Toast.makeText(v.getContext(),"Zvaita",Toast.LENGTH_SHORT).show();
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
                Toast.makeText(v.getContext(),"Something Went Wrong",Toast.LENGTH_SHORT).show();
                // ...
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);

                // ...
            }
        });

        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etEmail = (EditText)v.findViewById(R.id.etEmail);
        etPassword = (EditText)v.findViewById(R.id.etPassword);
        etReenterPassword = (EditText)v.findViewById(R.id.etReenterPassword);
        etUserName = (EditText)v.findViewById(R.id.etUserName);

        btnRegister = (Button) v.findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = etEmail.getText().toString().trim(),
                        password = etPassword.getText().toString(),
                        reenterPassword = etReenterPassword.getText().toString(),
                        username = etUserName.getText().toString().trim();

                if (email.equals("") || password.equals("") || reenterPassword.equals("") || username.equals("")){
                    Toast.makeText(v.getContext(),"Please Fill in all Fields",Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!password.equals(reenterPassword)){
                    Toast.makeText(v.getContext(),"Passwords do not match. Please enter correct Password",Toast.LENGTH_SHORT).show();
                    etPassword.setText("");
                    etReenterPassword.setText("");
                    return;
                }

                mAuth = FirebaseAuth.getInstance();

                progressDialog = ProgressDialog.show(v.getContext(),null,"Signing Up...");
                mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        User mUser = new User();
                        User.Builder userBuilder = new User.Builder();
                        userBuilder
                                .withEmail(email)
                                .withName(username);

                        mUser = userBuilder.build();

                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users/");
                        ref.child(user.getUid()).setValue(mUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                progressDialog.dismiss();
                                Toast.makeText(v.getContext(),"Log In successful.",Toast.LENGTH_SHORT).show();
                                //switch to tours fragment
                                listener.onResult(true);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(v.getContext(), "Something went wrong. Error : " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(v.getContext(), "Log In failed. Error : " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });



    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();


                            //updateUI(user);

                            //switch to tours fragment
                            listener.onResult(true);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(v.getContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                            //loginManager.logOut();
                            listener.onResult(false);
                        }

                        // ...
                    }
                });
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public interface OnSignUpListener{
        void onResult(Boolean logInState);
    }

    private OnSignUpListener listener;
    public void setOnSignUpListener(OnSignUpListener listener){
        this.listener = listener;
    }

}
