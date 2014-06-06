package ar.com.globant.githubrepository.fragments;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.egit.github.core.PullRequest;
import org.eclipse.egit.github.core.Repository;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import ar.com.globant.githubrepository.R;
import ar.com.globant.githubrepository.adapter.ListPullRequestCustomAdapter;
import ar.com.globant.githubrepository.loader.MyPullRequestListLoader;
import ar.com.globant.globant.model.WrapperPRItem;


public class MyPullRequestListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<List<PullRequest>>{

	private List<WrapperPRItem> apps = new ArrayList<WrapperPRItem>(){};
	
	private ListPullRequestCustomAdapter mAdapter;
	
	private static String name;
	private static String pass;
	
	private Repository repo;
	
	
	public static MyPullRequestListFragment newInstance(String username, String password) {
		
		MyPullRequestListFragment lf = new MyPullRequestListFragment();
		
		name = username;
		pass = password;
		
		return lf; 
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		mAdapter = new ListPullRequestCustomAdapter(getActivity(), R.layout.repo_request_row, apps);
		setListAdapter(mAdapter);
		
		setListShown(false);
		
		setEmptyText("No Open Pull Request found");
		
        getLoaderManager().initLoader(0, null, this);
	}
	
	// Loader Callbacks
	@Override
	public Loader<List<PullRequest>> onCreateLoader(int arg0, Bundle arg1) {
		return new MyPullRequestListLoader(getActivity(), repo, name, pass);
	}
	
	@Override
	public void onLoadFinished(Loader<List<PullRequest>> arg0, List<PullRequest> data) {
		
		mAdapter.setData(data);
		
        if (isResumed()) {
            setListShown(true);
        } else {
            setListShownNoAnimation(true);
        }		
	}
	
	@Override
	public void onLoaderReset(Loader<List<PullRequest>> arg0) {
		mAdapter.clear();
	}
	
	public void setRepository(Repository repository) {
		repo = repository;
	}
}
