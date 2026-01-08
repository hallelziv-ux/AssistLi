package com.example.assistli;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class OpenActivity extends AppCompatActivity {
    private Dialog dialog;
    private EditText editDUserName, editDmail, editDpassword, editPhoneNum;
    private TextView tvDMessage;
    private Button btnDGoBack, btnNoLogCan;
    public DataBaseHelper dbHelper;
    private Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_open);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        dbHelper = new DataBaseHelper(this);
        handler.postDelayed(this::createLoginDialog, 1500);
    }

    public void createSignInDialog() {
        if (dialog != null) dialog.dismiss();
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.firstsignin);
        dialog.setCancelable(true);

        tvDMessage = dialog.findViewById(R.id.tvMessage);
        editDUserName = dialog.findViewById(R.id.editUsername);
        editDmail = dialog.findViewById(R.id.editEmail);
        editDpassword = dialog.findViewById(R.id.editPassword);
        editPhoneNum = dialog.findViewById(R.id.editPhoneNum);
        btnDGoBack = dialog.findViewById(R.id.btnContinue);

        btnDGoBack.setOnClickListener(v -> {
            String username = editDUserName.getText().toString().trim();
            String email = editDmail.getText().toString().trim();
            String password = editDpassword.getText().toString().trim();
            String phonenum = editPhoneNum.getText().toString().trim();

            // Validation
            if (username.isEmpty() || email.isEmpty() || password.isEmpty() || phonenum.isEmpty()) {
                showMessage("Please fill all fields");
                return;
            }
            if (!email.contains("@") || !email.contains(".com")) {
                showMessage("Invalid email format");
                return;
            }
            if (phonenum.length() != 10 || !phonenum.startsWith("0")) {
                showMessage("Invalid phone number (must be 10 digits starting with 0)");
                return;
            }
            Intent intent = new Intent(OpenActivity.this, ContinueSignin.class);
            intent.putExtra("USER_NAME", username);
            intent.putExtra("USER_EMAIL", email);
            intent.putExtra("USER_PASS", password);
            intent.putExtra("USER_PHONE", phonenum);
            startActivity(intent);
            dialog.dismiss();
        });
        dialog.show();
    }

    public void createLoginDialog() {
        if (dialog != null) dialog.dismiss();
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.login);
        dialog.setCancelable(true);

        tvDMessage = dialog.findViewById(R.id.tvMessage);
        editDUserName = dialog.findViewById(R.id.editUsername);
        editDpassword = dialog.findViewById(R.id.editPassword);
        btnDGoBack = dialog.findViewById(R.id.btnContinue);
        btnNoLogCan = dialog.findViewById(R.id.btnNoLogCan);

        btnNoLogCan.setOnClickListener(v -> createSignInDialog());

        btnDGoBack.setOnClickListener(v -> {
            String username = editDUserName.getText().toString().trim();
            String password = editDpassword.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                showMessage("Please fill all fields");
                return;
            }

            if (dbHelper.loginUserByUsername(username, password)) {
                showMessage("Login successful");

                // Save to SharedPreferences
                SharedPreferences prefs = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
                prefs.edit().putString("USERNAME", username).apply();

                handler.postDelayed(() -> {
                    startActivity(new Intent(OpenActivity.this, MainActivity.class));
                    finish();
                }, 1000);
            } else {
                showMessage("Login failed. Please sign up.");
                handler.postDelayed(this::createSignInDialog, 1500);
            }
        });
        dialog.show();
    }

    private void showMessage(String msg) {
        if (tvDMessage != null) {
            tvDMessage.setVisibility(View.VISIBLE);
            tvDMessage.setText(msg);
        }
    }
}