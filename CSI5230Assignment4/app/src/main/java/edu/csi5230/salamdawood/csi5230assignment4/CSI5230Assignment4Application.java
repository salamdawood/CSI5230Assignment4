package edu.csi5230.salamdawood.csi5230assignment4;

import android.app.Application;

/**
 * Created by salamdawood on 11/15/17.
 */

public class CSI5230Assignment4Application extends Application {
    String playerName;
    String opponentName;
    public String getOpponentName() {
        return opponentName;
    }

    public void setOpponentName(String opponentName) {
        this.opponentName = opponentName;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
}
