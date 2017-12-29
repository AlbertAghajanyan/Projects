package com.instigatemobile.imessenger.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.instigatemobile.imessenger.R;
import com.instigatemobile.imessenger.activities.MainActivity;
import com.instigatemobile.imessenger.data.SharedPreferenceHelper;
import com.instigatemobile.imessenger.data.StaticConfig;
import com.instigatemobile.imessenger.models.User;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class LoginFragment extends Fragment {

    private AuthUtils mAuthUtils;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser user;
    private boolean firstTimeAccess;

    private View view;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private ProgressBar bar;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_login, container, false);
        bar = (ProgressBar) view.findViewById(R.id.progressBar);

        mAuth = FirebaseAuth.getInstance();
        mAuthUtils = new AuthUtils();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    StaticConfig.UID = user.getUid();
                    if (firstTimeAccess) {
                        startActivity(new Intent(getActivity(), MainActivity.class));
                        getActivity().finish();
                    }
                }
                firstTimeAccess = false;
            }
        };
        mAuth.addAuthStateListener(mAuthListener);

        editTextEmail = (EditText) view.findViewById(R.id.input_email);
        editTextPassword = (EditText) view.findViewById(R.id.input_password);
        Button login = (Button) view.findViewById(R.id.btn_login);
        TextView register = (TextView) view.findViewById(R.id.link_signup);
        TextView forgot = (TextView) view.findViewById(R.id.link_forgot_pass);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickLogin(view);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goRegisterPage();
            }
        });

        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goForgotPage();
            }
        });
        return view;
    }

    public void clickLogin(View view) {
        String username = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();
        if (validateEmail() && validatePassword()) {
            mAuthUtils.signIn(username, password);
        }
    }

    public boolean validateEmail() {
        String email = editTextEmail.getText().toString().trim();
        final String EMAIL_PATTERN = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (editTextEmail.getText().toString().equals("")) {
            editTextEmail.setError("You should specify the email");
            return false;
        }
        if (!email.matches(EMAIL_PATTERN)) {
            editTextEmail.setError("The specified email is not correctly formated");
            return false;
        }
        return true;
    }

    public boolean validatePassword() {
        String password = editTextPassword.getText().toString().trim();
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])[a-zA-Z0-9!@#$%^&*]{6,20}$";

        if ((editTextPassword.getText().toString().equals("")) || (editTextPassword.getText().length() < 6)) {
            editTextPassword.setError("You should specify the password");
            return false;
        }
        /*
        if (!password.matches(PASSWORD_PATTERN)) {
            editTextPassword.setError("The specified password is not correctly formated (min lenght 6 symbole, at least one symbole and on capital latter and one number)");
            return false;
        }*/
        return true;
    }

    private boolean validate(String emailStr, String password) {
        final Pattern VALID_EMAIL_ADDRESS_REGEX =
                Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return (password.length() > 0 || password.equals(";")) && matcher.find();
    }

    private void goForgotPage() {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        ForgotPasswordFragment forgotFragment = new ForgotPasswordFragment();
        forgotFragment.setAuthUtils(mAuthUtils);
        fragmentTransaction.replace(R.id.fragmentContainer, forgotFragment);
        fragmentTransaction.addToBackStack("Forgot");
        fragmentTransaction.commit();
    }

    private void goRegisterPage() {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        RegisterFragment registerFragment = new RegisterFragment();
        registerFragment.setAuthUtils(mAuthUtils);
        fragmentTransaction.replace(R.id.fragmentContainer, registerFragment);
        fragmentTransaction.addToBackStack("Register");
        fragmentTransaction.commit();
    }

    public void goLoginPage() {
        LoginFragment loginFragment = new LoginFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer, loginFragment);
        fragmentTransaction.addToBackStack("Login");
        fragmentTransaction.commit();
    }

    public void redirect() {
        Intent redirect = new Intent(getActivity().getApplicationContext(), MainActivity.class);
        getActivity().startActivity(redirect);
        getActivity().finish();
    }

    public void progressBarVisibility() {
        bar.setVisibility(ProgressBar.VISIBLE);
    }

    public void progressBarInvisibility() {
        bar.setVisibility(ProgressBar.INVISIBLE);
    }

    class AuthUtils {
        private RegisterFragment mRegisterFragment;

        void createUser(final String username, final String email, String password, RegisterFragment registerFragment) {
            mRegisterFragment = registerFragment;
            progressBarVisibility();
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(mRegisterFragment.getActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressBarInvisibility();
                            if (!task.isSuccessful()) {
                                Toast.makeText(getActivity(), "Register an account failed", Toast.LENGTH_SHORT).show();
                            } else {
                                initNewUserInfo(username, email);
                                Toast.makeText(getActivity(), "Register and Login success", Toast.LENGTH_SHORT).show();
                                FirebaseUser user = mAuth.getCurrentUser();
                                sendEmailVerification(mRegisterFragment.getActivity());
                                //startActivity(new Intent(getActivity(), LoginRegisterActivity.class));
                                //getActivity().finish();
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressBarInvisibility();
                        }
                    })
            ;
        }

        private void sendEmailVerification(Activity activity) {
            final FirebaseUser user = mAuth.getCurrentUser();
            user.getDisplayName();
            user.sendEmailVerification().addOnCompleteListener(activity, new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        goLoginPage();
                    } else {
                        showMessage(mRegisterFragment.getActivity(), "Authentication failed.");
                    }

                }
            });
        }

        private void showMessage(Activity activity, String message) {
            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
        }

        void signIn(String email, String password) {
            progressBarVisibility();
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressBarInvisibility();
                            if (!task.isSuccessful()) {
                                Toast.makeText(getActivity(), "Incorrect email or password. Authentication failed", Toast.LENGTH_LONG).show();
                            } else {
                                if (user.isEmailVerified()) {
                                    FirebaseMessaging.getInstance().subscribeToTopic(mAuth.getCurrentUser().getUid());
                                    System.out.println(mAuth.getCurrentUser().getUid());
                                    saveUserInfo();
                                    redirect();
                                }else {
                                    Toast.makeText(getActivity(), "Email verification failed", Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressBarInvisibility();
                        }
                    });
        }

        void saveUserInfo() {
            FirebaseDatabase.getInstance().getReference().child("user/" + StaticConfig.UID).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    progressBarInvisibility();
                    HashMap hashUser = (HashMap) dataSnapshot.getValue();
                    if (hashUser == null) {
                        return;
                    }
                    User userInfo = new User();
                    userInfo.name = (String) hashUser.get("name");
                    userInfo.email = (String) hashUser.get("email");
                    userInfo.avata = (String) hashUser.get("avata");
                    userInfo.background = (String) hashUser.get("background");
                    SharedPreferenceHelper.getInstance(getActivity()).saveUserInfo(userInfo);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        void initNewUserInfo(String username, String email) {
            User newUser = new User();
            newUser.email = email;
            newUser.name = username;
            newUser.avata = StaticConfig.STR_DEFAULT_BASE64;
            if (user == null) {
                user = mAuth.getCurrentUser();
            }
            FirebaseDatabase.getInstance().getReference().child("user/" + user.getUid()).setValue(newUser);
        }
    }

    //TODO: comment in case of issue or remove after test
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}