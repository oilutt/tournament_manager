package com.oilutt.tournament_manager.ui.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.oilutt.tournament_manager.R;


/**
 * Created by lucas on 17/03/17.
 */

public class CustomSimpleDialog {

    public static void show(Context context, int title, int content, MaterialDialog.SingleButtonCallback callback) {
        showDialog(context, title == -1 ? null : context.getString(title), context.getString(content), callback);
    }

    public static void show(Context context, int title, int content,
                            int positiveText, int negativeText,
                            MaterialDialog.SingleButtonCallback callback) {
        showDialog(context, title == -1 ? null : context.getString(title), context.getString(content),
                context.getString(positiveText), context.getString(negativeText), callback);
    }

    public static void show(Context context, String title, String content, MaterialDialog.SingleButtonCallback callback) {
        showDialog(context, title, content, callback);
    }

    public static void show(Context context, int title, int content) {
        showDialog(context, title == -1 ? null : context.getString(title), context.getString(content), (dialog, which) -> {
        });
    }

    public static void show(Context context, String title, String content) {
        showDialog(context, title, content, (dialog, which) -> {
        });
    }

    private static void showDialog(Context context, String title, String content, MaterialDialog.SingleButtonCallback callback) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_custom_simple, null);

        TextView textTitle = (TextView) view.findViewById(R.id.txt_title);
        TextView textContent = (TextView) view.findViewById(R.id.text_content);

        if (TextUtils.isEmpty(title)) {
            textTitle.setVisibility(View.GONE);
        } else {
            textTitle.setText(title);
        }

        textContent.setText(content);

        MaterialDialog simpleDialog = new MaterialDialog.Builder(context)
                .onPositive(callback)
//                .buttonsGravity(GravityEnum.CENTER)
                .positiveText(R.string.dialog_ok)
                .widgetColorRes(R.color.cerulean)
                .cancelable(false)
                .customView(view, true)
                .build();

        simpleDialog.show();
    }

    private static void showDialog(Context context, String title, String content,
                                   String positiveText, String negativeText,
                                   MaterialDialog.SingleButtonCallback callback) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_custom_simple, null);

        TextView textTitle = (TextView) view.findViewById(R.id.txt_title);
        TextView textContent = (TextView) view.findViewById(R.id.text_content);

        if (TextUtils.isEmpty(title)) {
            textTitle.setVisibility(View.GONE);
        } else {
            textTitle.setText(title);
        }

        textContent.setText(content);

        MaterialDialog simpleDialog = new MaterialDialog.Builder(context)
                .onPositive(callback)
                .positiveText(positiveText)
                .negativeText(negativeText)
                .widgetColorRes(R.color.cerulean)
                .cancelable(false)
                .customView(view, true)
                .build();

        simpleDialog.show();
    }
}