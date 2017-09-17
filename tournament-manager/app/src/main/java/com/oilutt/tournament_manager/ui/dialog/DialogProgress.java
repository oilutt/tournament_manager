package com.oilutt.tournament_manager.ui.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.widget.TextView;

import com.oilutt.tournament_manager.R;

import java.io.Serializable;

/**
 * Created by oilut on 21/08/2017.
 */
public class DialogProgress extends AlertDialog implements Serializable {


    private static TextView txtProgress;

    public DialogProgress(Context context) {
        super(context, R.style.ThemeDialogFadeInFadeOut);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_progress);
        setCancelable(false);
        txtProgress = (TextView) findViewById(R.id.txt_progress);
    }

    public void setTitle(String text) {
        txtProgress.setText(text);
    }

    @Override
    public void show() {
        super.show();
    }

    public void setTitle(int resString) {
        txtProgress.setText(getContext().getString(resString));
    }

}
