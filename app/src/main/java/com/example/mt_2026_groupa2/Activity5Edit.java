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
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class Activity5Edit extends AppCompatActivity {

    private ImageView imageViewEdit;
    private EditText editTextReader;
    private EditText editTextResult;
    private Button buttonSave;

    private String id;
    private String imageUriString;
    private String resultText;
    private String readerText;

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

        databaseReference =
                FirebaseDatabase
                        .getInstance()
                        .getReference("MLKitResults");

        id = getIntent().getStringExtra("id");

        imageUriString =
                getIntent().getStringExtra("imageUri");

        resultText =
                getIntent().getStringExtra("result");

        readerText =
                getIntent().getStringExtra("reader");

        MLKmode =
                getIntent().getIntExtra("MLKmode", 1);

        if (imageUriString != null &&
                !imageUriString.isEmpty()) {

            try {

                Uri uri = Uri.parse(imageUriString);

                imageViewEdit.post(() -> {

                    try {

                        imageViewEdit.setImageURI(uri);

                    } catch (SecurityException e) {

                        imageViewEdit.setImageResource(
                                android.R.color.transparent
                        );

                    } catch (Exception e) {

                        imageViewEdit.setImageResource(
                                android.R.color.transparent
                        );
                    }
                });

            } catch (Exception e) {

                imageViewEdit.setImageResource(
                        android.R.color.transparent
                );
            }
        }

        if (readerText != null &&
                !readerText.isEmpty()) {

            editTextReader.setText(readerText);

        } else {

            editTextReader.setText(
                    getReaderName(MLKmode)
            );
        }

        if (resultText != null) {

            editTextResult.setText(resultText);

        } else {

            editTextResult.setText("");
        }

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

    private String copyImageToAppStorage(String uriString) {
        try {
            File destDir = new File(getFilesDir(), "saved_images");
            if (!destDir.exists()) destDir.mkdirs();

            String fileName = "img_" + System.currentTimeMillis() + ".jpg";
            File destFile = new File(destDir, fileName);

            Uri sourceUri = Uri.parse(uriString);

            try (InputStream in = getContentResolver().openInputStream(sourceUri);
                 OutputStream out = new FileOutputStream(destFile)) {

                if (in == null) return uriString; // fallback to original

                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
            }

            return Uri.fromFile(destFile).toString(); // permanent file:// URI

        } catch (Exception e) {
            e.printStackTrace();
            return uriString; // fallback to original if copy fails
        }
    }

    private void saveToFirebase() {

        if (id == null || id.isEmpty()) {

            id = databaseReference.push().getKey();
        }

        String reader =
                editTextReader
                        .getText()
                        .toString()
                        .trim();

        String result =
                editTextResult
                        .getText()
                        .toString()
                        .trim();

        if (reader.isEmpty() || result.isEmpty()) {

            Toast.makeText(
                    this,
                    "Please enter reader and result",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        HashMap<String, String> data =
                new HashMap<>();

        data.put("id", id);

        data.put("reader", reader);

        data.put("result", result);

        String permanentUri = (imageUriString != null && !imageUriString.isEmpty())
                ? copyImageToAppStorage(imageUriString)
                : imageUriString;

        data.put("imageUri", permanentUri);

        databaseReference
                .child(id)
                .setValue(data)

                .addOnSuccessListener(unused -> {

                    Toast.makeText(
                            this,
                            "Saved successfully",
                            Toast.LENGTH_SHORT
                    ).show();

                    Intent intent =
                            new Intent(
                                    Activity5Edit.this,
                                    Activity6List.class
                            );

                    intent.addFlags(
                            Intent.FLAG_ACTIVITY_CLEAR_TOP
                    );

                    startActivity(intent);

                    finish();
                })

                .addOnFailureListener(e ->

                        Toast.makeText(
                                this,
                                "Save failed: " + e.getMessage(),
                                Toast.LENGTH_SHORT
                        ).show());
    }
}