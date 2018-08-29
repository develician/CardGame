package com.killi8n.cardgame.cardgame;

public class Card extends Thread {
    public static final int CARD_SHOW = 0;
    public static final int CARD_CLOSE = 1;
    public static final int CARD_PLAYEROPEN = 2;
    public static final int CARD_MATCHED = 3;

    public static final int IMG_RED = 1;
    public static final int IMG_GREEN = 2;
    public static final int IMG_BLUE = 3;

    public int m_color;
    public int m_state;

    public Card(int color) {
        m_state = CARD_SHOW;
        m_color = color;
    }


}
