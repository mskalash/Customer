package com.customer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * Created by oleh on 10/21/15.
 */
public class RemoveConfirmation extends DialogFragment {
ListAdapter contactAdapter;
    String purpose;
    int itemID;
    int position;
    String message;
   // DatabaseAdapter databaseAdapter= new DatabaseAdapter();;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        purpose = getArguments().getString("purpose");
        itemID = getArguments().getInt("itemID");
        position = getArguments().getInt("position");

        switch (purpose) {
            case "contacts":
                message = "This contact will be deleted with all it`s subordinate meetings and records permanently";
                break;
        }


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Delete "+purpose.substring(0, purpose.length()-1)+"?");
        builder.setMessage(message);
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (purpose.equals("contacts")){
                    removeContact(itemID, position);

                }

            }
        });

        Dialog dialog = builder.create();

        return dialog;
    }

    protected void removeContact(int itemID, int position){

//        int countOfAffectedRows = databaseAdapter.deleteContact(itemID);
//        if (countOfAffectedRows == 1) {
//            contactAdapter.names.remove(position);
//            contactAdapter.descriptions.remove(position);
//            contactAdapter.ids.remove(position);
//            contactAdapter.notifyItemRemoved(position);
//        }
    }
}
