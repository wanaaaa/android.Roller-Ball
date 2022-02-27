package com.zybooks.rollerball;

import java.util.List;

public class VariableSingle {
    private static VariableSingle instance;
    VariableSingle() {};
    public boolean hitOrNot = false;
    public long timeLong;
    public ScoreSQL scoreSQL;
    public boolean newGame = true;
    public boolean superAbility = false;

    public static VariableSingle getInstance() {
        if(instance == null) {
            instance = new VariableSingle();
        }
        return instance;
    }
}
