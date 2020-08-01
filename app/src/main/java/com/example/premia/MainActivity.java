package com.example.premia;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";

    private ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String[] menuItemsArr = getResources().getStringArray(R.array.main_menu_items);
        List<String> menuItemsList = Arrays.asList(menuItemsArr);
        arrayAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.menu_item, R.id.menuItemTextView, menuItemsList);

        ListView menuList = findViewById(R.id.menuListView);
        menuList.setAdapter(arrayAdapter);
    }
}
