package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

public class AlertDialogueBox extends DialogFragment {
    public interface  NoticeDialogueListener{
            void onDialogPositiveClick(DialogFragment dialog);
            void onDialogNegativeClick(DialogFragment dialog);
    }
    private static final AlertDialogueBox dialogueBox = new AlertDialogueBox();
    private AlertDialogueBox(){}
    NoticeDialogueListener noticeDialogueListener;
    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        builder.setTitle("Add Parameters").setPositiveButton("Add", null).setNegativeButton("Cancel", (dialogInterface, i) -> noticeDialogueListener.onDialogNegativeClick(AlertDialogueBox.this)).setView(inflater.inflate(R.layout.popup_layout,null));
        return  builder.create();
    }
    @Override
    public void onResume(){
        super.onResume();
        final AlertDialog dialog = (AlertDialog) getDialog();
        if(dialog!=null){
            Button positiveButton = (Button)dialog.getButton(Dialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(view->{
                noticeDialogueListener.onDialogPositiveClick(AlertDialogueBox.this);
            });
        }
    }
    @Override
    public void onAttach(@NonNull Context context) {

        super.onAttach(context);
        try {
            noticeDialogueListener = (NoticeDialogueListener) context;
        }
        catch (ClassCastException e){
            System.out.println("Implement the Methods");
        }
    }
    public static AlertDialogueBox getInstance(){
        return dialogueBox;
    }
}
