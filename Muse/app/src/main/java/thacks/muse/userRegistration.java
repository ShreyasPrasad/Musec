package thacks.muse;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class userRegistration extends AppCompatActivity {

    TextView prompt;
    EditText firstname, lastname;
    Button continuebutton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);

        prompt=(TextView)findViewById(R.id.title);

        firstname=(EditText)findViewById(R.id.firstname);
        lastname=(EditText)findViewById(R.id.lastname);


        continuebutton=(Button)findViewById(R.id.continuebutton);
        continuebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (verifyComplete()){
                    switchScreens();
                }

            }
        });
    }

    private boolean verifyComplete(){
        if ((firstname.getText()==null||lastname.getText()==null)||((firstname.getText().equals("Enter your first name."))||(lastname.getText().equals("Enter your last name.")))){
            Toast.makeText(userRegistration.this, "Either first name or last name field is empty.", Toast.LENGTH_SHORT).show();
            return false;
        }
        else
            return true;
    }

    private void switchScreens(){
        recordData();
        Intent intent=new Intent(userRegistration.this,profile.class);
        startActivity(intent);
    }
    private void recordData(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        //set firstname in firebase database
        DatabaseReference myRef = database.getReference().child("USERS").child(launch.id).child("firstName");
        myRef.setValue(firstname.getText().toString());
        //set lastname in firebase database
        myRef = database.getReference().child("USERS").child(launch.id).child("lastName");
        myRef.setValue(lastname.getText().toString());
    }
}
