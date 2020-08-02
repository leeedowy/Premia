package com.example.premia;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";

    private ArrayAdapter<String> mainMenuAdapter;
    private ListView mainMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<String> menuItemsList = Arrays.asList(getResources().getStringArray(R.array.main_menu_items));
        mainMenuAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.main_menu_item, R.id.mainMenuItemTextView, menuItemsList);

        mainMenu = findViewById(R.id.menuListView);
        mainMenu.setAdapter(mainMenuAdapter);
        mainMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        // TODO IMPLEMENT BONUS TRACKING
                        break;
                    case 1:
                        // TODO IMPLEMENT HISTORY
                        break;
                    case 2:
                        // TODO IMPLEMENT STATS
                        break;
                    default:
                        Log.e(TAG, "Position " + position + " not supported.");
                }
            }
        });
    }
}
