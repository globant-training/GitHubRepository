package ar.com.globant.githubrepository.loader;

import java.io.IOException;
import java.util.List;

import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.service.RepositoryService;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;


public class MyRepoListLoader extends AsyncTaskLoader<List<Repository>> {
	    
		private List<Repository> listRepositories;
		private String name;
	
		private List<Repository> results = null;
	    
		private List<Repository> myList;
		
		
		public MyRepoListLoader(Context context, String username) {
		  super(context);
		  
		  name = username;
		}
		
		@Override 
		public List<Repository> loadInBackground() {
			RepositoryService service = new RepositoryService();
	    	
			try {
				listRepositories =  service.getRepositories(name);
				
				for (Repository repo : listRepositories) 
					Log.i("INFO", repo.getName() + " Watchers: " + repo.getWatchers() + " IDs: " + repo.getId());
					
				results = listRepositories;
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
	        return results;
		}
		
		@Override 
		public void deliverResult(List<Repository> list) {
		  if (isReset()) {
		      if (list != null) {
		          onReleaseResources(list);
		      }
		  }
		  List<Repository> oldApps = list;
		  myList = list;
		
		  if (isStarted()) {
		      super.deliverResult(list);
		  }
		
		  if (oldApps != null) {
		      onReleaseResources(oldApps);
		  }
		}
		
		@Override 
		protected void onStartLoading() {
		  if (myList != null) {
		      deliverResult(myList);
		  }
		
		  if (takeContentChanged() || myList == null ) {
		      forceLoad();
		  }
		}
		
		@Override 
		protected void onStopLoading() {
		  cancelLoad();
		}
		
		@Override 
		public void onCanceled(List<Repository> apps) {
		  super.onCanceled(apps);
		
		  onReleaseResources(apps);
		}
		
		@Override 
		protected void onReset() {
		  super.onReset();
		}
		
		protected void onReleaseResources(List<Repository> apps) {
		
		}
}