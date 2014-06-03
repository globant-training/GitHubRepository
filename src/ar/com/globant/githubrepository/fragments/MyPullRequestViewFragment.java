package ar.com.globant.githubrepository.fragments;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.eclipse.egit.github.core.PullRequest;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.service.PullRequestService;
import org.eclipse.egit.github.core.service.RepositoryService;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;
import ar.com.globant.githubrepository.R;
import ar.com.globant.githubrepository.RepositoriesActivity;
import ar.com.globant.githubrepository.adapter.ListPullRequestCustomAdapter;
import ar.com.globant.globant.model.WrapperPRItem;

public class MyPullRequestViewFragment extends Fragment {
	
	private View view = null;
	private ListPullRequestCustomAdapter adapter = null;
	private ListView listaCustom 	  = null;
	private static String password    = null;
	private static String username    = null;
	private PullRequestListTask task  = null;
	private Repository repository     = null;
	
	private List<WrapperPRItem> lista = new ArrayList<WrapperPRItem>();
	
	
	public static MyPullRequestViewFragment newInstance(String titulo, String username, String password) {
		
		MyPullRequestViewFragment tvf = new MyPullRequestViewFragment();
		
		tvf.setUsername(username);
		tvf.setPassword(password);
		
		return tvf; 
	}
	
	private void setPassword(String pass) {
		this.password = pass;
	}
	
	private void setUsername(String user) {
		this.username = user;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_textview, container, false);
		
		//TODO
		listaCustom = (ListView) view.findViewById(R.id.listViewResult);
		adapter = new ListPullRequestCustomAdapter(view.getContext(), R.layout.pull_request_row, lista);
		listaCustom.setAdapter(adapter);
		
		((RepositoriesActivity)getActivity()).setMyPullRequestViewFragment(getId());
		
		int color;
	    Random rnd = new Random(); 
	    color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));   
	    view.setBackgroundColor(color);		
	    
		Log.e("INFO", "Creando el Fragment");
		
		return view; 
	}
	
	public void taskExecute() {
		task.execute();
	}
	
	@Override
	public void onDestroyView() {	
		super.onDestroyView();
		
		Log.e("INFO", "Destruyendo el Fragment");
	}
	
    // AsyncTask
    private static class PullRequestListTask extends AsyncTask<Repository, Void, List<PullRequest>> {
        private MyPullRequestViewFragment mActivity;
        private List<PullRequest> results = null;
        
		public PullRequestListTask(MyPullRequestViewFragment activity) {
    		attach(activity);
		}
    	
		public void detach() {
    		mActivity = null;
    	}
    	
    	public void attach(MyPullRequestViewFragment activity) {
    		mActivity = activity;
    	}
    	
    	protected List<PullRequest> doInBackground(Repository... param) {
    		// Repos from Userrepo
    		
    		RepositoryService service = new RepositoryService();
        	
    		try {
    	    	PullRequestService servicePR = new PullRequestService();
    	    	// TODO traer credenciales de modelo
    			servicePR.getClient().setCredentials(username, password);
    			
    			return servicePR.getPullRequests(param[0], "open");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
    		
            return results;
        }
    	
        @Override
        protected void onProgressUpdate(Void... values) {      
        	
        }
        
        protected void onPostExecute(List<PullRequest> result) {
        	mActivity.setListResults(result);
        }
    }
    
    private void setListResults(List<PullRequest> results) {
    	if (results != null && !results.isEmpty())
    		toMyList(results, false);
    	else
			Toast.makeText(getActivity().getApplicationContext(), "No Open Pull Request found in " + repository.getName(), Toast.LENGTH_SHORT).show();
    }
    
    private void toMyList(List<PullRequest> results, boolean doSave) {
       	for (PullRequest result : results) {
    		lista.add(new WrapperPRItem(result.getTitle(), result));
    		
    		Log.i("INFO", result.toString());
       	}
    	
    	adapter.notifyDataSetChanged();
	}
    
	public void setRepository(Repository repo) {
		repository = repo;
		
		PullRequestListTask task = new PullRequestListTask(MyPullRequestViewFragment.this);

		task.execute(repo);
	}
	
}
