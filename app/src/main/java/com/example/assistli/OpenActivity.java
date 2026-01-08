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
    Intent intent;
    private Dialog dialog;
    private EditText editDUserName, editDmail, editDpassword,editPhoneNum;
    private TextView tvDMessage;
    private Button btnDGoBack, btnNoLogCan;
    public DataBaseHelper dbHelper;
    private SharedPreferences sharedPreferences;
    private String savedUsername;
    private Handler handler = new Handler();

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

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            createLoginDialog();
        }, 1500);
        dbHelper = new DataBaseHelper(this);

        this.sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        this.savedUsername = sharedPreferences.getString("USERNAME", "DefaultUser");
    }

    public void createSignInDialog() {
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.firstsignin);
        dialog.setTitle("Sign In");
        dialog.setCancelable(true);
        tvDMessage = (TextView) dialog.findViewById(R.id.tvMessage);
        editDUserName = (EditText) dialog.findViewById(R.id.editUsername);
        editDmail = (EditText) dialog.findViewById(R.id.editEmail);
        editDpassword = (EditText) dialog.findViewById(R.id.editPassword);
        editPhoneNum = (EditText) dialog.findViewById(R.id.editPhoneNum);
        btnDGoBack = (Button) dialog.findViewById(R.id.btnContinue);
        btnDGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editDUserName.getText().toString().trim();
                String email = editDmail.getText().toString().trim();
                String password = editDpassword.getText().toString().trim();
                String phonenum = editPhoneNum.getText().toString().trim();
                if (username.isEmpty() || email.isEmpty() || password.isEmpty() || phonenum.isEmpty()) {
                    tvDMessage.setVisibility(View.VISIBLE);
                    tvDMessage.setText("Please fill all fields");
                    return;
                }
                if (!email.contains("@") || !email.contains(".com")) {
                    tvDMessage.setVisibility(View.VISIBLE);
                    tvDMessage.setText("Invalid email format. Email must contain '@' and '.com'");
                    return;
                }
                if (phonenum.length() != 10 || !phonenum.startsWith("0"))
                {
                    tvDMessage.setVisibility(View.VISIBLE);
                    tvDMessage.setText("Invalid phone number.");
                    return;
                }
                boolean isRegistered = dbHelper.registerUser(username, email, password);
                tvDMessage.setVisibility(View.VISIBLE);
                if (isRegistered) {
                    tvDMessage.setText("Registration successful");
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            intent = new Intent(OpenActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                    }, 1500);
                } else {
                    tvDMessage.setText("Registration failed user/main exist");
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            createLoginDialog();
                        }
                    }, 1500);
                }
            }
        });
        dialog.show();
    }

    public void createLoginDialog() {
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.login);
        dialog.setTitle("Log In");
        dialog.setCancelable(true);
        tvDMessage = (TextView) dialog.findViewById(R.id.tvMessage);
        editDUserName = (EditText) dialog.findViewById(R.id.editUsername);
        editDpassword = (EditText) dialog.findViewById(R.id.editPassword);
        btnDGoBack = (Button) dialog.findViewById(R.id.btnContinue);
        btnNoLogCan = (Button) dialog.findViewById(R.id.btnNoLogCan);
        btnNoLogCan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        createSignInDialog();
                    }
                }, 1500);
            }
        });
        btnDGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editDUserName.getText().toString().trim();
                String password = editDpassword.getText().toString().trim();
                if (username.isEmpty() || password.isEmpty()) {
                    tvDMessage.setVisibility(View.VISIBLE);
                    tvDMessage.setText("Please fill all fields");
                    return;
                }
                boolean isRegistered = dbHelper.loginUserByUsername(username, password);
                tvDMessage.setVisibility(View.VISIBLE);
                if (isRegistered) {
                    tvDMessage.setText("Login successful");
                    SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("USERNAME", username);
                    editor.apply();
                    intent = new Intent(OpenActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    tvDMessage.setText("Login failed please sign in first");
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            createSignInDialog();
                        }
                    }, 1500);
                }
            }
        });
        dialog.show();
    }
}