package com.killi8n.cardgame.cardgame;

public class CardGameThread extends Thread {
    CardGameView m_view;

    public CardGameThread(CardGameView m_view) {
        this.m_view = m_view;
    }

    @Override
    public void run() {
        while(true) {
            m_view.checkMatch();
        }
    }
}
