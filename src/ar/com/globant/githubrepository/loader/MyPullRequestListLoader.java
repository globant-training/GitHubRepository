package ar.com.globant.githubrepository.loader;

import java.io.IOException;
import java.util.List;

import org.eclipse.egit.github.core.PullRequest;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.service.PullRequestService;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;


public class MyPullRequestListLoader extends AsyncTaskLoader<List<PullRequest>> {
	    
		private Repository repository;
		
		private List<PullRequest> results = null;
	    
		private List<PullRequest> mApps;
		private String user;
		private String pass;
		
		public MyPullRequestListLoader(Context context, Repository repo, String username, String password) {
			super(context);
			
			repository = repo;
			user = username;
			pass = password;
		}
		
		@Override public List<PullRequest> loadInBackground() {
			
    		try {
    	    	PullRequestService servicePR = new PullRequestService();
    	    	// TODO traer credenciales de modelo
    			servicePR.getClient().setCredentials(user, pass);
    			
    			return servicePR.getPullRequests(repository, "open");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
	        return results;
		}
		
		@Override public void deliverResult(List<PullRequest> list) {
		  if (isReset()) {
		      if (list != null) {
		          onReleaseResources(list);
		      }
		  }
		  List<PullRequest> oldApps = list;
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
		
		@Override public void onCanceled(List<PullRequest> apps) {
		  super.onCanceled(apps);
		
		  onReleaseResources(apps);
		}
		
		@Override protected void onReset() {
		  super.onReset();
		}
		
		protected void onReleaseResources(List<PullRequest> apps) {
		
		}
}