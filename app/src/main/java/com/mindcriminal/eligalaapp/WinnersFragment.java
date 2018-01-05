package com.mindcriminal.eligalaapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

/**
 * Created by mindcriminal on 11/12/17.
 */

public class WinnersFragment extends Fragment implements View.OnClickListener{

    private CompoundButton autoFocus;
    private CompoundButton useFlash;
    private TextView statusMessage;
    private TextView ticketValue;
    private TextInputEditText prizeInput;

    FirebaseDatabase database;
    DatabaseReference reference;

    private static final int RC_BARCODE_CAPTURE = 9001;

    private ArrayList<String> ticketList;

    public static WinnersFragment newInstance(){
        WinnersFragment winnersFragment = new WinnersFragment();
        return winnersFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.activity_winnerspage, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        database= FirebaseDatabase.getInstance();
        reference= database.getReference("Purchased Tickets");

        statusMessage = getView().findViewById(R.id.status_message);
        prizeInput = getView().findViewById(R.id.prize_input);

        autoFocus = getView().findViewById(R.id.auto_focus);
        useFlash = getView().findViewById(R.id.use_flash);

        getView().findViewById(R.id.read_ticket).setOnClickListener(this);
        getView().findViewById(R.id.save_data).setOnClickListener(this);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.read_ticket) {
            // launch ticket activity.
            Intent intent = new Intent(getActivity(), OcrCaptureActivity.class);
            intent.putExtra(OcrCaptureActivity.AutoFocus, autoFocus.isChecked());
            intent.putExtra(OcrCaptureActivity.UseFlash, useFlash.isChecked());

            startActivityForResult(intent, RC_BARCODE_CAPTURE);
        }
        else if (v.getId() == R.id.save_data) {
            String prize = prizeInput.getText().toString();
            DatabaseReference ticketRef = reference.child(ticketList.get(0));
            Map<String, Object> prizeUpdate = new HashMap<>();
            prizeUpdate.put("prize", prize);

            // Publish to database
            ticketRef.updateChildren(prizeUpdate);

            Log.d(TAG, "Saving " + prize + " " + ticketList.get(0) + " to Firebase");

            Toast.makeText(getActivity().getApplicationContext(),"Data Successfully Saved!",Toast.LENGTH_LONG).show();
            prizeInput.setText("");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_BARCODE_CAPTURE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    //Barcode ticket = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);
                    //statusMessage.setText(R.string.ticket_success);
                    //ticketValue.setText(ticket.displayValue);
                    //Log.d(TAG, "Barcode read: " + ticket.displayValue);

                    ticketList = (ArrayList<String>)data.getSerializableExtra(OcrCaptureActivity.TicketList);
                    String ticketsConfirmed = ticketList.size() + " Barcodes Scanned!";
                    statusMessage.setText(ticketsConfirmed);
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
