package com.example.roadkeeper;

import android.net.Uri;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class FirebaseManager {

    private static FirebaseManager instance;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;

    // בנאי פרטי (חלק מתבנית ה-Singleton)
    private FirebaseManager() {
        // אתחול הגישה למסד הנתונים (Realtime Database)
        databaseReference = FirebaseDatabase.getInstance().getReference("travel_logs");
        // אתחול הגישה לאחסון הקבצים (Storage)
        storageReference = FirebaseStorage.getInstance().getReference("images");
    }

    // פונקציה לקבלת המופע היחיד של המחלקה
    public static synchronized FirebaseManager getInstance() {
        if (instance == null) {
            instance = new FirebaseManager();
        }
        return instance;
    }

    // ממשק (Interface) לעדכון ה-Activity כשהפעולה מצליחה או נכשלת
    public interface FirebaseCallback<T> {
        void onSuccess(T result);
        void onFailure(Exception e);
    }

    /**
     * פונקציה לשמירת אובייקט TravelLog ב-Database
     */
    public void saveTravelLog(TravelLog log, FirebaseCallback<Void> callback) {
        // אם אין ללוג ID, ניצור לו אחד ייחודי מה-Firebase
        if (log.getId() == null || log.getId().isEmpty()) {
            String key = databaseReference.push().getKey();
            log.setId(key);
        }

        databaseReference.child(log.getId()).setValue(log)
                .addOnSuccessListener(aVoid -> callback.onSuccess(null))
                .addOnFailureListener(e -> callback.onFailure(e));
    }

    /**
     * פונקציה להעלאת תמונה ל-Storage
     * @return מחזירה את ה-URL של התמונה אחרי ההעלאה
     */
    public void uploadImage(Uri imageUri, FirebaseCallback<String> callback) {
        // יצירת שם קובץ ייחודי לפי זמן נוכחי
        StorageReference fileRef = storageReference.child(System.currentTimeMillis() + ".jpg");

        fileRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    // לאחר ההעלאה, נבקש את הקישור הציבורי לתמונה
                    fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        callback.onSuccess(uri.toString());
                    });
                })
                .addOnFailureListener(e -> callback.onFailure(e));
    }
}