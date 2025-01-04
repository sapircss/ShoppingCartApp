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

import com.example.shoppingcartapp.Models.User;
import com.example.shoppingcartapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class Sign_Frag extends Fragment {

    private FirebaseAuth mAuth;
    private DatabaseReference usersRef;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        usersRef = FirebaseDatabase.getInstance().getReference("users");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_, container, false);

        EditText emailField = view.findViewById(R.id.etEmail);
        EditText passwordField = view.findViewById(R.id.etPassword);
        EditText confirmPasswordField = view.findViewById(R.id.repeatPassword);
        EditText phoneField = view.findViewById(R.id.etPhoneNumber);
        Button registerButton = view.findViewById(R.id.btnSignIn);

        registerButton.setOnClickListener(v -> {
            String email = emailField.getText().toString().trim();
            String password = passwordField.getText().toString().trim();
            String confirmPassword = confirmPasswordField.getText().toString().trim();
            String phone = phoneField.getText().toString().trim();

            if (validateInputs(email, password, confirmPassword, phone)) {
                registerUser(email, password, phone, view);
            }
        });

        return view;
    }

    private boolean validateInputs(String email, String password, String confirmPassword, String phone) {
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword) || TextUtils.isEmpty(phone)) {
            Toast.makeText(getActivity(), "All fields are required", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(getActivity(), "Passwords do not match", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (password.length() < 6) {
            Toast.makeText(getActivity(), "Password must be at least 6 characters long", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(getActivity(), "Invalid email format", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void registerUser(String email, String password, String phone, View view) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful())
                    {
                        Toast.makeText(getActivity(), "Registration Successful!", Toast.LENGTH_SHORT).show();
                        saveUserToDatabase(new User(email, password, phone), view);
                        Navigation.findNavController(view).navigate(R.id.action_sign_Frag_to_login_Frag);
                    }
                    else {
                        Toast.makeText(getActivity(), "Registration Failed ", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void saveUserToDatabase(User user, View view) {
        String sanitizedEmail = sanitizeEmail(user.getEmail());
        usersRef.child(sanitizedEmail).setValue(user)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getActivity(), "User saved to database successfully!", Toast.LENGTH_SHORT).show();
                        Navigation.findNavController(view).navigate(R.id.action_sign_Frag_to_login_Frag);
                    } else {
                        Toast.makeText(getActivity(), "Failed to save user to database: ", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private String sanitizeEmail(String email) {
        return email.replace(".", ",");
    }
}
