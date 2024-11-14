package com.example.myapplication;

import android.content.Context;
import android.text.InputType;
import android.view.MotionEvent;
import android.widget.EditText;

public class showPassword {
    public static boolean visiblePassword(final EditText password, MotionEvent event){
        boolean isPasswordVisible = false;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                password.setInputType(InputType.TYPE_CLASS_TEXT);
                isPasswordVisible = true;
                return true;

            case MotionEvent.ACTION_UP:
                password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                isPasswordVisible = false;
                return true;
        }
        return false;
    }
}
