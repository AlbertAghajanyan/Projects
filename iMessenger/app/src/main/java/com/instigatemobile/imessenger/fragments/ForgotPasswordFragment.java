package com.instigatemobile.imessenger.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.instigatemobile.imessenger.R;

public class ForgotPasswordFragment extends Fragment {
    private View view;
    private EditText editTextEmail;
    private FragmentManager fragmentManager;
    private LoginFragment.AuthUtils mAuthUtils;
    private ProgressBar bar;
    private Button submit;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_forgot, container, false);
        initViews();
        setListeners();
        return view;
    }

    public void setAuthUtils(LoginFragment.AuthUtils authUtils) {
        mAuthUtils = authUtils;
    }

    private void setListeners() {
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString().trim();
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getActivity(), "Enter your registered email", Toast.LENGTH_SHORT).show();
                    return;
                }
                bar.setVisibility(View.VISIBLE);
                FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getActivity(), "We have sent you instructions to reset your password", Toast.LENGTH_SHORT).show();
                                    goLoginPage();
                                } else {
                                    Toast.makeText(getActivity(), "Failed to send reset email", Toast.LENGTH_SHORT).show();
                                }
                                bar.setVisibility(View.GONE);
                            }
                        });
                goLoginPage();
            }
        });
    }

    private void initViews() {
        fragmentManager = getActivity().getSupportFragmentManager();
        editTextEmail = (EditText) view.findViewById(R.id.input_email);
        bar = (ProgressBar) view.findViewById(R.id.progressBar);
        submit = (Button) view.findViewById(R.id.btn_submit);
    }

    private void goLoginPage() {
        LoginFragment loginFragment = new LoginFragment();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer, loginFragment);
        fragmentTransaction.addToBackStack("Forgot");
        fragmentTransaction.commit();
    }
}