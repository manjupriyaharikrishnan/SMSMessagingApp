package com.asap.messenger;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.asap.messenger.custom.ViewMessagesListAdapter;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;


public class ViewAllMessagesActivity extends AppCompatActivity {

    ListView list;

    String[] itemname ={
            "Safari",
            "Camera",
            "Global",
            "FireFox",
            "UC Browser",
            "Android Folder",
            "VLC Player",
            "Cold War"
    };

    String[] messages ={
            "Safari Description",
            "Camera Description",
            "Global Description",
            "FireFox Description",
            "UC Browser Description",
            "Android Folder Description",
            "VLC Player Description",
            "Cold War Description"
    };

    Integer[] imgid={
            R.drawable.usericon,
            R.drawable.usericon,
            R.drawable.usericon,
            R.drawable.usericon,
            R.drawable.usericon,
            R.drawable.usericon,
            R.drawable.usericon,
            R.drawable.usericon,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewMessagesListAdapter adapter=new ViewMessagesListAdapter(this, itemname, messages);
        list=(ListView)findViewById(R.id.list);
        list.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
