package edu.csi5230.salamdawood.csi5230assignment4;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView turnLabel = null;
    Button startButton = null;
    TTTButton[] tttButton = new TTTButton[9];
    Player[] players = new Player[2];
    Player currentPlayer = null;
    int currentTurn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        turnLabel = (TextView) findViewById(R.id.player_turn);
        startButton = (Button) findViewById(R.id.start_game);

        tttButton[0] = (TTTButton) findViewById(R.id.button0);
        tttButton[1] = (TTTButton) findViewById(R.id.button1);
        tttButton[2] = (TTTButton) findViewById(R.id.button2);
        tttButton[3] = (TTTButton) findViewById(R.id.button3);
        tttButton[4] = (TTTButton) findViewById(R.id.button4);
        tttButton[5] = (TTTButton) findViewById(R.id.button5);
        tttButton[6] = (TTTButton) findViewById(R.id.button6);
        tttButton[7] = (TTTButton) findViewById(R.id.button7);
        tttButton[8] = (TTTButton) findViewById(R.id.button8);

        init();

    }

    void swapPlayer () {

        if (CheckForWinner()){
            turnLabel.setText(currentPlayer.getPlayerName() + " has won the game.");

            for (int i=0; i < 9 ; i++) {
                tttButton[i].setEnabled(false);
            }
        }
        else if (currentTurn > 8){turnLabel.setText("No Winner");}
        else {
            currentTurn++;
            currentPlayer = currentPlayer == players[0] ? players[1] : players[0];

            turnLabel.setText("Player: " + currentPlayer.getPlayerName());
        }

    }

    public void init() {

        currentPlayer = players[0] = new Player("Michael Jackson", "X");
        players[1] = new Player("James Bond", "O");

        turnLabel.setText("Game is not started yet!!!");

        for (int i = 0; i < 9; i++)
        {
            String buttonId = "button" + i;

            tttButton[i].setIndex(i);
            players[0].cells[i] = new DataCell("");
            players[0].cells[i].addObserver(tttButton[i]);
            players[1].cells[i] = new DataCell("");
            players[1].cells[i].addObserver(tttButton[i]);

            tttButton[i].setEnabled(false);

            tttButton[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TTTButton source = (TTTButton) view;
                    int index = source.getIndex();
                    currentPlayer.cells[index].setValue(currentPlayer.getSymbol());
                    swapPlayer();
                    source.setEnabled(false);
                }
            });
        }
        startButton.setText("Start Game");
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartGame();
            }
        });
    }

    public void StartGame() {
        turnLabel.setText("Player: " + currentPlayer.getPlayerName());

        for (int i=0; i<9; i++){
            //enable buttons and clear any value from last game
            tttButton[i].setText("");
            tttButton[i].setEnabled(true);
            //clear data cell values from previous game played
            players[0].cells[i].setValue("");
            players[1].cells[i].setValue("");

            currentTurn = 1;

        }

    }

    public boolean CheckForWinner() {

        String playerSymbol = currentPlayer.getSymbol();

        if (currentPlayer.cells[0].getValue().equals(playerSymbol) && currentPlayer.cells[1].getValue().equals(playerSymbol) && currentPlayer.cells[2].getValue().equals(playerSymbol))
            return true;
        if (currentPlayer.cells[3].getValue().equals(playerSymbol)&& currentPlayer.cells[4].getValue().equals(playerSymbol) && currentPlayer.cells[5].getValue().equals(playerSymbol))
            return true;
        if (currentPlayer.cells[6].getValue().equals(playerSymbol)&& currentPlayer.cells[7].getValue().equals(playerSymbol) && currentPlayer.cells[8].getValue().equals(playerSymbol))
            return true;
        if (currentPlayer.cells[0].getValue().equals(playerSymbol)&& currentPlayer.cells[3].getValue().equals(playerSymbol) && currentPlayer.cells[6].getValue().equals(playerSymbol))
            return true;
        if (currentPlayer.cells[1].getValue().equals(playerSymbol)&& currentPlayer.cells[4].getValue().equals(playerSymbol) && currentPlayer.cells[7].getValue().equals(playerSymbol))
            return true;
        if (currentPlayer.cells[2].getValue().equals(playerSymbol)&& currentPlayer.cells[5].getValue().equals(playerSymbol) && currentPlayer.cells[8].getValue().equals(playerSymbol))
            return true;
        if (currentPlayer.cells[0].getValue().equals(playerSymbol)&& currentPlayer.cells[4].getValue().equals(playerSymbol) && currentPlayer.cells[8].getValue().equals(playerSymbol))
            return true;
        if (currentPlayer.cells[2].getValue().equals(playerSymbol)&& currentPlayer.cells[4].getValue().equals(playerSymbol) && currentPlayer.cells[6].getValue().equals(playerSymbol))
            return true;

        return false;
    }
}
