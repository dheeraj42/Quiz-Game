package com.example.quizgame;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.internal.GoogleSignInOptionsExtensionParcelable;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class Login_page extends AppCompatActivity {

    EditText mail;
    EditText password;
    Button signIn;
    SignInButton signInGoogle;
    TextView signUp;
    TextView forgotPassword;

    GoogleSignInClient googleSignInClient;

    FirebaseAuth auth = FirebaseAuth.getInstance();

    ActivityResultLauncher<Intent> activityResultLauncher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // register
        registerActivityForGoogleSignIn();

        mail = findViewById(R.id.editTextLoginEmail);
        password = findViewById(R.id.editTextLoginPassword);
        signIn = findViewById(R.id.buttonLoginSignin);
        signInGoogle = findViewById(R.id.buttonLoginGoogleSignin);
        signUp = findViewById(R.id.textViewLoginSignup);
        forgotPassword = findViewById(R.id.textViewLoginForgotPassword);

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userEmail = mail.getText().toString();
                String userPassword = password.getText().toString();

                signInWithFirebase(userEmail,userPassword);
            }
        });

        signInGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signinGoogle();
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(Login_page.this,Sign_up_page.class);
                startActivity(i);
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login_page.this,Forgot_Password.class);
                startActivity(i);
            }
        });
    }

    public void signinGoogle(){

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("1097951178167-oplvoa6bnuiu2p89dpstpc3v0pvhouja.apps.googleusercontent.com")
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this,gso);

        signin();
    }

    public void signin(){

        Intent signInIntent = googleSignInClient.getSignInIntent();
        activityResultLauncher.launch(signInIntent);
    }


    public void registerActivityForGoogleSignIn(){

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {

                        int resultCode = result.getResultCode();
                        Intent data = result.getData();

                        if(resultCode == RESULT_OK && data != null){

                            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                            firebaseSignInWithGoogle(task);

                        }

                    }
                });

    }

    private void firebaseSignInWithGoogle(Task<GoogleSignInAccount> task){

        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            Toast.makeText(this, "Successfully", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(Login_page.this,MainActivity.class);
            startActivity(i);
            finish();

            firebaseGoogleAccount(account);
            
        } catch (ApiException e) {
            e.printStackTrace();
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }

    }

    private void firebaseGoogleAccount(GoogleSignInAccount account){

        AuthCredential authCredential = GoogleAuthProvider.getCredential(account.getIdToken(),null);
        auth.signInWithCredential(authCredential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                        }else{

                        }
                    }
                });

    }


    public void signInWithFirebase(String userEmail, String userPassword){
        signIn.setClickable(false);

        auth.signInWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener(this,new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        
                        if(task.isSuccessful()){
                            Intent i = new Intent(Login_page.this,MainActivity.class);
                            startActivity(i);
                            finish();
                            Toast.makeText(Login_page.this, "Sign In is successful. ", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(Login_page.this, "Sign In is not successful.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = auth.getCurrentUser();
        if(user != null){
            Intent i = new Intent(Login_page.this,MainActivity.class);
            startActivity(i);
            finish();
        }
    }
}