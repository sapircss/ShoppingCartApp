package com.example.shoppingcartapp.Fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.shoppingcartapp.R;
import com.google.firebase.auth.FirebaseAuth;

public class Login_Frag extends Fragment {
    private FirebaseAuth mAuth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login_, container, false);

        EditText emailField = view.findViewById(R.id.LoginEmail);
        EditText passwordField = view.findViewById(R.id.LoginPassword);
        Button loginButton = view.findViewById(R.id.btnToCart);
        Button regButton = view.findViewById(R.id.btnToRegister);

        loginButton.setOnClickListener(v -> {
            String email = emailField.getText().toString().trim();
            String password = passwordField.getText().toString().trim();

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(getActivity(), "Email and Password are required", Toast.LENGTH_SHORT).show();
            } else {
                loginUser(email, password, view);
            }
        });

        regButton.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_login_Frag_to_sign_Frag));

        return view;
    }

    private void loginUser(String email, String password, View view) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getActivity(), "Login Successful!", Toast.LENGTH_SHORT).show();
                        try {
                            Navigation.findNavController(view).navigate(R.id.action_login_Frag_to_items_Frag);
                        } catch (Exception e) {
                            Toast.makeText(getActivity(), "Navigation failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), "Login Failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}