package com.example.cmput301project.activities;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cmput301project.MainActivity;
import com.example.cmput301project.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText emailField;
    private EditText passwordField;
    private Button signInButton;
    private TextView createAccountTextView;
    private FirebaseAuth userAuth;

    private void grabUIElements(){
        emailField = findViewById(R.id.emailEntry);
        passwordField = findViewById(R.id.passwordEntry);
        passwordField.setTypeface(Typeface.DEFAULT); // To display the hint
        signInButton = findViewById(R.id.signInButton);
        createAccountTextView = findViewById(R.id.accountCreationText);
    }

    private void showToast(String msg){
        Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

    private void checkUserLoggedOn(){
        FirebaseUser currentUser = userAuth.getCurrentUser();
        if(currentUser != null) navigateToMainPage();
    }

    private boolean checkForInvalidInputs() {
        if (emailField.getText().toString().equals("")){
            showToast("Please enter an email");
            return true;
        }
        else if (passwordField.getText().toString().equals("")){
            showToast("Please enter a password");
            return true;
        }
        return false;
    }

    private void navigateToSignUp(View view){
        Intent i  = new Intent(LoginActivity.this, SignUpActivity.class);
        startActivity(i);
    }

    private void navigateToMainPage(){
        //TODO probably better way, this is just so this can be
        // merged in and used
        Intent i  = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(i);
    }

    private void addListeners(){
        signInButton.setOnClickListener(this::attemptLogin);
        createAccountTextView.setOnClickListener(this::navigateToSignUp);
    }

    public void attemptLogin(View v){
        if (checkForInvalidInputs()) return;

        String userEmail = emailField.getText().toString();
        String userPassword = passwordField.getText().toString();

        userAuth.signInWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success");
                        navigateToMainPage();
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        showToast(task.getException().getLocalizedMessage());
                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        userAuth = FirebaseAuth.getInstance();

        checkUserLoggedOn();

        grabUIElements();
        addListeners();
    }

}
