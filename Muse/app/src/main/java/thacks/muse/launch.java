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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class launch extends AppCompatActivity {

    EditText user, pass;
    Button login, createUser;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static String id;
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
                            loadUser();
                            Intent intent=new Intent(launch.this,profile.class);
                            startActivity(intent);

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
                            establishUser();
                            Intent intent=new Intent(launch.this,userRegistration.class);
                            startActivity(intent);
                        }
                    }
                });
    }

    private boolean validLogin(){
        if ((user.getText()==null||pass.getText()==null)||((user.getText().equals("Enter email"))||(user.getText().equals("Enter password")))){
            Toast.makeText(launch.this, "Either username or password field is empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        else{
            return true;
        }
    }

    currentUser cU;
    private void loadUser(){
        //get firebase info pertaining to user
        id=mAuth.getCurrentUser().getUid().toString();
        //create instance of user class with information
        readValue();
    }

    private void establishUser(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("USERS").child(mAuth.getCurrentUser().getUid().toString()).child("email");
        myRef.setValue(user.getText().toString());
    }

    private void readValue(){

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("USERS").child(id);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String firstName=dataSnapshot.child("firstName").getValue().toString();
                String lastName=dataSnapshot.child("lastName").getValue().toString();
                String email=dataSnapshot.child("email").getValue().toString();
                Log.d("msg:",firstName+"");
                cU=new currentUser(firstName,lastName,email);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.d("error",error.getDetails()+"");
            }
        });
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


