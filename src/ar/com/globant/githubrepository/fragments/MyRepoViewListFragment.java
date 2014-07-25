package ar.com.globant.githubrepository.fragments;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.egit.github.core.Repository;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import ar.com.globant.githubrepository.R;
import ar.com.globant.githubrepository.RepositoriesActivity;
import ar.com.globant.githubrepository.adapter.ListRepoCustomAdapter;
import ar.com.globant.githubrepository.contentprovider.MyGitHubContentProvider;
import ar.com.globant.githubrepository.loader.MyRepoListLoader;
import ar.com.globant.githubrepository.sql.MySQLiteHelper;
import ar.com.globant.globant.model.WrapperItem;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;


public class MyRepoViewListFragment extends ListFragment implements OnQueryTextListener, LoaderManager.LoaderCallbacks<List<Repository>>{

	static List<WrapperItem> apps = new ArrayList<WrapperItem>(){};
	
	ListRepoCustomAdapter mAdapter;

	private MenuItem refreshMenuItem;
	
	static String name;
	static String pass;
	
	
	public static MyRepoViewListFragment newInstance(String username, String password) {
		
		MyRepoViewListFragment lf = new MyRepoViewListFragment();
		
		name = username;
		pass = password;
		
		return lf; 
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		((RepositoriesActivity)getActivity()).setMyRepoViewListFragment(getId());
		
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		mAdapter = new ListRepoCustomAdapter(getActivity(), R.layout.repo_request_row, apps);
		setListAdapter(mAdapter);
		
		setListShown(false);
		
		setEmptyText(getResources().getText(R.string.repo_and_user_error));
		
		setHasOptionsMenu(true);
		
		getLoaderManager().initLoader(0, null, this);
	}
	
	@Override
	public Loader<List<Repository>> onCreateLoader(int arg0, Bundle arg1) {
		return new MyRepoListLoader(getActivity(), name);
	}
	
	@Override
	public void onLoadFinished(Loader<List<Repository>> arg0, List<Repository> data) {
		
		if ( refreshMenuItem != null ) {
			refreshMenuItem.collapseActionView();
			refreshMenuItem.setActionView(null);
		}
		
		if ( data == null ) {
        	Crouton.makeText(getActivity(), R.string.repo_and_user_error, Style.ALERT).show();
        	
        	data = new ArrayList<Repository>();
		} else {
			if ( !userExist(name) ) {
				ContentValues cv = new ContentValues();
				cv.put(MySQLiteHelper.GITHUB_COLUMNA_USERNAME, name);
				cv.put(MySQLiteHelper.GITHUB_COLUMNA_PASSWORD, pass);
				
				getActivity().getContentResolver().insert(MyGitHubContentProvider.CONTENT_URI, cv);
			}
		}
		
		if (isResumed()) {
			setListShown(true);
		} else {
			setListShownNoAnimation(true);
		}
		
		mAdapter.clear();
		mAdapter.setData(data);
		
		// Voice Speak Filter
		String searchVoiceText = ((RepositoriesActivity)getActivity()).getSearchVoiceText();
		if ( searchVoiceText != null && !searchVoiceText.isEmpty() )
			doFilter( searchVoiceText );
		
		mAdapter.notifyDataSetChanged();
	}
	
	private boolean userExist(String name) {
		String[] columns = new String[] { MySQLiteHelper.GITHUB_ID,
				MySQLiteHelper.GITHUB_COLUMNA_USERNAME,
				MySQLiteHelper.GITHUB_COLUMNA_PASSWORD };
		
		Cursor cursor = getActivity().getContentResolver().query(MyGitHubContentProvider.CONTENT_URI,
																 columns, null, null, null);
		
		if ( cursor != null ) {
			while ( cursor.moveToNext() )
				if ( name.equalsIgnoreCase(cursor.getString(cursor.getColumnIndex(MySQLiteHelper.GITHUB_COLUMNA_USERNAME))) ) {
					cursor.close();
					
					return true;
				}
		}
		
		return false;
	}

	@Override
	public void onLoaderReset(Loader<List<Repository>> arg0) {
		mAdapter.clear();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		
		Crouton.cancelAllCroutons();
	}
	
    @Override 
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    	inflater.inflate(R.menu.menu, menu);
    	
    	SearchManager searchManager = (SearchManager)getActivity().getSystemService(Context.SEARCH_SERVICE);
        SearchableInfo si = searchManager.getSearchableInfo( new ComponentName(getActivity().getApplicationContext(), RepositoriesActivity.class) );
        
        List<SearchableInfo> searchables = searchManager.getSearchablesInGlobalSearch();
        
    	MenuItem item = menu.findItem(R.id.action_search);
    	SearchView searchView = (SearchView) item.getActionView();
    	searchView.setSearchableInfo(si);
    	searchView.setOnQueryTextListener(this);
    	searchView.setQueryHint(getResources().getText(R.string.search));
    	
    	super.onCreateOptionsMenu(menu, inflater);
    }
	
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
			case R.id.action_refresh:
				
				refreshMenuItem = item;
				refreshMenuItem.setActionView(R.layout.action_progressbar);
				refreshMenuItem.expandActionView();
				
				getLoaderManager().restartLoader(0, null, this);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
    }
    
	@Override
	public boolean onQueryTextChange(String newText) {
		
        String newFilter = !TextUtils.isEmpty(newText) ? newText : null;
        
        doFilter(newFilter);
        
		return true;
	}
	
	public void doFilter(String newFilter) {
		mAdapter.getFilter().filter(newFilter);
	}
	
	@Override
	public boolean onQueryTextSubmit(String arg0) {
		return true;
	}
}
