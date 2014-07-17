package ar.com.globant.githubrepository.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {

	public static final String TABLE_GITHUB = "users";
	public static final String GITHUB_ID = "_id";
	
	public static final String GITHUB_COLUMNA_USERNAME = "user";
	public static final String GITHUB_COLUMNA_PASSWORD = "password";
	
	public static final String DATABASE_NOMBRE = "githubusers.db";
	public static final int DATABASE_VERSION = 1;
	
	public MySQLiteHelper(Context context) {
		super(context, DATABASE_NOMBRE, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) { // Si la base no existe.
		db.execSQL("create table " + TABLE_GITHUB + "(" + GITHUB_ID + " integer primary key autoincrement, " + 
														  GITHUB_COLUMNA_USERNAME   + " text not null, " +
														  GITHUB_COLUMNA_PASSWORD   + " text not null ); "); 
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) { // Actualizar el schema.
		Log.e("INFO", "DB Actualizada.");
		
		db.execSQL("drop table id exists " + TABLE_GITHUB);
		onCreate(db);
	}

}
