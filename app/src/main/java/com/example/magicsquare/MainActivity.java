package com.example.magicsquare;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.content.Intent;

public class MainActivity extends AppCompatActivity implements OnClickListener {
    Button mButton3x3;
    Button mButton4x4;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mButton3x3 = (Button) findViewById(R.id.btn_3x3);
        mButton3x3.setOnClickListener(this);

        mButton4x4 = (Button) findViewById(R.id.btn_4x4);
        mButton4x4.setOnClickListener(this);
    }

    public void onClick(View v) {
        if (v == mButton3x3) {
            Intent intent = new Intent(this, Game3Activity.class);
            startActivity(intent);
            finish();
        } else if (v == mButton4x4) {
            Intent intent = new Intent(this, Game4Activity.class);
            startActivity(intent);
            finish();
        }
    }
}
