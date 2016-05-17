package com.fachrur_122.rxdabinmodule;

import android.databinding.BindingAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by fachrur_122 on 17/05/2016.
 *
 */
public class Binding {

    @BindingAdapter({"bind:imageUrl"})
    public static void loadImage(ImageView view, String imageUrl) {
        if (!imageUrl.equals("")) {
            Picasso.with(view.getContext()).load(imageUrl).into(view);
        }
    }


    @BindingAdapter({"bind:font"})
    public static void setFont(TextView textView, String fontName) {
        Font font = Font.getInstance();
        font.addFont(fontName, fontName);
        textView.setTypeface(font.get(fontName));
    }
}
