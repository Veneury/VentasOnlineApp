package com.inc.soft.devers.ventasonlineapp.Activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import com.inc.soft.devers.ventasonlineapp.R;

public class RegisterActivity extends AppCompatActivity {
    ImageView imgUserPhoto;
    static int PReqCode=1;
    static int REQUESCODE=1;
    Uri pickedImgUri;

    private EditText userEmail,userPassword,userPassword2,userName;
    private ProgressBar loadingProgram;
    private Button regBtn;

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        this.setTitle("Registro");

        //
        imgUserPhoto = findViewById(R.id.reg_UserPhoto);
        userEmail = findViewById(R.id.regMail);
        userPassword = findViewById(R.id.regPassword);
        userPassword2 = findViewById(R.id.regPassword2);
        userName = findViewById(R.id.regName);
        regBtn = findViewById(R.id.regBtn);
        loadingProgram = findViewById(R.id.reg_ProgressBar);

        mAuth =FirebaseAuth.getInstance();
        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                regBtn.setVisibility(View.INVISIBLE);
                loadingProgram.setVisibility(View.VISIBLE);
                final String email=userEmail.getText().toString();
                final String password=userPassword.getText().toString();
                final String password2=userPassword2.getText().toString();
                final String name=userName.getText().toString();

                if(email.isEmpty()|| name.isEmpty() || password.isEmpty()|| !password.equals(password2) || imgUserPhoto==null)
                {
                    showMessage("Por favor todos los datos tienen que estar llenos y las contraseñas iguales");
                    regBtn.setVisibility(View.VISIBLE);
                    loadingProgram.setVisibility(View.INVISIBLE);
                }else
                {
                    createUserAccount(email,name,password);
                }
            }
        });

        imgUserPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Build.VERSION.SDK_INT >=22)
                {
                    checkAndRequestForPermission();
                }else{
                    openGallery();
                }


            }
        });
    }

    private void createUserAccount(String email, final String name, String password) {
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this,new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    showMessage("Usuario registrado correctamente");
                    updateUserInfo(name,pickedImgUri,mAuth.getCurrentUser());

                }
                else
                {
                    showMessage("Upps!! hubo un error" +task.getException().getMessage());
                    regBtn.setVisibility(View.VISIBLE);
                    loadingProgram.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    private void updateUserInfo(final String name, Uri pickedImgUri, final FirebaseUser currentUser) {
        StorageReference mRstorage = FirebaseStorage.getInstance().getReference().child("users_photo");
        final StorageReference imageFilePath = mRstorage.child(pickedImgUri.getLastPathSegment());
        imageFilePath.putFile(pickedImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        UserProfileChangeRequest profleUpdate = new UserProfileChangeRequest.Builder()
                                .setDisplayName(name)
                                .setPhotoUri(uri)
                                .build();

                        currentUser.updateProfile(profleUpdate)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        showMessage("Usuario registrado corectatmente");
                                        updateUI();
                                    }
                                    }
                                });
                    }
                });
            }
        });
    }

    private void updateUI() {
         Intent homeActivity = new Intent(getApplicationContext(),Home.class);
         startActivity(homeActivity);
    }


    private void showMessage(String Message) {
        Toast.makeText(this,Message,Toast.LENGTH_SHORT).show();
    }

    private void openGallery() {
        //Abrir Galeria
        Intent galeryInten = new Intent(Intent.ACTION_GET_CONTENT);
        galeryInten.setType("image/*");
        startActivityForResult(galeryInten,REQUESCODE);
    }

    private void checkAndRequestForPermission() {

        if(ContextCompat.checkSelfPermission(RegisterActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(RegisterActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE)){
                Toast.makeText(this,"Por favor acepta el permiso requerido",Toast.LENGTH_LONG).show();
            }
            else
            {
                ActivityCompat.requestPermissions(RegisterActivity.this,
                        new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                        PReqCode);
            }
        }else
        {
            openGallery();
        }




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && requestCode==REQUESCODE && data !=null)
        {
            //--------------------
            pickedImgUri=data.getData();
            imgUserPhoto.setImageURI(pickedImgUri);

        }
    }
}
