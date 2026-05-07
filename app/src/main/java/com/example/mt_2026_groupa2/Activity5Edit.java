package com.example.mt_2026_groupa2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Activity5Edit extends AppCompatActivity {

    private ImageView imageViewEdit;
    private EditText editTextReader, editTextResult;
    private Button buttonSave;

    private String imageUriString;
    private String resultText;
    private int MLKmode;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity5_edit);

        imageViewEdit = findViewById(R.id.imageViewEdit);
        editTextReader = findViewById(R.id.editTextReader);
        editTextResult = findViewById(R.id.editTextResult);
        buttonSave = findViewById(R.id.buttonSave);

        databaseReference = FirebaseDatabase.getInstance().getReference("MLKitResults");

        imageUriString = getIntent().getStringExtra("imageUri");
        resultText = getIntent().getStringExtra("result");
        MLKmode = getIntent().getIntExtra("MLKmode", 1);

        if (imageUriString != null) {
            imageViewEdit.setImageURI(Uri.parse(imageUriString));
        }

        editTextReader.setText(getReaderName(MLKmode));
        editTextResult.setText(resultText);

        buttonSave.setOnClickListener(v -> saveToFirebase());
    }

    private String getReaderName(int mode) {
        if (mode == 1) {
            return "Barcode Reader";
        } else if (mode == 2) {
            return "Image Content Reader";
        } else if (mode == 3) {
            return "Text Reader";
        } else {
            return "Unknown Reader";
        }
    }

    private void saveToFirebase() {
        String id = databaseReference.push().getKey();

        String reader = editTextReader.getText().toString().trim();
        String result = editTextResult.getText().toString().trim();

        if (reader.isEmpty() || result.isEmpty()) {
            Toast.makeText(this, "Please enter reader and result", Toast.LENGTH_SHORT).show();
            return;
        }

        HashMap<String, String> data = new HashMap<>();
        data.put("id", id);
        data.put("reader", reader);
        data.put("result", result);
        data.put("imageUri", imageUriString);

        databaseReference.child(id).setValue(data)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(this, "Saved successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Activity5Edit.this, Activity6List.class);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Save failed: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
    }
}