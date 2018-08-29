package com.killi8n.cardgame.cardgame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;

public class CardGameView extends View {
    Bitmap m_backgroundImage;
    Bitmap m_card_backside;
    Bitmap m_card_red, m_card_blue, m_card_green;

    private Card m_SelectCard_1 = null;
    private Card m_SelectCard_2 = null;

    public static final int STATE_READY =0 ; // ���ӽ����� �غ����
    public static final int STATE_GAME=1 ; // ������
    public static final int STATE_END =2 ; // ��������

    private int m_state = STATE_READY;

    Card m_Shuffle[][];



    public CardGameView(Context context) {
        super(context);
        m_backgroundImage = BitmapFactory.decodeResource(getResources(), R.drawable.background);
        m_card_backside = BitmapFactory.decodeResource(getResources(), R.drawable.backside);

        m_card_red = BitmapFactory.decodeResource(getResources(), R.drawable.front_red);
        m_card_blue = BitmapFactory.decodeResource(getResources(), R.drawable.front_blue);
        m_card_green = BitmapFactory.decodeResource(getResources(), R.drawable.front_green);

        m_Shuffle = new Card[3][2] ;
        m_Shuffle[0][0]= new Card(Card.IMG_RED);

        SetCardShuffle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Rect dest = new Rect(0, 0, getWidth(), getHeight());

        int marginLeft = 10;
        int marginBottom= 50;
        int cardHeight = m_card_backside.getHeight();
        int cardWidth = m_card_backside.getWidth();

        canvas.drawBitmap(m_backgroundImage, null, dest, null);
        for (int y = 0; y < 2; y++) {
            for(int x = 0;x < 3;x++) {
                switch (m_Shuffle[x][y].m_state) {
                    case Card.CARD_SHOW:
                    case Card.CARD_MATCHED:
                    case Card.CARD_PLAYEROPEN:
                        if (m_Shuffle[x][y].m_color == Card.IMG_RED) {
                            canvas.drawBitmap(m_card_red, marginLeft + x * ((dest.width() + cardWidth)  - marginLeft * 2) / 3,
                                    150 + y * (cardHeight + marginBottom), null);
                        } else if (m_Shuffle[x][y].m_color == Card.IMG_GREEN) {
                            canvas.drawBitmap(m_card_green, marginLeft + x * ((dest.width() + cardWidth)  - marginLeft * 2) / 3,
                                    150 + y * (cardHeight + marginBottom), null);
                        } else if (m_Shuffle[x][y].m_color == Card.IMG_BLUE) {
                            canvas.drawBitmap(m_card_blue, marginLeft + x * ((dest.width() + cardWidth)  - marginLeft * 2) / 3,
                                    150 + y * (cardHeight + marginBottom), null);
                        }
                        break;
                    case Card.CARD_CLOSE:
                        canvas.drawBitmap(m_card_backside, marginLeft + x * ((dest.width() + cardWidth)  - marginLeft * 2) / 3,
                                150 + y * (cardHeight + marginBottom), null);
                        break;
                }
            }
        }
    }


    public void SetCardShuffle()
    {
        //������ ���� ���� ī����� ����
        m_Shuffle[0][0]= new Card(Card.IMG_RED);
        m_Shuffle[0][1]= new Card(Card.IMG_BLUE);
        m_Shuffle[1][0]= new Card(Card.IMG_GREEN);
        m_Shuffle[1][1]= new Card(Card.IMG_GREEN);
        m_Shuffle[2][0]= new Card(Card.IMG_BLUE);
        m_Shuffle[2][1]= new Card(Card.IMG_RED);
    }

    public void startGame() {
        m_Shuffle[0][0].m_state =Card.CARD_CLOSE ;
        m_Shuffle[0][1].m_state =Card.CARD_CLOSE ;
        m_Shuffle[1][0].m_state =Card.CARD_CLOSE ;
        m_Shuffle[1][1].m_state =Card.CARD_CLOSE ;
        m_Shuffle[2][0].m_state =Card.CARD_CLOSE ;
        m_Shuffle[2][1].m_state =Card.CARD_CLOSE ;

        invalidate();
    }

    public void checkMatch() {
        if(m_SelectCard_1 == null || m_SelectCard_2 == null)
            return;

        if(m_SelectCard_1.m_color == m_SelectCard_2.m_color){
            // �� ī���� ������ ������� ��ī�带 ���߾������·� �ٲپ��ش�
            m_SelectCard_1.m_state = Card.CARD_MATCHED;
            m_SelectCard_2.m_state = Card.CARD_MATCHED;
            // �ٽ� ������ �Ҽ��ְ� null���� �־��ش�.
            m_SelectCard_1 = null;
            m_SelectCard_2 = null;
        } else {
            try
            {
                Thread.sleep(500);
            }
            catch (InterruptedException e)
            {
            }

            m_SelectCard_1.m_state = Card.CARD_CLOSE;
            m_SelectCard_2.m_state = Card.CARD_CLOSE;

            m_SelectCard_1 = null;
            m_SelectCard_2 = null;
        }

        postInvalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        Rect dest = new Rect(0, 0, getWidth(), getHeight());

        int marginLeft = 10;
        int marginBottom= 50;
        int cardHeight = m_card_backside.getHeight();
        int cardWidth = m_card_backside.getWidth();

        switch (this.m_state) {
            case STATE_READY:
                startGame();
                m_state = STATE_GAME;
                break;
            case STATE_GAME:
                if (m_SelectCard_1 != null && m_SelectCard_2 != null) {
                    return true;
                }
                int px = (int) event.getX();
                int py = (int) event.getY();
                for(int y = 0;y<2;y++) {
                    for(int x = 0; x < 3; x++){
                        Rect cardRect = new Rect(marginLeft + x * ((dest.width() + cardWidth)  - marginLeft * 2) / 3, 150 + y * (cardHeight + marginBottom),
                                marginLeft + x * ((dest.width() + cardWidth)  - marginLeft * 2) / 3 + cardWidth, 150 + y * (cardHeight + marginBottom) + cardHeight);
                        if(cardRect.contains(px, py)) {
                            if (m_Shuffle[x][y].m_state != Card.CARD_MATCHED) {
                                if(m_SelectCard_1 == null) {
                                    m_SelectCard_1 = m_Shuffle[x][y];
                                    m_SelectCard_1.m_state = Card.CARD_PLAYEROPEN;
                                    m_Shuffle[x][y].m_state = Card.CARD_PLAYEROPEN ; //���õ� ī�带 ������ �ݴϴ�.
                                } else {
                                    if(m_SelectCard_1 != m_Shuffle[x][y])
                                    { // �ߺ����������
                                        m_SelectCard_2 = m_Shuffle[x][y];
                                        m_SelectCard_2.m_state = Card.CARD_PLAYEROPEN;
                                    }
                                }
                            }
                        }
                    }
                }
                break;
            case STATE_END:
                m_state = STATE_READY;
                break;
        }


        invalidate();
        return super.onTouchEvent(event);
    }

}
