package com.afss.milocr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class selectionActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);


    }

    public void enBn(View view)
    {
        Intent enBn = new Intent(this, enBn.class);
        startActivity(enBn);
    }

    public void en(View view)
    {
        Intent en = new Intent(this, MainActivity.class);
        startActivity(en);
    }
}
