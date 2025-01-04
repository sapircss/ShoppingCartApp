package com.example.shoppingcartapp.Activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.shoppingcartapp.R;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private FirebaseAuth mAuth;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // Initialize NavController
        try {
            navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        } catch (Exception e) {
            Log.e(TAG, "Error initializing NavController: " + e.getMessage());
            Toast.makeText(this, "Navigation Controller Error", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if a user is already logged in
        if (mAuth.getCurrentUser() != null) {
            Log.d(TAG, "User is already logged in: " + mAuth.getCurrentUser().getEmail());
            Toast.makeText(this, "Welcome back, " + mAuth.getCurrentUser().getEmail(), Toast.LENGTH_SHORT).show();

            // Navigate to Items_Frag if logged in
            try {
                navController.navigate(R.id.items_Frag);
            } catch (Exception e) {
                Log.e(TAG, "Navigation to Items_Frag failed: " + e.getMessage());
                Toast.makeText(this, "Failed to navigate to items", Toast.LENGTH_SHORT).show();
            }
        } else {
            Log.d(TAG, "No user is logged in.");
        }
    }
}
