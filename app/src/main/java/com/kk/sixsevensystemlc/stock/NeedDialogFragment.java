package com.kk.sixsevensystemlc.stock;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.kk.sixsevensystemlc.R;

public class NeedDialogFragment extends DialogFragment {

    public interface NeedListener
    {
        void onNeedComplete(int num);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater =getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_need_fragment, null);
        builder.setView(view).setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                NeedDialogFragment.NeedListener inListener = (NeedDialogFragment.NeedListener)getActivity();
            }
        }).setNegativeButton("取消",null);
        return builder.create();
    }

}
