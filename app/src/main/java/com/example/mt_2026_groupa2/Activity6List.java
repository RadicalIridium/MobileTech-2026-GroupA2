package com.example.mt_2026_groupa2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
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

    private ArrayList<HashMap<String, String>> dataList;

    private ResultAdapter adapter;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_6list);

        listViewResults =
                findViewById(R.id.listViewResults);

        buttonAdd =
                findViewById(R.id.buttonAdd);

        dataList = new ArrayList<>();

        adapter = new ResultAdapter();

        listViewResults.setAdapter(adapter);

        databaseReference =
                FirebaseDatabase
                        .getInstance()
                        .getReference("MLKitResults");

        loadDataFromFirebase();

        buttonAdd.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            Activity6List.this,
                            Activity1Main.class
                    );

            startActivity(intent);

            finish();
        });

        listViewResults.setOnItemClickListener(
                (parent, view, position, id) -> {

                    HashMap<String, String> selectedItem =
                            dataList.get(position);

                    Intent intent =
                            new Intent(
                                    Activity6List.this,
                                    Activity7Detail.class
                            );

                    intent.putExtra(
                            "id",
                            selectedItem.get("id")
                    );

                    intent.putExtra(
                            "reader",
                            selectedItem.get("reader")
                    );

                    intent.putExtra(
                            "result",
                            selectedItem.get("result")
                    );

                    intent.putExtra(
                            "imageUri",
                            selectedItem.get("imageUri")
                    );

                    startActivity(intent);
                });
    }

    private void loadDataFromFirebase() {

        databaseReference.addValueEventListener(
                new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot snapshot) {

                        dataList.clear();

                        for (DataSnapshot itemSnapshot :
                                snapshot.getChildren()) {

                            try {

                                String id =
                                        itemSnapshot
                                                .child("id")
                                                .getValue(String.class);

                                String reader =
                                        itemSnapshot
                                                .child("reader")
                                                .getValue(String.class);

                                String result =
                                        itemSnapshot
                                                .child("result")
                                                .getValue(String.class);

                                String imageUri =
                                        itemSnapshot
                                                .child("imageUri")
                                                .getValue(String.class);

                                HashMap<String, String> item =
                                        new HashMap<>();

                                item.put("id", id);

                                item.put("reader", reader);

                                item.put("result", result);

                                item.put("imageUri", imageUri);

                                dataList.add(item);

                            } catch (Exception e) {

                                e.printStackTrace();
                            }
                        }

                        adapter.notifyDataSetChanged();

                        if (dataList.isEmpty()) {

                            Toast.makeText(
                                    Activity6List.this,
                                    "No saved results yet",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {

                        Toast.makeText(
                                Activity6List.this,
                                "Failed to load data: "
                                        + error.getMessage(),
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                });
    }

    private class ResultAdapter extends BaseAdapter {

        @Override
        public int getCount() {

            return dataList.size();
        }

        @Override
        public Object getItem(int position) {

            return dataList.get(position);
        }

        @Override
        public long getItemId(int position) {

            return position;
        }

        @Override
        public View getView(
                int position,
                View convertView,
                ViewGroup parent
        ) {

            if (convertView == null) {

                convertView =
                        getLayoutInflater().inflate(
                                R.layout.list_item_result,
                                parent,
                                false
                        );
            }

            ImageView imageViewListItem =
                    convertView.findViewById(
                            R.id.imageViewListItem
                    );

            TextView textViewListItem =
                    convertView.findViewById(
                            R.id.textViewListItem
                    );

            HashMap<String, String> item =
                    dataList.get(position);

            String reader =
                    item.get("reader");

            String result =
                    item.get("result");

            String imageUri =
                    item.get("imageUri");

            if (reader == null ||
                    reader.isEmpty()) {

                reader = "Unknown Reader";
            }

            if (result == null) {

                result = "";
            }

            if (result.length() > 35) {

                result =
                        result.substring(0, 35)
                                + "...";
            }

            textViewListItem.setText(
                    reader + "\n" + result
            );

            imageViewListItem.setImageResource(
                    android.R.color.transparent
            );

            if (imageUri != null &&
                    !imageUri.isEmpty()) {

                try {

                    Uri uri = Uri.parse(imageUri);

                    imageViewListItem.post(() -> {

                        try {

                            imageViewListItem.setImageURI(uri);

                        } catch (SecurityException e) {

                            imageViewListItem.setImageResource(
                                    android.R.color.transparent
                            );

                        } catch (Exception e) {

                            imageViewListItem.setImageResource(
                                    android.R.color.transparent
                            );
                        }
                    });

                } catch (Exception e) {

                    imageViewListItem.setImageResource(
                            android.R.color.transparent
                    );
                }
            }

            return convertView;
        }
    }
}