package com.sevenhourdev.getlyric;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class WriteFile
{
    // the Activity or Application that is creating an object from this class.
    Context context;

    // a reference to the database used by this application/object
    private SQLiteDatabase db;

    // These constants are specific to the database.  They should be
    // changed to suit your needs.
    private final String DB_NAME = "lyrics";
    private final int DB_VERSION = 2;

    // These constants are specific to the database table.  They should be
    // changed to suit your needs.
    private final String TABLE_NAME = "database_table2";
    private final String TABLE_ROW_ID = "id";
    private final String TABLE_ROW_ONE = "artist";
    private final String TABLE_ROW_TWO = "song";
    private final String TABLE_ROW_THREE = "lyric";
    private final String TABLE_ROW_FOUR = "file_artist";
    private final String TABLE_ROW_FIVE = "file_song";

    public WriteFile(Context context)
    {
        this.context = context;

        // create or open the database
        CustomSQLiteOpenHelper helper = new CustomSQLiteOpenHelper(context);
        this.db = helper.getWritableDatabase();
    }




    /**********************************************************************
     * ADDING A ROW TO THE DATABASE TABLE
     *
     * This is an example of how to add a row to a database table
     * using this class.  You should edit this method to suit your
     * needs.
     *
     * the key is automatically assigned by the database
     * @param rowStringOne Artist of the song
     * @param rowStringTwo Name of the Song
     * @param rowStringThree Lyrics of the Song
     *
     */
    public void addRow(String rowStringOne, String rowStringTwo, String rowStringThree)
    {
        // this is a key value pair holder used by android's SQLite functions
        ContentValues values = new ContentValues();
        values.put(TABLE_ROW_ONE, rowStringOne);
        values.put(TABLE_ROW_TWO, rowStringTwo);
        values.put(TABLE_ROW_THREE, rowStringThree);
        values.put(TABLE_ROW_FOUR, rowStringOne);
        values.put(TABLE_ROW_FIVE, rowStringTwo);

        // ask the database object to insert the new data
        try{db.insert(TABLE_NAME, null, values);}
        catch(Exception e)
        {
            Log.e("DB ERROR", e.toString());
            e.printStackTrace();
        }
    }
    public void close(){
        db.close();
}



    /**********************************************************************
     * DELETING A ROW FROM THE DATABASE TABLE
     *
     * This is an example of how to delete a row from a database table
     * using this class. In most cases, this method probably does
     * not need to be rewritten.
     *
     * @param rowID the SQLite database identifier for the row to delete.
     */
    public void deleteRow(long rowID)
    {
        // ask the database manager to delete the row of given id
        try {db.delete(TABLE_NAME, TABLE_ROW_ID + "=" + rowID, null);}
        catch (Exception e)
        {
            Log.e("DB ERROR", e.toString());
            e.printStackTrace();
        }
    }

    /**
     * Check if Previously Imported
     * @return if previously imported or not!
     */
    public boolean checkImportState(){
         if(DatabaseUtils.queryNumEntries(db, TABLE_NAME)>0) {
             return true;
         }else{
             return false;
         }
    }

    /**
     *
     * @return amount of entries
     */
    public long numberOfEntries(){
        return DatabaseUtils.queryNumEntries(db, TABLE_NAME);
    }

    public boolean reset(){
//        Cursor c = myDB.rawQuery("SELECT * FROM " + MY_DB_TABLE+ " WHERE " + FIELD + "= '" + VALUE + "'");
//        if(c == null)
//        {
//            //doesn't exists therefore insert record.
//        }
        return true;
    }

    /**********************************************************************
     * UPDATING A ROW IN THE DATABASE TABLE
     *
     * This is an example of how to update a row in the database table
     * using this class.  You should edit this method to suit your needs.
     *
     * @param rowID ID of the song
     * @param rowStringOne Name of the song
     * @param rowStringTwo Artist of the Song
     * @param rowStringThree Lyrics of the Song
     */
    public void updateRow(long rowID, String rowStringOne, String rowStringTwo, String rowStringThree)
    {
        // this is a key value pair holder used by android's SQLite functions
        ContentValues values = new ContentValues();
        values.put(TABLE_ROW_ONE, rowStringOne);
        values.put(TABLE_ROW_TWO, rowStringTwo);
        values.put(TABLE_ROW_THREE, rowStringThree);

        // ask the database object to update the database row of given rowID
        try {
            db.update(TABLE_NAME, values, TABLE_ROW_ID + "=" + rowID, null);}
        catch (Exception e)
        {
            Log.e("DB Error", e.toString());
            e.printStackTrace();
        }
    }

    /**********************************************************************
     * RETRIEVING A ROW FROM THE DATABASE TABLE
     *
     * This is an example of how to retrieve a row from a database table
     * using this class.  You should edit this method to suit your needs.
     *
     * @param songName Name of the Song
     * @param band Name of the Artist
     * @return an array containing the data from the row
     */
    public Song getRowAsArray(String songName, String band)
    {
        // create an array list to store data from the database row.
        // I would recommend creating a JavaBean compliant object
        // to store this data instead.  That way you can ensure
        // data types are correct.
        Song song = new Song (-1, null, null, null);
        Cursor cursor;

        try
        {
            // this is a database call that creates a "cursor" object.
            // the cursor object store the information collected from the
            // database and is used to iterate through the data.
            cursor = db.query
                    (
                            TABLE_NAME,
                            new String[] { TABLE_ROW_ID, TABLE_ROW_ONE, TABLE_ROW_TWO,TABLE_ROW_THREE  },
                            TABLE_ROW_FOUR + "=?" +" and " + TABLE_ROW_FIVE +"=?", new String[]{ band,songName}
                            , null, null, null, null
                    );

            // move the pointer to position zero in the cursor.
            cursor.moveToFirst();

            // if there is data available after the cursor's pointer, add
            // it to the ArrayList that will be returned by the method.
            if (!cursor.isAfterLast())
            {
                do
                {
                    song = new Song(cursor.getLong(0),cursor.getString(1),cursor.getString(2),cursor.getString(3));
                }
                while (cursor.moveToNext());
            }

            // let java know that you are through with the cursor.
            cursor.close();
        }
        catch (SQLException e)
        {
            Log.e("DB ERROR", e.toString());
            e.printStackTrace();
        }

        // return the ArrayList containing the given row from the database.
        return song;
    }

    /**********************************************************************
     * RETRIEVING A ROW FROM THE DATABASE TABLE - Real Title
     *
     * This is an example of how to retrieve a row from a database table
     * using this class.  You should edit this method to suit your needs.
     *
     * @param songName Name of the Song
     * @param band Name of the Artist
     * @return an array containing the data from the row
     */
    public Song getRowAsArrayReal(String songName, String band)
    {
        // create an array list to store data from the database row.
        // I would recommend creating a JavaBean compliant object
        // to store this data instead.  That way you can ensure
        // data types are correct.
        Song song = new Song (-1, null, null, null);
        Cursor cursor;

        try
        {
            // this is a database call that creates a "cursor" object.
            // the cursor object store the information collected from the
            // database and is used to iterate through the data.
            cursor = db.query
                    (
                            TABLE_NAME,
                            new String[] { TABLE_ROW_ID, TABLE_ROW_ONE, TABLE_ROW_TWO,TABLE_ROW_THREE  },
                            TABLE_ROW_ONE + "=?" +" and " + TABLE_ROW_TWO +"=?", new String[]{ band,songName}
                            , null, null, null, null
                    );

            // move the pointer to position zero in the cursor.
            cursor.moveToFirst();

            // if there is data available after the cursor's pointer, add
            // it to the ArrayList that will be returned by the method.
            if (!cursor.isAfterLast())
            {
                do
                {
                    song = new Song(cursor.getLong(0),cursor.getString(1),cursor.getString(2),cursor.getString(3));
                }
                while (cursor.moveToNext());
            }

            // let java know that you are through with the cursor.
            cursor.close();
        }
        catch (SQLException e)
        {
            Log.e("DB ERROR", e.toString());
            e.printStackTrace();
        }

        // return the ArrayList containing the given row from the database.
        return song;
    }


    /**********************************************************************
     * RETRIEVING ALL ROWS FROM THE DATABASE TABLE
     *
     * This is an example of how to retrieve all data from a database
     * table using this class.  You should edit this method to suit your
     * needs.
     *
     * the key is automatically assigned by the database
     *
     * @return Arraylist of all the Songs (in Song)
     */

    public ArrayList<Song> getAllRowsAsArrays()
    {
        // create an ArrayList that will hold all of the data collected from
        // the database.
        ArrayList<Song> dataArrays = new ArrayList<Song>();

        // this is a database call that creates a "cursor" object.
        // the cursor object store the information collected from the
        // database and is used to iterate through the data.
        Cursor cursor;

        try
        {
            // ask the database object to create the cursor.
            cursor = db.query(
                    TABLE_NAME,
                    new String[]{TABLE_ROW_ID, TABLE_ROW_ONE, TABLE_ROW_TWO,TABLE_ROW_THREE},
                    null, null, null, null, null
            );

            // move the cursor's pointer to position zero.
            cursor.moveToFirst();

            // if there is data after the current cursor position, add it
            // to the ArrayList.
            if (!cursor.isAfterLast())
            {
                do {
                        dataArrays.add(new Song(cursor.getLong(0), cursor.getString(1), cursor.getString(2), cursor.getString(3)));

                }
                // move the cursor's pointer up one position.
                while (cursor.moveToNext());
            }
            cursor.close();
        }
        catch (SQLException e)
        {
            Log.e("DB Error", e.toString());
            e.printStackTrace();
        }

        // return the ArrayList that holds the data collected from
        // the database.
        return dataArrays;
    }




    /**********************************************************************
     * THIS IS THE BEGINNING OF THE INTERNAL SQLiteOpenHelper SUBCLASS.
     *
     * I MADE THIS CLASS INTERNAL SO I CAN COPY A SINGLE FILE TO NEW APPS
     * AND MODIFYING IT - ACHIEVING DATABASE FUNCTIONALITY.  ALSO, THIS WAY
     * I DO NOT HAVE TO SHARE CONSTANTS BETWEEN TWO FILES AND CAN
     * INSTEAD MAKE THEM PRIVATE AND/OR NON-STATIC.  HOWEVER, I THINK THE
     * INDUSTRY STANDARD IS TO KEEP THIS CLASS IN A SEPARATE FILE.
     *********************************************************************/

    /**
     * This class is designed to check if there is a database that currently
     * exists for the given program.  If the database does not exist, it creates
     * one.  After the class ensures that the database exists, this class
     * will open the database for use.  Most of this functionality will be
     * handled by the SQLiteOpenHelper parent class.  The purpose of extending
     * this class is to tell the class how to create (or update) the database.
     *
     * @author Randall Mitchell
     *
     */

    private class CustomSQLiteOpenHelper extends SQLiteOpenHelper
    {
        public CustomSQLiteOpenHelper(Context context)
        {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db)
        {
            // This string is used to create the database.  It should
            // be changed to suit your needs.
            String newTableQueryString = "create table " +
                    TABLE_NAME +
                    " (" +
                    TABLE_ROW_ID + " integer primary key autoincrement not null," +
                    TABLE_ROW_ONE + " text," +
                    TABLE_ROW_TWO + " text," +
                    TABLE_ROW_THREE + " text," +
                    TABLE_ROW_FOUR + " text," +
                    TABLE_ROW_FIVE + " text"+
                    ");";
            // execute the query string to the database.
            db.execSQL(newTableQueryString);
        }


        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
            String q1 = "ALTER TABLE " + TABLE_NAME + " ADD " + TABLE_ROW_THREE + " text";
            db.execSQL(q1);
        }
    }
}