package com.example.myapplication;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

public class StyleUpdater {
    private static ImageView lastSelectedImageView;
    private static TextView lastSelectedTextView;
    public static void updateTextStyles(TextView selectedTextView, TextView[] allTextViews, Context context) {
        for (TextView textView : allTextViews) {
            boolean isSelected = (textView == selectedTextView);

            updateTextStyle(textView, isSelected, context);
        }
    }

    private static void updateTextStyle(TextView textView, boolean isSelected, Context context) {
        String text = textView.getText().toString();
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(text);

        if (isSelected) {
            spannableStringBuilder.setSpan(new UnderlineSpan(), 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableStringBuilder.setSpan(new StyleSpan(Typeface.BOLD), 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableStringBuilder.setSpan(new ForegroundColorSpan(context.getColor(R.color.bluegreen)), 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else {
            spannableStringBuilder.removeSpan(new UnderlineSpan());
            spannableStringBuilder.removeSpan(new StyleSpan(Typeface.BOLD));
            spannableStringBuilder.removeSpan(new ForegroundColorSpan(context.getColor(R.color.black)));
        }

        textView.setText(spannableStringBuilder);
    }


    public static void changeImageColor(ImageView imageView, Context context) {
        if (lastSelectedImageView != null) {
            resetImageColor(lastSelectedImageView);
        }

        Drawable drawable = imageView.getDrawable();
        int color = context.getResources().getColor(R.color.bluegreen);
        drawable.setColorFilter(color, PorterDuff.Mode.SRC_IN);
        imageView.setImageDrawable(drawable);

        lastSelectedImageView = imageView;
    }

    private static void resetImageColor(ImageView imageView) {
        Drawable drawable = imageView.getDrawable();
        drawable.clearColorFilter();
        imageView.setImageDrawable(drawable);
    }
}
