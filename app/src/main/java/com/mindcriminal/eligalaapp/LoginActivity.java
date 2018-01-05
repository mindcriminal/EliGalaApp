package com.mindcriminal.eligalaapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

import static android.content.ContentValues.TAG;

/**
 * Created by mindcriminal on 1/2/18.
 */

public class LoginActivity extends AppCompatActivity {

    private EditText emailText, passwordText;
    private Button loginButton;
    
    private FirebaseAuth fauth;
    private String authError;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        fauth = FirebaseAuth.getInstance();
        
        emailText = findViewById(R.id.email);
        passwordText = findViewById(R.id.password);
        loginButton = findViewById(R.id.login);

        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    public void login() {
        if (!validate()) {
            onLoginFailed();
            return;
        }

        loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        final String email = emailText.getText().toString();
        final String password = passwordText.getText().toString();

        fauth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task)
                {
                    if (task.isSuccessful())
                    {
                        Log.d(TAG, " Verification : signIn With Email:onComplete:" + task.isSuccessful());
                        Intent t1r = new Intent(LoginActivity.this, MainActivity.class);
                        progressDialog.dismiss();
                        startActivity(t1r);
                        finish();
                    }
                    else
                    {
                        try {
                            throw task.getException();
                        } catch (FirebaseAuthInvalidUserException e) {
                            Log.d(TAG, "email :" + email);
                            authError = "Invalid E-mail";
                        } catch (FirebaseAuthInvalidCredentialsException e) {
                            Log.d(TAG , "password :" + password);
                            authError = "Invalid Password";
                        } catch (FirebaseNetworkException e) {
                            authError = "Network Error";
                        } catch (Exception e) {
                            Log.e(TAG, e.getMessage());
                            authError = "Unknown Error";
                        }
                        Log.w(TAG, "signInWithEmail:failed", task.getException());
                        progressDialog.dismiss();
                        loginButton.setEnabled(true);
                        Toast.makeText(LoginActivity.this, authError,
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginFailed() {
        Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_LONG).show();
        loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailText.setError("enter a valid email address");
            valid = false;
        } else {
            emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            passwordText.setError(null);
        }

        return valid;
    }
}
