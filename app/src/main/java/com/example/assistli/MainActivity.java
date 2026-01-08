package com.example.assistli;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView tvWelcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Ensure this matches your XML filename

        // 1. Initialize the TextView from your XML
        tvWelcome = findViewById(R.id.tvWelcome);

        // 2. Open SharedPreferences (use the same name "MyAppPrefs" you used in OpenActivity)
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);

        // 3. Get the saved username (defaults to "אורח" if nothing is saved)
        String savedUsername = sharedPreferences.getString("USERNAME", "אורח");

        // 4. Update the text to show the name
        tvWelcome.setText("ברוכים הבאים, " + savedUsername);
    }
}