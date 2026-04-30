package com.example.mt_2026_groupa2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Activity1Main extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageButton imageButtonBarcodeReader = findViewById(R.id.imageButtonBarcodeReader);
        imageButtonBarcodeReader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity1Main.this, Activity2.class);
                startActivity(intent);
                // add intent to change image
                intent.putExtra("image_id", R.drawable.barcode);
                startActivity(intent);
            }
        });

        ImageButton imageButtonContentReader = findViewById(R.id.imageButtonContentReader);
        imageButtonContentReader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity1Main.this, Activity2.class);
                startActivity(intent);
                // add intent to change image
                intent.putExtra("image_id", R.drawable.content);
                startActivity(intent);
            }
        });

        ImageButton imageButtonTextReader = findViewById(R.id.imageButtonTextReader);
        imageButtonTextReader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity1Main.this, Activity2.class);
                startActivity(intent);
                // add intent to change image
                intent.putExtra("image_id", R.drawable.text);
                startActivity(intent);
            }
        });

    }
}