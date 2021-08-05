package com.project.aas.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class CallDialog extends AppCompatDialogFragment {

    private String phone;


    public void setPhone(String phone) {
        this.phone = phone;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + phone));

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Call " + phone)
                .setMessage("Are you sure to call this number?")
                .setPositiveButton("Call", (dialog, which) -> {

                    startActivity(callIntent);
                    dialog.dismiss();
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        return builder.create();
    }
}
