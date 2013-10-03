package com.bdjamealapp.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.bdjamealapp.R;
import com.fima.cardsui.objects.Card;

import java.util.Random;

public class MyCard extends Card {

    private int color;
    static private final int resId[] = {R.color.red, R.color.orange, R.color.green, R.color.blue, R.color.purple};

    public MyCard(String title) {
        super(title);
        Random r = new Random(System.currentTimeMillis());
        color = resId[r.nextInt(5)];
    }

    @Override
    public View getCardContent(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_im, null);
        ((TextView) view.findViewById(R.id.title)).setText(title);
        ((ImageView) view.findViewById(R.id.img)).setImageResource(color);
        ((TextView) view.findViewById(R.id.description)).setText(desc);
        return view;
    }

    public int getColor() {
        return color;
    }

    public void setCardDesc(String desc) {
        this.desc = desc;
    }


}