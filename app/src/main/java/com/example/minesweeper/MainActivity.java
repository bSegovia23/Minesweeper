package com.example.minesweeper;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.GridLayout;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // add cells
        GridLayout grid = findViewById(R.id.gridLayout01);
        LayoutInflater li = LayoutInflater.from(this);

        for (int i = 0; i <= 9; ++i){
            for (int j = 0; j <= 7; ++j) {
                TextView tv = (TextView) li.inflate(R.layout.custom_cell_layout, grid, false);

                tv.setOnClickListener(this::onClickTV);
                GridLayout.LayoutParams lp = (GridLayout.LayoutParams) tv.getLayoutParams();
                lp.rowSpec = GridLayout.spec(i);
                lp.columnSpec = GridLayout.spec(j);

                grid.addView(tv, lp);
            }
        }
    }

    protected void onClickTV(View view){
        System.out.println("Hello world!");
    }
}