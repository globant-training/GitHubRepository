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
import ar.com.globant.githubrepository.adapter.ListCustomAdapter;
import ar.com.globant.githubrepository.loader.MyListLoader;
import ar.com.globant.globant.model.WrapperItem;


public class MyRepoViewListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<List<Repository>>{

	static List<WrapperItem> apps = new ArrayList<WrapperItem>(){};
	
	ListCustomAdapter mAdapter;
	
	static String name;
	
	private static List<Repository> listRepositories;
	
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
		
		mAdapter = new ListCustomAdapter(getActivity(), R.layout.repo_request_row, apps);
		setListAdapter(mAdapter);
		
		setListShown(false);
		
        getLoaderManager().initLoader(0, null, this);
	}
	
		@Override
	public Loader<List<Repository>> onCreateLoader(int arg0, Bundle arg1) {
		return new MyListLoader(getActivity(), name);
	}
	
	@Override
	public void onLoadFinished(Loader<List<Repository>> arg0, List<Repository> data) {

		mAdapter.setData(data);
		
        if (isResumed()) {
            setListShown(true);
        } else {
            setListShownNoAnimation(true);
        }		
	}

	@Override
	public void onLoaderReset(Loader<List<Repository>> arg0) {
		mAdapter.clear();
	}
}
