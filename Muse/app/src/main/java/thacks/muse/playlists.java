package thacks.muse;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class playlists extends AppCompatActivity {

    TextView title;
    ListView container;
    private ArrayList<String> list;
    private ArrayList<String> links;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlists);
        //listview configuration
        container = (ListView) findViewById(R.id.container);
        //title
        title = (TextView) findViewById(R.id.title3);
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/moonlight.ttf");
        title.setTypeface(font);

        list = new ArrayList<String>();
        links = new ArrayList<String>();
        initList();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
        container.setAdapter(adapter);

        container.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String url = links.get(position);
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

    }

    private void initList() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("USERS").child(currentUser.id).child("playlists");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        String currentItem = ds.getKey().toString();
                        String currentLink = ds.getValue().toString();
                        list.add(currentItem);
                        links.add(currentLink);
                    }
                }
                else{
                    list.add("No Playlists Added Yet");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}



