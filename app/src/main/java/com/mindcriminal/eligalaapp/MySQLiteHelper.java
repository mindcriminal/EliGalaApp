package com.mindcriminal.eligalaapp;

import java.util.LinkedList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {
	
	// Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "ELI_GALA_TICKETS.DB";
   
	public MySQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);	
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// SQL statement to create book table
		String CREATE_BOOK_TABLE = "CREATE TABLE tickets ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " + 
				"name TEXT, "+
                "ticket TEXT, "+
				"prize TEXT )";
		
		// create books table
		db.execSQL(CREATE_BOOK_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older tickets table if existed
        db.execSQL("DROP TABLE IF EXISTS tickets");
        
        // create fresh tickets table
        this.onCreate(db);
	}
	//---------------------------------------------------------------------

	
	// Books table name
    private static final String TABLE_TICKETS = "tickets";
    
    // Books Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_TICKET = "ticket";
    private static final String KEY_PRIZE = "prize";


    private static final String[] COLUMNS = {KEY_ID,KEY_NAME,KEY_TICKET,KEY_PRIZE};
    
	public void addTicket(RaffleData ticket){
		Log.d("addTicket", ticket.toString());
		// 1. get reference to writable DB
		SQLiteDatabase db = this.getWritableDatabase();
		 
		// 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, ticket.getName()); // get name
        values.put(KEY_TICKET, ticket.getTicket()); // get ticket
        values.put(KEY_PRIZE, ticket.getPrize()); // get prize

        // 3. insert
        db.insert(TABLE_TICKETS, // table
        		null, //nullColumnHack
        		values); // key/value -> keys = column names/ values = column values
        
        // 4. close
        db.close(); 
	}


	public RaffleData getDataForTicket(Integer ticketNumber){

		// 1. get reference to readable DB
		SQLiteDatabase db = this.getReadableDatabase();
		 
		// 2. build query
        Cursor cursor = 
        		db.query(TABLE_TICKETS, // a. table
        		COLUMNS, // b. column names
        		" ticket = ?", // c. selections
                new String[] { String.valueOf(ticketNumber) }, // d. selections args
                null, // e. group by
                null, // f. having
                null, // g. order by
                null); // h. limit
        
        // 3. if we got results get the first one
        if (cursor != null)
            cursor.moveToFirst();
 
        // 4. build ticket object
        RaffleData ticket = new RaffleData();
        ticket.setId(Integer.parseInt(cursor.getString(0)));
        ticket.setName(cursor.getString(1));
        ticket.setTicket(Integer.parseInt(cursor.getString(2)));
        ticket.setPrize(Integer.parseInt(cursor.getString(3)));

        Log.d("getDataForTicket("+ticket+")", ticket.toString());

        // 5. return ticket
        return ticket;
	}

	/*
	// Get All Books
    public List<RaffleData> getAllTickets() {
        List<RaffleData> tickets = new LinkedList<RaffleData>();

        // 1. build the query
        String query = "SELECT  * FROM " + TABLE_TICKETS;
 
    	// 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
 
        // 3. go over each row, build book and add it to list
        RaffleData ticket = null;
        if (cursor.moveToFirst()) {
            do {
                ticket = new RaffleData();
                ticket.setId(Integer.parseInt(cursor.getString(0)));
                ticket.setName(cursor.getString(1));
                ticket.setTicket(Integer.parseInt(cursor.getString(2)));
                ticket.setPrize(Integer.parseInt(cursor.getString(3)));

                // Add book to books
                tickets.add(ticket);
            } while (cursor.moveToNext());
        }
        
		Log.d("getAllTickets()", tickets.toString());

        // return tickets
        return tickets;
    }

	 // Updating single book
    public int updateBook(Book book) {

    	// 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
 
		// 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put("title", book.getTitle()); // get title 
        values.put("author", book.getAuthor()); // get author
 
        // 3. updating row
        int i = db.update(TABLE_TICKETS, //table
        		values, // column/value
        		KEY_ID+" = ?", // selections
                new String[] { String.valueOf(book.getId()) }); //selection args
        
        // 4. close
        db.close();
        
        return i;
        
    }

    // Deleting single book
    public void deleteBook(Book book) {

    	// 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        
        // 2. delete
        db.delete(TABLE_TICKETS,
        		KEY_ID+" = ?",
                new String[] { String.valueOf(book.getId()) });
        
        // 3. close
        db.close();
        
		Log.d("deleteBook", book.toString());

    }
    */
}
