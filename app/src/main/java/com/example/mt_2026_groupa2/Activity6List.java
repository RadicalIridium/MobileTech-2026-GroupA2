package com.example.mt_2026_groupa2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class Activity6List extends AppCompatActivity {

    private ListView listViewResults;
    private Button buttonAdd;

    private ArrayList<String> displayList;
    private ArrayList<HashMap<String, String>> dataList;
    private ArrayAdapter<String> adapter;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_6list);

        listViewResults = findViewById(R.id.listViewResults);
        buttonAdd = findViewById(R.id.buttonAdd);

        displayList = new ArrayList<>();
        dataList = new ArrayList<>();

        adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                displayList
        );

        listViewResults.setAdapter(adapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("MLKitResults");

        loadDataFromFirebase();

        buttonAdd.setOnClickListener(v -> {
            Intent intent = new Intent(Activity6List.this, Activity1Main.class);
            startActivity(intent);
            finish();
        });

        listViewResults.setOnItemClickListener((parent, view, position, id) -> {
            HashMap<String, String> selectedItem = dataList.get(position);

            Intent intent = new Intent(Activity6List.this, Activity7Detail.class);

            intent.putExtra("id", selectedItem.get("id"));
            intent.putExtra("reader", selectedItem.get("reader"));
            intent.putExtra("result", selectedItem.get("result"));
            intent.putExtra("imageUri", selectedItem.get("imageUri"));

            startActivity(intent);
        });
    }

    private void loadDataFromFirebase() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                displayList.clear();
                dataList.clear();

                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    HashMap<String, String> item =
                            (HashMap<String, String>) itemSnapshot.getValue();

                    if (item != null) {
                        dataList.add(item);

                        String reader = item.get("reader");
                        String result = item.get("result");

                        if (result != null && result.length() > 40) {
                            result = result.substring(0, 40) + "...";
                        }

                        displayList.add(reader + "\n" + result);
                    }
                }

                adapter.notifyDataSetChanged();

                if (displayList.isEmpty()) {
                    Toast.makeText(Activity6List.this, "No saved results yet", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(
                        Activity6List.this,
                        "Failed to load data: " + error.getMessage(),
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }
}