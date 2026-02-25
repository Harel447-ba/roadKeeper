package com.example.roadkeeper;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;

public class CaptureActivity extends AppCompatActivity {

    private ImageView imageView;
    private EditText etDescription;
    private Button btnCapture, btnSave;

    private Bitmap capturedBitmap; // נשמור כאן את התמונה שצולמה
    private double lat, lon; // קואורדינאטות שיגיעו מהמפה

    // 1. הגדרת ה"חוזה" לקבלת תוצאה מהמצלמה (דרישת 5 יח"ל)
    private final ActivityResultLauncher<Intent> cameraLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    // קבלת התמונה כ-Bitmap
                    Bundle extras = result.getData().getExtras();
                    capturedBitmap = (Bitmap) extras.get("data");
                    imageView.setImageBitmap(capturedBitmap);
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture);

        // אתחול רכיבי ממשק
        imageView = findViewById(R.id.iv_preview);
        etDescription = findViewById(R.id.et_description);
        btnCapture = findViewById(R.id.btn_take_photo);
        btnSave = findViewById(R.id.btn_save_log);

        // 2. קבלת הנתונים שנשלחו מה-MapActivity
        lat = getIntent().getDoubleExtra("lat", 0.0);
        lon = getIntent().getDoubleExtra("lon", 0.0);

        // כפתור צילום
        btnCapture.setOnClickListener(v -> {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            cameraLauncher.launch(takePictureIntent);
        });

        // כפתור שמירה
        btnSave.setOnClickListener(v -> saveLogToFirebase());
    }

    private void saveLogToFirebase() {
        if (capturedBitmap == null) {
            Toast.makeText(this, "בבקשה צלם תמונה קודם", Toast.LENGTH_SHORT).show();
            return;
        }

        String description = etDescription.getText().toString();

        // שלב א': הפיכת ה-Bitmap ל-Uri (או העלאה ישירה כ-Stream)
        // לצורך הפשטות נשתמש ב-Manager שיצרנו.
        // נצטרך להוסיף ל-FirebaseManager פונקציה שתומכת ב-Bitmap או להמיר ל-Uri.

        // כאן נכנס הלוגיקה של ה-FirebaseManager:
        // 1. העלאת תמונה -> קבלת URL
        // 2. יצירת אובייקט TravelLog
        // 3. שמירה ב-Database

        Toast.makeText(this, "מתחיל שמירה...", Toast.LENGTH_SHORT).show();
        // (המשך המימוש של השמירה בשלב הבא)
    }
}