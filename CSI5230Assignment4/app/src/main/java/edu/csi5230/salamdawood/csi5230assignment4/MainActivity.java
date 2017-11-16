package edu.csi5230.salamdawood.csi5230assignment4;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.provider.Telephony;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static android.R.attr.data;

public class MainActivity extends AppCompatActivity {
    IntentFilter filter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");

    Button startButton = null;
    EditText playerName = null, requestNumber = null;
    BroadcastReceiver br = null;

    AlertDialog dialog = null;
    String requestorNumber = null;
    String opponentName = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playerName = (EditText) findViewById(R.id.playerNameEditText);
        requestNumber = (EditText) findViewById(R.id.numberEditText);
        startButton = (Button) findViewById(R.id.startGamebutton);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = "$$$$ ! TTTGAME ! INVITE ! " + playerName.getText().toString();
                String number = requestNumber.getText().toString();
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(number, null, msg, null, null);
            }
        });

        br = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                    SmsMessage[] messages = Telephony.Sms.Intents.getMessagesFromIntent(intent);
                    for (SmsMessage m: messages) {
                        //String number = m.getDisplayOriginatingAddress();
                        String text = m.getDisplayMessageBody();
                        requestorNumber = m.getDisplayOriginatingAddress();
                        if (text.startsWith("$$$$ ")){
                            String [] splitText = text.split("!");

//                            Toast toast = Toast.makeText(context , splitText[3], Toast.LENGTH_LONG);
//                            toast.show();
                            if (splitText[2].trim().equalsIgnoreCase("invite")){
                                opponentName = splitText[3].trim();
                                dialog.show();
                            }
                            else if (splitText[2].trim().equalsIgnoreCase("accepted")){
                                opponentName = splitText[3].trim();

                                //start game
                                Intent intentGame = new Intent(MainActivity.this, PlayGame.class);
                                intentGame.putExtra("playerName", playerName.getText().toString());
                                intentGame.putExtra("opponentName", opponentName);
                                intentGame.putExtra("player1or2", "player1");
                                intentGame.putExtra("number", requestorNumber);
                                intentGame.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                intentGame.setFlags(PendingIntent.FLAG_UPDATE_CURRENT);
                                startActivity(intentGame);

                                Toast toast = Toast.makeText(context , splitText[2], Toast.LENGTH_LONG);
                                toast.show();
                                toast = Toast.makeText(context, opponentName, Toast.LENGTH_LONG);
                                toast.show();
                            }
                            else if (splitText[2].trim().equalsIgnoreCase("declined")){
                                Toast toast = Toast.makeText(context , splitText[2], Toast.LENGTH_LONG);
                                toast.show();
                            }
                        }
                    }
                }
            }
        };
        registerReceiver(br, filter);

        // 1. Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // 2. Chain together various setter methods to set the dialog characteristics
        builder.setMessage("Do you want to play?")
                .setTitle("Play Game");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //send accepting message to other number
                String msg = "$$$$ ! TTTGAME ! ACCEPTED ! " + playerName.getText().toString();
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(requestorNumber, null, msg, null, null);

                //start game
                Intent intent = new Intent(MainActivity.this, PlayGame.class);
                intent.putExtra("playerName", playerName.getText().toString());
                intent.putExtra("opponentName", opponentName);
                intent.putExtra("player1or2", "player2");
                intent.putExtra("number", requestorNumber);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent.setFlags(PendingIntent.FLAG_UPDATE_CURRENT);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //send decline message to other number
                String msg = "$$$$ ! TTTGAME ! DECLINED ! " + playerName.getText().toString();
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(requestorNumber, null, msg, null, null);
            }
        });

        // 3. Get the AlertDialog from create()
        dialog = builder.create();
    }


}
