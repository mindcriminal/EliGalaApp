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

    private TextView statusMessage;
    private EditText nameInput;
    private EditText ticketInput;
    private Button readTicketButton;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private static final int RC_BARCODE_CAPTURE = 9001;
    private ArrayList<String> ticketList;

    public static EntryFragment newInstance(){
        EntryFragment entryFragment = new EntryFragment();
        return entryFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.activity_entrypage, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        database= FirebaseDatabase.getInstance();
        reference= database.getReference("Purchased Tickets");

        statusMessage = getView().findViewById(R.id.status_message);
        nameInput = getView().findViewById(R.id.name_input);
        ticketInput = getView().findViewById(R.id.ticket_input);

        ticketList = new ArrayList<>();

        // Disable scanning for now
        //readTicketButton = getView().findViewById(R.id.read_ticket);
        //readTicketButton.setEnabled(false;)

        getView().findViewById(R.id.read_ticket).setOnClickListener(this);
        getView().findViewById(R.id.save_data).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.read_ticket) {
            Intent intent = new Intent(getActivity(), OcrCaptureActivity.class);
            intent.putExtra(OcrCaptureActivity.AutoFocus, true);
            intent.putExtra(OcrCaptureActivity.UseFlash, true);

            startActivityForResult(intent, RC_BARCODE_CAPTURE);
        }
        else if (v.getId() == R.id.save_data) {
            String name = nameInput.getText().toString();
            String ticket = ticketInput.getText().toString();
            RaffleData raffleData = new RaffleData();

            if(ticketList.size() > 1) {
                // Loop over ticket list and publish to database
                for (int i = 0; i < ticketList.size(); i++) {
                    raffleData.setData(name, ticketList.get(i), "");
                    reference.child(ticketList.get(i)).setValue(raffleData);
                    Log.d(TAG, "Saving " + name + " " + ticketList.get(i) + " to Firebase");
                }
            }
            else {
                // Single ticket
                raffleData.setData(name, ticket, "");
                reference.child(ticket).setValue(raffleData);
                Log.d(TAG, "Saving " + name + " " + ticket + " to Firebase");
            }

            Toast.makeText(getActivity().getApplicationContext(),"Data Successfully Saved!",Toast.LENGTH_LONG).show();
            nameInput.setText("");
            ticketInput.setText("");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_BARCODE_CAPTURE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    ticketList = (ArrayList<String>)data.getSerializableExtra(OcrCaptureActivity.TicketList);
                    String ticketsConfirmed = ticketList.size() + " Tickets Scanned!";
                    statusMessage.setText(ticketsConfirmed);
                    if(ticketList.size() > 1) {
                        ticketInput.setText(R.string.multiple_tickets);
                    }
                    else {
                        ticketInput.setText(ticketList.get(0));
                    }
                } else {
                    statusMessage.setText(R.string.ticket_failure);
                    Log.d(TAG, "No ticket captured, intent data is null");
                }
            } else {
                statusMessage.setText(String.format(getString(R.string.ticket_error),
                        CommonStatusCodes.getStatusCodeString(resultCode)));
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
