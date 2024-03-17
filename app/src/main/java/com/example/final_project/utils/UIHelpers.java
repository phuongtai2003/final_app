package com.example.final_project.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.final_project.R;
import com.example.final_project.viewmodel.JobDetailsViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;

public class UIHelpers {
    public static final String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
    public static final String phoneRegex = "^[0-9]{10}$";
    public static void showSnackBar(View view, String message){
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);

        snackbar.setAction("Dismiss", v -> {
            snackbar.dismiss();
        });

        snackbar.show();
    }

    public static void showDialog(int gravity, String message, Context mContext){
        Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(com.example.final_project.R.layout.message_dialog);

        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams windowAttribute = window.getAttributes();
        windowAttribute.gravity = gravity;
        window.setAttributes(windowAttribute);
        dialog.setCancelable(true);

        MaterialButton mainButton = dialog.findViewById(R.id.main_dialog_button);
        TextView alertMessage = dialog.findViewById(R.id.message_dialog_body);
        alertMessage.setText(message);
        mainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}
