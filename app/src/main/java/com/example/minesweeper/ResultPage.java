package com.example.minesweeper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ResultPage extends AppCompatActivity {

    Button btn;
    TextView victory;
    String victoryString;
    String time;
    boolean won;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_page);

        Bundle extras = getIntent().getExtras();
        btn = findViewById(R.id.newGame);
        btn.setOnClickListener(this::newGame);
        victory = findViewById(R.id.victory);
        time = String.valueOf(extras.getInt("time"));
        won = extras.getBoolean("won");
        victoryString = "Used " + time + " secs.\n";
        victoryString += won ? "You won.\nGood job!" : "You lost.\nBetter luck next time!";
        victory.setText(victoryString);
    }
    protected void newGame(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}