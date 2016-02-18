package com.digitale.database;

/*
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.sql.Database;
import com.badlogic.gdx.sql.DatabaseCursor;
import com.badlogic.gdx.sql.DatabaseFactory;
import com.badlogic.gdx.sql.SQLiteGdxException;


public class DataBaseCopy {
 
    //The Android's default system path of your application database.
    private static String DB_PATH = "/data/data/com.digitale.myepiclife/databases/";
    private static String DB_NAME = "mel";
    private static final String DATABASE_NAME = "mel";
    private static final int DATABASE_VERSION = 1;
    private Database myDataBase; 
 
 //   private final Context myContext;
 
    *//**
     * Constructor
     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
     * @param context
     *//*
    public DataBaseCopy(Context context) {
 
    	super(context, DB_NAME, null, 1);
        this.myContext = context;
    }	
 
  *//**
     * Creates a empty database on the system and rewrites it with your own database.
     * *//*
    public void createDataBase() throws IOException{
    	myDataBase = DatabaseFactory.getNewDatabase(DATABASE_NAME,
	                DATABASE_VERSION, null, null);
    	boolean dbExist = checkDataBase();
 
    	if(dbExist){
    		//do nothing - database already exist
    	}else{
 
    		//By calling this method and empty database will be created into the default system path
               //of your application so we are gonna be able to overwrite that database with our database.
	        try {
	        	myDataBase.openOrCreateDatabase();
	        } catch (SQLiteGdxException e) {
	            e.printStackTrace();
	        }
 
        	try {
 
    			copyDataBase();
 
    		} catch (IOException e) {
 
        		throw new Error("Error copying database");
 
        	}
    	}
 
    }
 
    *//**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     * @return true if it exists, false if it doesn't
     *//*
    private boolean checkDataBase(){
 
    	Database checkDB = null;
 
    	try{
    		String myPath = DB_PATH + DB_NAME;
//    		checkDB = Database.openDatabase(myPath, null, Database.OPEN_READONLY);
    		checkDB = DatabaseFactory.getNewDatabase(DATABASE_NAME,
	                DATABASE_VERSION, null, null);
    		checkDB.openOrCreateDatabase();
    	}catch(SQLiteGdxException e){
 
    		//database does't exist yet.
 
    	}
 
    	if(checkDB != null){
 
	    	try {
	    		myDataBase.closeDatabase();
	        } catch (SQLiteGdxException e) {
	            e.printStackTrace();
	        }
 
    	}
 
    	return checkDB != null ? true : false;
    }
 
    *//**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     * *//*
    private void copyDataBase() throws IOException{
 
    	FileHandle from = Gdx.files.internal("mel");
    	from.copyTo(Gdx.files.external("mel"));
    	//Open your local db as the input stream
    	InputStream myInput = myContext.getAssets().open(DB_NAME);
 
    	// Path to the just created empty db
    	String outFileName = DB_PATH + DB_NAME;
 
    	//Open the empty db as the output stream
    	OutputStream myOutput = new FileOutputStream(outFileName);
 
    	//transfer bytes from the inputfile to the outputfile
    	byte[] buffer = new byte[1024];
    	int length;
    	while ((length = myInput.read(buffer))>0){
    		myOutput.write(buffer, 0, length);
    	}
 
    	//Close the streams
    	myOutput.flush();
    	myOutput.close();
    	myInput.close();
 
    }
 
    public void openDataBase() throws SQLiteGdxException{
 
    	//Open the database
        String myPath = DB_PATH + DB_NAME;
//    	myDataBase = Database.openDatabase(myPath, null, Database.OPEN_READONLY);
        try {
        	myDataBase.openOrCreateDatabase();
        } catch (SQLiteGdxException e) {
            e.printStackTrace();
        }
    }
 

	public  void close() {
 
    	    if(myDataBase != null)
    	    	try {
    	    		myDataBase.closeDatabase();
    	        } catch (SQLiteGdxException e) {
    	            e.printStackTrace();
    	        }
 
	}
 
 
        // Add your public helper methods to access and get content from the database.
       // You could return cursors by doing "return myDataBase.query(....)" so it'd be easy
       // to you to create adapters for your views.
 }
*/