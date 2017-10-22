package thacks.muse;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class profile extends AppCompatActivity {

    Button playlist, muse, tests;
    ImageView title, profilepic;
    TextView userPrompt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //init textfield
        userPrompt=(TextView)findViewById(R.id.introText);
        //init imageviews
        title=(ImageView)findViewById(R.id.title2);
        profilepic=(ImageView)findViewById(R.id.profilePic);

        //init all buttons
        playlist=(Button)findViewById(R.id.playlists);
        muse=(Button)findViewById(R.id.connectmuse);
        tests=(Button)findViewById(R.id.musictests);

        //actions for all buttons
        playlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(profile.this, playlists.class);
                startActivity(intent);
            }
        });

        muse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        tests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //set text values
        userPrompt.setText("Welcome back "+currentUser.firstName);

        //set fonts
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/moonlight.ttf");
        playlist.setTypeface(font);
        muse.setTypeface(font);
        tests.setTypeface(font);
        userPrompt.setTypeface(font);
    }
}
