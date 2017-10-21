package thacks.muse;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class launch extends AppCompatActivity {

    EditText user, pass;
    Button login, createUser;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        //init textviews
        user=(EditText)findViewById(R.id.userPrompt);
        pass=(EditText)findViewById(R.id.passPrompt);
        //buttons
        login=(Button)findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validLogin())
                    existingUserLogin(user.getText().toString(), pass.getText().toString());
            }
        });

        createUser=(Button)findViewById(R.id.createAccount);
        createUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validLogin())
                 createNewUser(user.getText().toString(), pass.getText().toString());
            }
        });

        //firebase
        mAuth = FirebaseAuth.getInstance();
        mAuthListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                } else {
                }
            }
        };
    }
    private void existingUserLogin(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (!task.isSuccessful()) {
                            Toast.makeText(launch.this, R.string.existingUserError,
                                    Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(launch.this, R.string.existingUserSuccess,
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void createNewUser(String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (!task.isSuccessful()) {
                            Toast.makeText(launch.this,R.string.newUserError, Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(launch.this,R.string.newUserSuccess, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private boolean validLogin(){
        if ((user.getText()==null||pass.getText()==null)&&((!user.getText().equals("Enter email"))||(!user.getText().equals("Enter password")))){
            Toast.makeText(launch.this, "Either username or password field is empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        else{
            return true;
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
