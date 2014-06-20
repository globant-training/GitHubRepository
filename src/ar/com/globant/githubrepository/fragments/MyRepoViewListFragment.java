package ar.com.globant.githubrepository.fragments;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.egit.github.core.Repository;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import ar.com.globant.githubrepository.R;
import ar.com.globant.githubrepository.adapter.ListRepoCustomAdapter;
import ar.com.globant.githubrepository.loader.MyRepoListLoader;
import ar.com.globant.globant.model.WrapperItem;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;


public class MyRepoViewListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<List<Repository>>{

	static List<WrapperItem> apps = new ArrayList<WrapperItem>(){};
	
	ListRepoCustomAdapter mAdapter;
	
	static String name;
	
	
	public static MyRepoViewListFragment newInstance(String username) {
		
		MyRepoViewListFragment lf = new MyRepoViewListFragment();
		
		name = username;
		
		return lf; 
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		mAdapter = new ListRepoCustomAdapter(getActivity(), R.layout.repo_request_row, apps);
		setListAdapter(mAdapter);
		
		setEmptyText(getResources().getText(R.string.repo_and_user_error));
		
		setListShown(false);
		
        getLoaderManager().initLoader(0, null, this);
	}
	
	@Override
	public Loader<List<Repository>> onCreateLoader(int arg0, Bundle arg1) {
		return new MyRepoListLoader(getActivity(), name);
	}
	
	@Override
	public void onLoadFinished(Loader<List<Repository>> arg0, List<Repository> data) {
		
		if ( data == null ) {
        	Crouton.makeText(getActivity(), R.string.repo_and_user_error, Style.ALERT).show();
        	
        	data = new ArrayList<Repository>();
		}
		
		if (isResumed()) {
			setListShown(true);
		} else {
			setListShownNoAnimation(true);
		}
		
		mAdapter.setData(data);
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
}
