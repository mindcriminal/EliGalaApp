package com.mindcriminal.eligalaapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SearchActivity extends Activity implements OnClickListener {

    private Button search;
    private EditText ticket;
    private EditText name;
    private EditText prize;

    private MySQLiteHelper mySQLiteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("Ticket Search");

        setContentView(R.layout.activity_search);

        ticket = findViewById(R.id.ticket_search);
        name = findViewById(R.id.name_search);
        name.setEnabled(false);
        prize = findViewById(R.id.prize_search);
        prize.setEnabled(false);

        search = findViewById(R.id.search);

        mySQLiteHelper = new MySQLiteHelper(this);
        search.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search:

                final String ticketNumber = ticket.getText().toString();

                if(!ticketNumber.isEmpty())
                {
                    // TODO - add error handling for query
                    RaffleData ticket = mySQLiteHelper.getDataForTicket(Integer.parseInt(ticketNumber));
                    name.setText(ticket.getName(), TextView.BufferType.EDITABLE);
                    prize.setText(Integer.toString(ticket.getPrize()), TextView.BufferType.EDITABLE);
                }

                break;
        }
    }

}