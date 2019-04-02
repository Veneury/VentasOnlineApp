package com.inc.soft.devers.ventasonlineapp.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.inc.soft.devers.ventasonlineapp.R;

public class LoginActivity extends AppCompatActivity {
private EditText userEmail,userPassword;
private Button btnlogin;
private ProgressBar loginProgress;
private ImageView loginPhoto;
private FirebaseAuth mAuth;
private Intent HomeActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userEmail = findViewById(R.id.login_user);
        userPassword= findViewById(R.id.login_password);

        btnlogin= findViewById(R.id.login_btn);

        loginProgress= findViewById(R.id.login_progress);
        loginPhoto= findViewById(R.id.login_photo);
        mAuth=FirebaseAuth.getInstance();
        loginPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent registerActivity = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(registerActivity);
                finish();

            }
        });
        HomeActivity = new Intent(this, com.inc.soft.devers.ventasonlineapp.Activities.Home.class);
        loginProgress.setVisibility(View.INVISIBLE);

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginProgress.setVisibility(View.VISIBLE);
                btnlogin.setVisibility(View.INVISIBLE);

                final String mail = userEmail.getText().toString();
                final String password=userPassword.getText().toString();

                if(mail.isEmpty() || password.isEmpty())
                {
                    showMessage("Todos los campos deben estar llenos");
                    loginProgress.setVisibility(View.INVISIBLE);
                    btnlogin.setVisibility(View.VISIBLE);
                }
                else
                {
                    signIn(mail,password);
                    loginProgress.setVisibility(View.VISIBLE);
                    btnlogin.setVisibility(View.INVISIBLE);
                    btnlogin.postDelayed(new Runnable() {
                        public void run() {
                            btnlogin.setVisibility(View.VISIBLE);
                            loginProgress.setVisibility(View.INVISIBLE);
                        }
                    }, 5000);
                }
            }
        });
    }

    private void signIn(String mail, String password) {

        mAuth.signInWithEmailAndPassword(mail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    loginProgress.setVisibility(View.INVISIBLE);
                    btnlogin.setVisibility(View.VISIBLE);
                    updateUi();
                }else
                {
                    showMessage(task.getException().getMessage());
                }
            }
        });






    }

    private void updateUi() {
        startActivity(HomeActivity);
        finish();
    }

    private void showMessage(String message) {
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if(user!=null)
        {
            updateUi();
        }
    }
}
