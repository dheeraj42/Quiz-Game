package com.example.quizgame;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class Forgot_Password extends AppCompatActivity {

    EditText mail;
    Button button;
    ProgressBar progressBar;

    FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgot_password);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mail = findViewById(R.id.editTextPasswordEmail);
        button = findViewById(R.id.buttonPasswordContinue);
        progressBar = findViewById(R.id.progressBarForgotPassword);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userEmail = mail.getText().toString();
                resetPassword(userEmail);
            }
        });
    }

    public void resetPassword(String userEmail){

        progressBar.setVisibility(View.VISIBLE);
        auth.sendPasswordResetEmail(userEmail).addOnCompleteListener(this,new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()){
                    Toast.makeText(Forgot_Password.this, "We sent you an email to reset your password."
                            , Toast.LENGTH_LONG).show();
                    button.setClickable(false);
                    progressBar.setVisibility(View.INVISIBLE);

                    finish();
                }
                else{
                    Toast.makeText(Forgot_Password.this, "Sorry, there is a problem , try again later..", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}