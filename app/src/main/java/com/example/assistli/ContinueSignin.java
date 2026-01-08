package com.example.assistli;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;

public class ContinueSignin extends AppCompatActivity {
    private EditText birthdate, editMeters;
    private Button btnPickFile, btnPickPic, btnRegister;
    private RadioButton rbtnGenderF, rbtnGenderM, rbtnDisableY, rbtnDisableN;
    private DataBaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.continuesignin);

        dbHelper = new DataBaseHelper(this);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        birthdate = findViewById(R.id.birthdate_text);
        editMeters = findViewById(R.id.edit_text_Meters);
        btnPickFile = findViewById(R.id.btn_pick_file);
        btnPickPic = findViewById(R.id.btn_pick_profile_pic);
        btnRegister = findViewById(R.id.btnRegister);
        rbtnDisableY = findViewById(R.id.radio_button_yes);
        rbtnDisableN = findViewById(R.id.radio_button_no);
        rbtnGenderF = findViewById(R.id.radio_button_female);
        rbtnGenderM = findViewById(R.id.radio_button_male);

        editMeters.setEnabled(false);
        editMeters.setAlpha(0.5f);

        rbtnDisableY.setOnClickListener(v -> {
            editMeters.setEnabled(true);
            editMeters.setAlpha(1.0f);
        });

        rbtnDisableN.setOnClickListener(v -> {
            editMeters.setEnabled(false);
            editMeters.setAlpha(0.5f);
            editMeters.setText("");
        });

        ActivityResultLauncher<Intent> filePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri fileUri = result.getData().getData();
                    }
                });

        birthdate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    ContinueSignin.this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        String date = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                        birthdate.setText(date);
                    }, year, month, day
            );
            datePickerDialog.show();
        });

        btnRegister.setOnClickListener(v -> {
            String username = getIntent().getStringExtra("USER_NAME");
            String email = getIntent().getStringExtra("USER_EMAIL");
            String password = getIntent().getStringExtra("USER_PASS");
            String gender = rbtnGenderM.isChecked() ? "Male" : "Female";
            String disabled = rbtnDisableY.isChecked() ? "Yes" : "No";
            String dob = birthdate.getText().toString();
            String details = editMeters.getText().toString();

            if (dob.isEmpty()) {
                Toast.makeText(this, "Please enter birthdate", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean isInserted = dbHelper.registerUser(username, email, password, gender, dob, disabled);

            if (isInserted) {
                Toast.makeText(this, "Registration Complete!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ContinueSignin.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Error saving data", Toast.LENGTH_SHORT).show();
            }
        });

        btnPickFile.setOnClickListener(v -> openFilePicker(filePickerLauncher));
        btnPickPic.setOnClickListener(v -> openFilePicker(filePickerLauncher));
    }

    private void openFilePicker(ActivityResultLauncher<Intent> launcher) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        String[] mimeTypes = {"image/*", "application/pdf"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        launcher.launch(intent);
    }
}