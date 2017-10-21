package thacks.muse;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.choosemuse.libmuse.MuseManagerAndroid;

public class MuseHeadband extends AppCompatActivity {

    private MuseManagerAndroid manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_muse_headband);

        manager = MuseManagerAndroid.getInstance();
        manager.setContext(this);
    }
}
