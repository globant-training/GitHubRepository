package ar.com.globant.githubrepository.fragments;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.widget.ArrayAdapter;

public class MyRepoViewListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<List<String>>{

	static List<String> apps = new ArrayList<String>(){};
	ArrayAdapter<String> adapter;
	
	public static MyRepoViewListFragment newInstance() {
		
		MyRepoViewListFragment lf = new MyRepoViewListFragment();
		
		return lf; 
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {

		super.onActivityCreated(savedInstanceState);
		
		setListShown(false);

		adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, apps);

		setListAdapter(adapter);
		
        getLoaderManager().initLoader(0, null, this);
	}
	
	/////////////////////////
	@Override
	public Loader<List<String>> onCreateLoader(int arg0, Bundle arg1) {
        return new AppListLoader(getActivity());
	};
	
	@Override
	public void onLoadFinished(Loader<List<String>> arg0, List<String> arg1) {

		adapter.addAll(arg1);
		
        if (isResumed()) {
            setListShown(true);
        } else {
            setListShownNoAnimation(true);
        }		
	}
	
	@Override
	public void onLoaderReset(Loader<List<String>> arg0) {
		
	}
	
    ///////////////////////	
    public static class AppListLoader extends AsyncTaskLoader<List<String>> {

        List<String> mApps;

        public AppListLoader(Context context) {
            super(context);
        }

        @Override public List<String> loadInBackground() {
        	ArrayList<String> list = new ArrayList<String>(Arrays.asList( "Android", "iPhone", "WindowsMobile","Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X","Linux", "OS/2" ));
        	
			ArrayList<String> app =  new ArrayList<String>(){};
        	
        	for (String e : list) {
        		app.add(e);
        		
        		try {
					Thread.sleep(200);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
        	}

            // Done!
            return app;
        }
        
        @Override public void deliverResult(List<String> list) {
            if (isReset()) {
                if (list != null) {
                    onReleaseResources(list);
                }
            }
            List<String> oldApps = list;
            mApps = list;

            if (isStarted()) {
                super.deliverResult(list);
            }

            if (oldApps != null) {
                onReleaseResources(oldApps);
            }
        }

        @Override protected void onStartLoading() {
            if (mApps != null) {
                deliverResult(mApps);
            }

            if (takeContentChanged() || mApps == null ) {
                forceLoad();
            }
        }

        @Override protected void onStopLoading() {
            cancelLoad();
        }

        @Override public void onCanceled(List<String> apps) {
            super.onCanceled(apps);

            onReleaseResources(apps);
        }

        @Override protected void onReset() {
            super.onReset();
        }

        protected void onReleaseResources(List<String> apps) {

        }
    }
}
