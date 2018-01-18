package com.mindcriminal.eligalaapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * Created by mindcriminal on 11/12/17.
 */

public class EntryFragment extends Fragment implements View.OnClickListener {

    private EditText nameInput;
    private EditText ticketInput;
    private EditText rangeInput;
    private Button readTicketButton;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private static final int RC_TICKET_CAPTURE = 9001;
    private MySQLiteHelper mySQLiteHelper;

    public static EntryFragment newInstance(){
        return new EntryFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.activity_entrypage, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Purchased Tickets");
        mySQLiteHelper = new MySQLiteHelper(getActivity());

        nameInput = getView().findViewById(R.id.name_input);
        ticketInput = getView().findViewById(R.id.ticket_input);
        rangeInput = getView().findViewById(R.id.range_input);

        // Disable scanning for now
        readTicketButton = getView().findViewById(R.id.read_ticket);
        readTicketButton.setEnabled(false);

        getView().findViewById(R.id.read_ticket).setOnClickListener(this);
        getView().findViewById(R.id.save_ticket).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.read_ticket) {
            Intent intent = new Intent(getActivity(), OcrCaptureActivity.class);
            intent.putExtra(OcrCaptureActivity.AutoFocus, true);
            intent.putExtra(OcrCaptureActivity.UseFlash, true);

            startActivityForResult(intent, RC_TICKET_CAPTURE);
        }
        else if (v.getId() == R.id.save_ticket) {
            String name = nameInput.getText().toString();
            Integer ticket = Integer.parseInt(ticketInput.getText().toString());
            Integer range = Integer.parseInt(rangeInput.getText().toString());

            RaffleData raffleData = new RaffleData();

            if(range > 0) {
                for (int i = 0; i <= range; i++) {
                    raffleData.setData(name, ticket + i, 0);
                    reference.child(Integer.toString(ticket + i)).setValue(raffleData);
                    Log.d(TAG, "Saving " + name + " " + (ticket + i) + " to Firebase");
                    mySQLiteHelper.addTicket(raffleData);
                }
            }
            else {
                raffleData.setData(name, ticket, 0);
                reference.child(Integer.toString(ticket)).setValue(raffleData);
                Log.d(TAG, "Saving " + name + " " + ticket + " to Firebase");
                mySQLiteHelper.addTicket(raffleData);
            }

            Toast.makeText(getActivity().getApplicationContext(),"Ticket Successfully Saved!",Toast.LENGTH_LONG).show();
            nameInput.setText("");
            ticketInput.setText("");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_TICKET_CAPTURE) {
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
