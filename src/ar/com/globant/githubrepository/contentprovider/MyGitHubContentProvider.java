package ar.com.globant.githubrepository.contentprovider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import ar.com.globant.githubrepository.sql.MySQLiteHelper;

public class MyGitHubContentProvider extends ContentProvider {

	private MySQLiteHelper mDBHelper;
	private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);	
	private static final Uri BASE_CONTENT_URI = Uri.parse("content://ar.com.globant.githubrepository.contentprovider");
	public  static final Uri CONTENT_URI;
	private static final int ALL = 10;
	private static final int SINGLE_ID = 15;
	
	static {
		CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath("user").build();
		
		uriMatcher.addURI("com.bootcamp.globant.contentprovider", "user", ALL);
		uriMatcher.addURI("com.bootcamp.globant.contentprovider", "user/*", SINGLE_ID);
	}
	
	@Override
	public int delete(Uri uri, String where, String[] whereArgs) {
		SQLiteDatabase db = mDBHelper.getWritableDatabase();
		int count = 0;
		
		switch (uriMatcher.match(uri)) {
		case ALL:
			count = db.delete(mDBHelper.TABLE_GITHUB, where, whereArgs);
			break;			
			
		case SINGLE_ID:
			String id = uri.getLastPathSegment();
			count = db.delete(mDBHelper.TABLE_GITHUB, mDBHelper.GITHUB_ID + " = " + uri.getPathSegments().get(1), whereArgs);
			break;		
			
		default:
			throw new IllegalArgumentException("Error al insertar un registro. " + uri);
		}
		
		return count;
	}

	@Override
	public String getType(Uri uri) {
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		SQLiteDatabase db = mDBHelper.getWritableDatabase();
		
		long id = db.insert(mDBHelper.TABLE_GITHUB, null, values);
		
		if (id > 0) {
			return CONTENT_URI.buildUpon().appendPath(Long.toString(id)).build();
		}

		throw new IllegalArgumentException("Error al insertar un registro. " + uri);
	}

	@Override
	public boolean onCreate() {
		mDBHelper = new MySQLiteHelper(this.getContext());
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		
		queryBuilder.setTables(mDBHelper.TABLE_GITHUB);
		
		switch (uriMatcher.match(uri)) {
		case ALL:
			
			break;
		case SINGLE_ID:
        	queryBuilder.appendWhere(mDBHelper.GITHUB_ID + " = " + uri.getPathSegments().get(1));
			break;
		default:
			break;
		}
		
		SQLiteDatabase db = mDBHelper.getReadableDatabase();
		
		Cursor c = queryBuilder.query(db, projection, selection, selectionArgs, null, null, null);
		
		return c;
	}

	@Override
	public int update(Uri uri, ContentValues cv, String arg2, String[] arg3) {
		return 0;
	}

}
