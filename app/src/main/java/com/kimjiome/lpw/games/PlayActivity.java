package com.kimjiome.lpw.games;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class PlayActivity extends AppCompatActivity {

    AppCompatButton colorBtn, numberBtn, colorNumBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        colorBtn = findViewById(R.id.colorMatch);
        numberBtn = findViewById(R.id.numberMatch);
        colorNumBtn = findViewById(R.id.colorNumMatch);

        numberBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PlayActivity.this, NumberGameActivity.class));
            }
        });
    }
}