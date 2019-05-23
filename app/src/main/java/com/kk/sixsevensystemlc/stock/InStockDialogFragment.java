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

public class InStockDialogFragment extends DialogFragment {

    public interface InListener
    {
        void onInComplete(int num);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater =getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_instock_fragment, null);
        final EditText numText = (EditText)view.findViewById(R.id.in_num);
        builder.setView(view).setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                InStockDialogFragment.InListener inListener = (InStockDialogFragment.InListener)getActivity();
                inListener.onInComplete(Integer.parseInt(numText.getText().toString()));
            }
        }).setNegativeButton("取消",null);
        return builder.create();
    }

}
