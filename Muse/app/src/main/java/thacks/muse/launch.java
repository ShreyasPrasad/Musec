package thacks.muse;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
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
    ImageButton login, createUser;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    public static String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        //init textviews
        user=(EditText)findViewById(R.id.userPrompt);
        pass=(EditText)findViewById(R.id.passPrompt);
        //buttons
        login=(ImageButton)findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validLogin())
                    existingUserLogin(user.getText().toString(), pass.getText().toString());
            }
        });

        createUser=(ImageButton)findViewById(R.id.createAccount);
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

        //set all view fonts
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/moonlight.ttf");
        user.setTypeface(font);
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
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference myRef = database.getReference().child("USERS").child(mAuth.getCurrentUser().getUid().toString()).child("firstName");
                            myRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    loadUser();
                                    if (dataSnapshot.exists()){
                                        CountDownTimer timer1=new CountDownTimer(2000,1000) {
                                            @Override
                                            public void onTick(long millisUntilFinished) {


                                            }

                                            @Override
                                            public void onFinish() {
                                                user.setText(null);
                                                pass.setText(null);
                                                Intent intent=new Intent(launch.this,profile.class);
                                                startActivity(intent);
                                            }
                                        };
                                        timer1.start();
                                    }
                                    else{
                                        loadUser();
                                        user.setText(null);
                                        pass.setText(null);
                                        Intent intent=new Intent(launch.this,userRegistration.class);
                                        startActivity(intent);
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError error) {
                                    Log.d("error",error.getDetails()+"");
                                }
                            });
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
                            user.setText(null);
                            pass.setText(null);
                            Intent intent=new Intent(launch.this,userRegistration.class);
                            startActivity(intent);
                        }
                    }
                });
    }

    private boolean validLogin(){
        if (user.getText().equals("") ||user.getText().equals("Enter Email")){
            user.setError("Username is empty.");
            return false;
        }
        else if (pass.getText().equals("")||pass.getText().equals("Enter Password")){
            pass.setError("Password is empty.");
            return false;
        }
        else if (user.getText().length()<8){
            user.setError("Username must be atleast 8 characters");
            return false;
        }
        else if (pass.getText().length()<8){
            user.setError("Password must be atleast 8 characters");
            return false;
        }
        else{
            return true;
        }
    }

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
        loadUser();
    }

    private void readValue(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("USERS").child(id);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("firstName").exists() && dataSnapshot.child("lastName").exists()) {
                    String firstName = dataSnapshot.child("firstName").getValue().toString();
                    String lastName = dataSnapshot.child("lastName").getValue().toString();
                    currentUser.lastName=lastName;
                    currentUser.firstName=firstName;
                }
                String email=dataSnapshot.child("email").getValue().toString();
                currentUser.email=email;
                currentUser.id=id;
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


