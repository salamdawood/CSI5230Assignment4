package edu.csi5230.salamdawood.csi5230assignment4;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.provider.Telephony;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class PlayGame extends AppCompatActivity {
    TextView turnLabel = null;
    Button startButton = null;
    TTTButton[] tttButton = new TTTButton[9];
    Player[] players = new Player[2];
    Player currentPlayer = null;
    int currentTurn;
    Intent otherIntent = null;
    String player1 = null, player2 = null, player1or2 = null;
    String number = null;
    IntentFilter filter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
    BroadcastReceiver br = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_game);

        turnLabel = (TextView) findViewById(R.id.player_turn);
        startButton = (Button) findViewById(R.id.start_game);

        otherIntent = getIntent();

        player1or2 = otherIntent.getStringExtra("player1or2");
        if (player1or2.equalsIgnoreCase("player1")){
            player1 = otherIntent.getStringExtra("playerName");
            player2 = otherIntent.getStringExtra("opponentName");
        }
        else
        {
            player2 = otherIntent.getStringExtra("playerName");
            player1 = otherIntent.getStringExtra("opponentName");
        }
        number = otherIntent.getStringExtra("number");

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

        br = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                    SmsMessage[] messages = Telephony.Sms.Intents.getMessagesFromIntent(intent);
                    for (SmsMessage m: messages) {
                        //String number = m.getDisplayOriginatingAddress();
                        String text = m.getDisplayMessageBody();
                        number = m.getDisplayOriginatingAddress();
                        if (text.startsWith("$$$$ ")){
                            String [] splitText = text.split("!");

//                            Toast toast = Toast.makeText(context , splitText[3], Toast.LENGTH_LONG);
//                            toast.show();
                            if (splitText[2].trim().equalsIgnoreCase("selected")){
                                int index = Integer.parseInt(splitText[3].trim());
                                selectionSMS(index);
                                if (CheckForWinner()){
                                    turnLabel.setText(currentPlayer.getPlayerName() + " has won the game.");

                                    for (int i=0; i < 9 ; i++) {
                                        tttButton[i].setEnabled(false);
                                    }
                                }
                                else{
                                    enableButtonsWithoutSymbols();
                                    currentPlayer = currentPlayer == players[0] ? players[1] : players[0];

                                    turnLabel.setText("Player: " + currentPlayer.getPlayerName());
                                }
                            }
                            else if (splitText[2].trim().equalsIgnoreCase("gameover")){

                            }
                        }
                    }
                }
            }
        };
        registerReceiver(br, filter);
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
            for (int i=0; i < 9 ; i++) {
                tttButton[i].setEnabled(false);
            }
        }

    }

    public void init() {

        currentPlayer = players[0] = new Player(player1, "X");
        players[1] = new Player(player2, "O");

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
                    sendSelectionSMS(index);
                    swapPlayer();
                    source.setEnabled(false);
                }
            });
        }

        StartGame();
    }

    public void StartGame() {
        turnLabel.setText("Player: " + player1);

        for (int i=0; i<9; i++){
            //enable buttons and clear any value from last game
            tttButton[i].setText("");
            if (player1or2.equalsIgnoreCase("player1")) {
                tttButton[i].setEnabled(true);
            }
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

    public void sendSelectionSMS (int index){
        String msg = "$$$$ ! TTTGAME ! SELECTED ! " + index;
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(number, null, msg, null, null);
    }

    public void selectionSMS(int index) {
        currentPlayer.cells[index].setValue(currentPlayer.getSymbol());
        if (CheckForWinner()){
            turnLabel.setText(currentPlayer.getPlayerName() + " has won the game.");

            for (int i=0; i < 9 ; i++) {
                tttButton[i].setEnabled(false);
            }
        }
        tttButton[index].setEnabled(false);
    }

    public void enableButtonsWithoutSymbols(){
        for (int i=0; i < 9 ; i++) {
            if (players[0].cells[i].getValue() == "" || players[1].cells[i].getValue() == ""){
                tttButton[i].setEnabled(true);
            }
        }
    }
}
