package com.example.bicap;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class IndagineActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indagine);
        IndagineHead indagineHead = getIntent().getParcelableExtra("Indagine");
        TextView titoloTextView = findViewById(R.id.titoloIndagineTextView);
        titoloTextView.setText(indagineHead.getTitoloIndagine());
    }
}
