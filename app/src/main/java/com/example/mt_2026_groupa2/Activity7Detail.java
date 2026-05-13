package com.example.mt_2026_groupa2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Activity7Detail extends AppCompatActivity {

    private ImageView imageViewDetail;
    private TextView textViewReader, textViewResult;
    private Button buttonEdit, buttonDelete, buttonCancel;

    private String id;
    private String reader;
    private String result;
    private String imageUri;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity7_detail);

        imageViewDetail = findViewById(R.id.imageViewDetail);
        textViewReader = findViewById(R.id.textViewReader);
        textViewResult = findViewById(R.id.textViewResult);

        buttonEdit = findViewById(R.id.buttonEdit);
        buttonDelete = findViewById(R.id.buttonDelete);
        buttonCancel = findViewById(R.id.buttonCancel);

        id = getIntent().getStringExtra("id");
        reader = getIntent().getStringExtra("reader");
        result = getIntent().getStringExtra("result");
        imageUri = getIntent().getStringExtra("imageUri");

        if (reader == null || reader.isEmpty()) {
            reader = "Unknown Reader";
        }

        if (result == null) {
            result = "";
        }

        textViewReader.setText(reader);
        textViewResult.setText(result);

        imageViewDetail.setImageResource(android.R.color.transparent);

        if (imageUri != null && !imageUri.isEmpty()) {

            try {

                Uri uri = Uri.parse(imageUri);

                imageViewDetail.post(() -> {

                    try {

                        imageViewDetail.setImageURI(uri);

                    } catch (SecurityException e) {

                        imageViewDetail.setImageResource(
                                android.R.color.transparent
                        );

                    } catch (Exception e) {

                        imageViewDetail.setImageResource(
                                android.R.color.transparent
                        );
                    }
                });

            } catch (Exception e) {

                imageViewDetail.setImageResource(
                        android.R.color.transparent
                );
            }
        }

        databaseReference = FirebaseDatabase
                .getInstance()
                .getReference("MLKitResults");

        buttonEdit.setOnClickListener(v -> {

            Intent intent =
                    new Intent(Activity7Detail.this, Activity5Edit.class);

            intent.putExtra("id", id);
            intent.putExtra("reader", reader);
            intent.putExtra("imageUri", imageUri);
            intent.putExtra("result", result);
            intent.putExtra("isEdit", true);

            startActivity(intent);
        });

        buttonDelete.setOnClickListener(v -> {

            if (id == null || id.isEmpty()) {

                Toast.makeText(
                        Activity7Detail.this,
                        "Invalid item",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            databaseReference.child(id).removeValue()

                    .addOnSuccessListener(unused -> {

                        Toast.makeText(
                                Activity7Detail.this,
                                "Deleted successfully",
                                Toast.LENGTH_SHORT
                        ).show();

                        Intent intent =
                                new Intent(
                                        Activity7Detail.this,
                                        Activity6List.class
                                );

                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        startActivity(intent);
                        finish();
                    })

                    .addOnFailureListener(e ->

                            Toast.makeText(
                                    Activity7Detail.this,
                                    "Delete failed",
                                    Toast.LENGTH_SHORT
                            ).show());
        });

        buttonCancel.setOnClickListener(v -> finish());
    }
}