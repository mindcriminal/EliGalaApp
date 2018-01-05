package com.mindcriminal.eligalaapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

import static android.content.ContentValues.TAG;

public class SplashActivity extends AppCompatActivity
{
    private ProgressBar spinner;
    private String email, password;
    private FirebaseAuth fauth;
    private String authError;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //fauth = FirebaseAuth.getInstance();
        //email = "test@test.com";
        //password = "testtest";

        spinner=findViewById(R.id.progress_bar);
        spinner.setVisibility(View.VISIBLE);

        k n=new k();
        n.execute("xyz","abc");
    }

    class k extends AsyncTask<String,Integer,Boolean>
    {
        @Override
        protected Boolean doInBackground(String... params)
        {
            try
            {
                Thread.sleep(3000);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values)
        {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Boolean aBoolean)
        {
            super.onPostExecute(aBoolean);

            Intent t1r = new Intent(SplashActivity.this, LoginActivity.class);
            spinner.setVisibility(View.GONE);
            startActivity(t1r);
            finish();
/*
            fauth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task)
                {
                    if (task.isSuccessful())
                    {
                        Log.d(TAG, " Verification : signIn With Email:onComplete:" + task.isSuccessful());
                        //Toast.makeText(SplashActivity.this, "Welcome!", Toast.LENGTH_LONG).show();
                        Intent t1r = new Intent(SplashActivity.this, MainActivity.class);
                        spinner.setVisibility(View.GONE);
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
                        Toast.makeText(SplashActivity.this, authError,
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
            */
        }
    }
}