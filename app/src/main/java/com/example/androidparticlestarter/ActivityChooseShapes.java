package com.example.androidparticlestarter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Random;

import android.view.View;
import android.widget.Button;

public class ActivityChooseShapes extends AppCompatActivity {

    Button btnRandom;
    Button btnActivity1;
    Button btnActivity2;

    Random rnd;
    int i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_shapes);

        this.btnActivity1 = findViewById(R.id.buttonAc1);
        this.btnActivity2 = findViewById(R.id.buttonAc2);
        this.btnRandom = findViewById(R.id.buttonRandomActivities);

        this.rnd = new Random();
    }


    public void switchToActivityOne(View view){
        Intent myIntent = new Intent(getBaseContext(),   MainActivity.class);
        startActivity(myIntent);
    }

    public void switchToActivityTwo(View view){
        Intent myIntent = new Intent(getBaseContext(),   Main2Activity.class);
        startActivity(myIntent);

    }

    public void switchRamdom(View view){
        this.i = rnd.nextInt(2);
        if (this.i == 0){
            Intent myIntent = new Intent(getBaseContext(),   MainActivity.class);
            startActivity(myIntent);
        }
        else{
            Intent myIntent = new Intent(getBaseContext(),   Main2Activity.class);
            startActivity(myIntent);
        }
    }
}
