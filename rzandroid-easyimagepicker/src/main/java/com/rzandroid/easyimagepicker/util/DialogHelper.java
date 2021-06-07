package com.rzandroid.easyimagepicker.util;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AlertDialog;

import com.rzandroid.easyimagepicker.R;
import com.rzandroid.easyimagepicker.constant.ImageProvider;
import com.rzandroid.easyimagepicker.listener.DismissListener;
import com.rzandroid.easyimagepicker.listener.ResultListener;

public class DialogHelper {
    public void showChooseAppDialog(Context argContext, ResultListener argListener, DismissListener argDismissListener) {
        LayoutInflater layoutInflater = LayoutInflater.from(argContext);
        View customView = layoutInflater.inflate(R.layout.dialog_choose_app, null);
        AlertDialog dialog = new AlertDialog.Builder(argContext)
                .setTitle(R.string.title_choose_image_provider)
                .setView(customView)
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        argListener.onResult(null);
                    }
                })
                .setNegativeButton(R.string.action_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        argListener.onResult(null);
                    }
                })
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        argDismissListener.onDismiss();
                    }
                })
                .show();
        customView.findViewById(R.id.lytCameraPick)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        argListener.onResult(ImageProvider.CAMERA);
                        dialog.dismiss();
                    }
                });
        customView.findViewById(R.id.lytGalleryPick)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        argListener.onResult(ImageProvider.GALLERY);
                        dialog.dismiss();
                    }
                });
    }
}
