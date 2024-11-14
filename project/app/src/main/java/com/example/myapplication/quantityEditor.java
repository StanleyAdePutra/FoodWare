package com.example.myapplication;

import android.widget.TextView;

public class quantityEditor {
    public static void increaseQuantity(TextView qtyTextView) {
        int quantity = getQuantity(qtyTextView);
        quantity++;
        updateQuantityTextView(qtyTextView, quantity);
    }

    public static void decreaseQuantity(TextView qtyTextView) {
        int quantity = getQuantity(qtyTextView);
        if (quantity > 1) {
            quantity--;
            updateQuantityTextView(qtyTextView, quantity);
        }
    }

    private static int getQuantity(TextView qtyTextView) {
        try {
            return Integer.parseInt(qtyTextView.getText().toString());
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return 1;
        }
    }

    private static void updateQuantityTextView(TextView qtyTextView, int quantity) {
        qtyTextView.setText(String.valueOf(quantity));
    }
}
