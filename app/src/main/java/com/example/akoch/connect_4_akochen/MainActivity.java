package com.example.akoch.connect_4_akochen;


import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.Shape;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private GridLayout gameGrid, buttonGrid;
    private RelativeLayout main;
    private Button restart;
    private int colCount, rowCount, moveCounter;
    private String[][] board;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        main = new RelativeLayout(this);
        main.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        setContentView(main);
        //Fill gamegrid with empty holes
        createGUI();
        emptyBoard();
    }

    private void createGUI(){
        //setup gameGrid
        gameGrid = new GridLayout(this);
        colCount = 7;
        rowCount = 6;
        gameGrid.setColumnCount(colCount);
        gameGrid.setRowCount(rowCount);
        gameGrid.setBackground(ContextCompat.getDrawable(this,R.color.board));
        gameGrid.setOrientation(GridLayout.VERTICAL);
        RelativeLayout.LayoutParams gamePos = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        gameGrid.setLayoutParams(gamePos);
        gameGrid.setId(View.generateViewId());
        main.addView(gameGrid);
        buttonGrid = new GridLayout(this);
        buttonGrid.setRowCount(1);
        buttonGrid.setColumnCount(colCount);
        RelativeLayout.LayoutParams buttonPos = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        buttonPos.addRule(RelativeLayout.BELOW, gameGrid.getId());
        buttonGrid.setLayoutParams(buttonPos);
        main.addView(buttonGrid);
        //Create restart button
        restart = new Button(this);
        restart.setText("Restart");
        restart.setGravity(Gravity.CENTER);
        restart.setPadding(0,50,0,50);
        restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                restart();
            }
        });
        RelativeLayout.LayoutParams restartPos = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        restartPos.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        restart.setLayoutParams(restartPos);
        main.addView(restart);
        //create board for value comparison
        board = new String[colCount][rowCount];

    }

    private void emptyBoard(){
        TextView hole;
        GridLayout.LayoutParams p1;
        moveCounter = 0;
        gameGrid.removeAllViews();
        buttonGrid.removeAllViews();
        //Create board
        for(int c = 0; c < colCount; c++){
            for(int r = 0; r < rowCount; r++){
                hole = new TextView(this);
                hole.setBackground(ContextCompat.getDrawable(this,R.drawable.empty_hole));
                p1 = new GridLayout.LayoutParams();
                p1.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, GridLayout.CENTER, 1);
                p1.rowSpec = GridLayout.spec(GridLayout.UNDEFINED, GridLayout.CENTER, 1);
                hole.setLayoutParams(p1);
                ViewGroup.MarginLayoutParams p2 = (ViewGroup.MarginLayoutParams) hole.getLayoutParams();
                p2.setMargins(0,5,0,5);
                hole.setLayoutParams(p2);
                gameGrid.addView(hole, (c*6) + r);
            }
        }
        //Create buttons to drop pieces
        Button dropButton;
        GridLayout.LayoutParams pButton;
        for(int i = 0; i < colCount; i++){
            pButton = new GridLayout.LayoutParams();
            pButton.columnSpec = GridLayout.spec(i, GridLayout.CENTER, 1);
            pButton.rowSpec = GridLayout.spec(0, GridLayout.CENTER, 1);
            //
            dropButton = new Button(this);
            dropButton.setPadding(0,0,0,0);
            dropButton.setMinimumWidth(0);
            dropButton.setMinimumHeight(0);
            dropButton.setText("▼");
            dropButton.setWidth(100);
            dropButton.setHeight(100);
            dropButton.setOnClickListener(new PutAPiece(i));
            dropButton.setLayoutParams(pButton);
            buttonGrid.addView(dropButton);
        }
        for(int c = 0; c < colCount; c++) {
            for (int r = 0; r < rowCount; r++) {
                board[c][r] = "hole";
            }
        }
    }

    private Drawable getPieceColor(){
        if(moveCounter % 2 == 0){
            return ContextCompat.getDrawable(this,R.drawable.red_game_piece);
        }else{
            return ContextCompat.getDrawable(this,R.drawable.yellow_game_piece);
        }
    }

    private String getCurrPlayer() {
        if (moveCounter % 2 == 0) {
            return "Red";
        } else {
            return "Yellow";
        }
    }
    public void restart(){
        emptyBoard();
    }

    private boolean checkHorWin(int row){
        int counter = 0;
        for(int i = 0; i < colCount; i++){
            if(board[i][row].equals(getCurrPlayer())){
                counter++;
                if(counter == 4){
                    break;
                }
            } else{
                counter = 0;
            }
        }
        if(counter == 4){
            return true;
        } else {
            return false;
        }
    }

    private boolean checkVertWin(int col){
        int counter = 0;
        for(int i = 0; i < rowCount; i++){
            if(board[col][i].equals(getCurrPlayer())){
                counter++;
                if(counter == 4){
                    break;
                }
            } else{
                counter = 0;
            }
        }
        if(counter == 4){
            return true;
        } else {
            return false;
        }
    }

    private boolean checkDescDiagWin(int col, int row){
        int xCheck = 0;
        int yCheck = 0;
        int counter = 0;
        for(int x = col, y = row; x >= 0 && y >= 0; x--, y--){
            xCheck = x;
            yCheck = y;
        }
        for(int x = xCheck, y = yCheck; x < colCount && y < rowCount; x++, y++){
            if(board[x][y].equals(getCurrPlayer())){
                counter++;
                if(counter == 4){
                    break;
                }
            } else{
                counter = 0;
            }
        }
        if(counter == 4){
            return true;
        } else {
            return false;
        }
    }

    private boolean checkAscDiagWin(int col, int row){
        int xCheck = 0;
        int yCheck = 0;
        int counter = 0;
        for(int x = col, y = row; x < colCount && y >= 0; x++, y--){
            xCheck = x;
            yCheck = y;
        }
        for(int x = xCheck, y = yCheck; x >= 0 && y < rowCount; x--, y++){
            if(board[x][y].equals(getCurrPlayer())){
                counter++;
                if(counter == 4){
                    break;
                }
            } else{
                counter = 0;
            }
        }
        if(counter == 4){
            return true;
        } else {
            return false;
        }
    }

    private class PutAPiece implements View.OnClickListener {
        private int col;
        private int row;
        PutAPiece(int colNum){
            col = colNum;
            row = 5;
        }

        @Override
        public void onClick(View view) {
            if(row >= 0) {
                //set up piece
                TextView piece = new TextView(MainActivity.this);
                //piece.setLayoutParams();
                piece.setBackground(getPieceColor());
                GridLayout.LayoutParams p1 = new GridLayout.LayoutParams();
                p1.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, GridLayout.CENTER, 1);
                p1.rowSpec = GridLayout.spec(GridLayout.UNDEFINED, GridLayout.CENTER, 1);
                piece.setLayoutParams(p1);
                ViewGroup.MarginLayoutParams p2 = (ViewGroup.MarginLayoutParams) piece.getLayoutParams();
                p2.setMargins(0,5,0,5);
                piece.setLayoutParams(p2);
                //insert new piece
                int pos = (col * 6) + row;
                gameGrid.removeViewAt(pos);
                gameGrid.addView(piece, pos);
                board[col][row] = getCurrPlayer();
                row--;
                //Check victory conditions
                if(checkHorWin(row + 1) || checkVertWin(col) || checkDescDiagWin(col, row + 1) || checkAscDiagWin(col, row + 1)){
                    AlertDialog.Builder winner = new AlertDialog.Builder(MainActivity.this);
                    winner.setTitle(getCurrPlayer() + "victory!");
                    winner.setMessage("Victory for player " + getCurrPlayer().replaceAll("R", "r"));
                    winner.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {emptyBoard();}
                    });
                    winner.show();
                }
                //Increase move counter
                moveCounter++;
            }
        }
    }
}
