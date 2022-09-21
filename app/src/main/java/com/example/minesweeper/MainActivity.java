package com.example.minesweeper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.GridLayout;
import android.view.View;
import android.widget.TextView;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    final String CLOCK = "\uD83D\uDD53";
    final String FLAG = "\uD83D\uDEA9";
    final String MINE = "\uD83D\uDCA3";
    final String PICK = "\u26CF";

    final int MAX_BOMBS = 4;
    final int NUM_COLS = 8;
    final int NUM_ROWS = 10;

    TextView flagsView;
    TextView timeView;

    Integer flags;
    Integer time;
    boolean flagging;
    Random rand;

    boolean[][] bombs = new boolean[NUM_ROWS][NUM_COLS]; // bomb or no?
    boolean[][] flagged = new boolean[NUM_ROWS][NUM_COLS];
    boolean[][] revealed = new boolean[NUM_ROWS][NUM_COLS];
    int[][] adjs = new int[NUM_ROWS][NUM_COLS]; // how many adjacent bombs?
    TextView[][] tiles = new TextView[NUM_ROWS][NUM_COLS];

    int revealedNum;

    boolean gameOver;
    boolean playerWon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // register counters
        flagsView = findViewById(R.id.flagsView);
        timeView = findViewById(R.id.timeView);

        flags = MAX_BOMBS;
        time = 0;
        flagging = false;

        flagsView.setText(flags.toString());
        timeView.setText(time.toString());

        rand = new Random();

        Integer num_bombs = 0;
        Integer rand_i = 0;
        Integer rand_j = 0;
        int revealedNum = 0;

        // fill in bombs
        while(num_bombs != 4){
            rand_i = rand.nextInt(NUM_ROWS);
            rand_j = rand.nextInt(NUM_COLS);
            if(bombs[rand_i][rand_j] == false){
                bombs[rand_i][rand_j] = true;
                num_bombs++;
            }
        }
        // fill in adjacents
        for(int i = 0; i < NUM_ROWS; ++i){
            for(int j = 0; j < NUM_COLS; ++j){
                if(bombs[i][j] == false){
                    int sum = 0;
                    for(int x = -1; x <= 1; ++x){
                        for(int y = -1; y <= 1; ++y){
                            sum += getBomb(i+x,j+y);
                        }
                    }
                    adjs[i][j] = sum;
                }
            }
        }


        // add cells
        GridLayout grid = findViewById(R.id.gridLayout01);
        LayoutInflater li = LayoutInflater.from(this);

        for (int i = 0; i < NUM_ROWS; ++i){
            for (int j = 0; j < NUM_COLS; ++j) {
                TextView tv = (TextView) li.inflate(R.layout.custom_cell_layout, grid, false);

                int finalI = i, finalJ = j;
                tv.setOnClickListener(v -> onClickTile(tv, finalI, finalJ));
                tiles[i][j] = tv;


                GridLayout.LayoutParams lp = (GridLayout.LayoutParams) tv.getLayoutParams();
                lp.rowSpec = GridLayout.spec(i);
                lp.columnSpec = GridLayout.spec(j);

                grid.addView(tv, lp);
            }
        }

        // set up button
        Button mode = findViewById(R.id.mode);
        mode.setOnClickListener(this::onClickPick);

        // start the timer!
        runTimer();
    }

    protected void onClickTile(TextView tv, Integer i, Integer j){
        if(gameOver) {
            Intent intent = new Intent(this, ResultPage.class);
            intent.putExtra("time", time);
            intent.putExtra("won", playerWon);
            startActivity(intent);
        }
        else{
            if(flagging) {
                if(!flagged[i][j] && flags > 0){
                    flagged[i][j] = true;
                    tv.setText(FLAG);
                    flags--;
                    flagsView.setText(flags.toString());
                }
                else if(flagged[i][j]){
                    flagged[i][j] = false;
                    tv.setText("");
                    flags++;
                    flagsView.setText(flags.toString());
                }
            }
            else if(!flagged[i][j]){
                reveal(i,j);
            }
            if(revealedNum >= (NUM_ROWS*NUM_COLS)-MAX_BOMBS){
                gameOver = true;
                playerWon = true;
            }
        }
    }

    protected void onClickPick(View view){
        Button btn = (Button) view;
        flagging = !flagging;
        if(flagging){
            btn.setText(FLAG);
        }
        else{
            btn.setText(PICK);
        }
    }

    private void runTimer(){
        final TextView timeView = (TextView) findViewById(R.id.timeView);
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                timeView.setText(time.toString());
                if(!gameOver){
                    time++;
                }
                handler.postDelayed(this, 1000);
            }
        });
    }

    protected int getBomb(int i, int j){
        if(i < 0 || i >= NUM_ROWS || j < 0 || j >= NUM_COLS){
            return 0;
        }
        else{
            return bombs[i][j] ? 1 : 0;
        }
    }

    protected void reveal(int i, int j){
        if(i < 0 || i >= NUM_ROWS || j < 0 || j >= NUM_COLS || revealed[i][j]){
            // do nothing
        }
        else {
            revealed[i][j] = true;
            TextView tv = tiles[i][j];
            if (bombs[i][j]) {
                tv.setBackgroundColor(Color.LTGRAY);
                tv.setText(MINE);
                if (!gameOver) {
                    gameOver = true;
                    for (int x = 0; x < NUM_ROWS; ++x) {
                        for (int y = 0; y < NUM_COLS; ++y) {
                            reveal(x, y);
                        }
                    }
                }
            } else {
                int adj = adjs[i][j];
                tv.setText(((Integer) adj).toString());
                tv.setBackgroundColor(Color.LTGRAY);
                if(!gameOver){
                    revealedNum++; // don't increment when the player has lost
                }
                if (adj == 0) {
                    for(int x = -1; x <= 1; ++x){
                        for(int y = -1; y <= 1; ++y){
                            reveal(i+x, j+y);
                        }
                    }
                }
            }
        }
    }
}