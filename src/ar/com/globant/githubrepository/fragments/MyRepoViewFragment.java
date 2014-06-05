package ar.com.globant.githubrepository.fragments;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.RepositoryTag;
import org.eclipse.egit.github.core.service.RepositoryService;

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
import ar.com.globant.githubrepository.adapter.ListRepoCustomAdapter;
import ar.com.globant.globant.model.WrapperItem;

public class MyRepoViewFragment extends Fragment {
	
	private View view = null;
	private ListRepoCustomAdapter adapter = null;
	private ListView listaCustom = null;
	private String username;
	
	private static List<WrapperItem> lista = new ArrayList<WrapperItem>();
	private static List<Repository> listRepositories;
	
	
	public static MyRepoViewFragment newInstance(String titulo, String username) {
		MyRepoViewFragment tvf = new MyRepoViewFragment();
		
		tvf.setUsername(username);
		
		Bundle b = new Bundle();
		
		b.putString("Texto", titulo);
		tvf.setArguments(b);
		
		return tvf; 
	}
	
	private void setUsername(String username) {
		this.username = username;
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_textview, container, false);
		
    	RepoListTask task = new RepoListTask(MyRepoViewFragment.this);
		task.execute(username);
		
		listaCustom = (ListView) view.findViewById(R.id.listViewResult);
		adapter = new ListRepoCustomAdapter(view.getContext(), R.layout.repo_request_row, lista);
		listaCustom.setAdapter(adapter);
		
		Log.e("INFO", "Creando el Fragment");
		
		return view; 
	}
	
	@Override
	public void onDestroyView() {	
		super.onDestroyView();
		
		Log.e("INFO", "Destruyendo el Fragment");
	}
	
    // AsyncTask
    private static class RepoListTask extends AsyncTask<String, Void, List<Repository>> {
        private MyRepoViewFragment mActivity;
        private List<Repository> results = null;
        
        
    	public RepoListTask(MyRepoViewFragment activity) {
    		attach(activity);
		}
    	
		public void detach() {
    		mActivity = null;
    	}
    	
    	public void attach(MyRepoViewFragment activity) {
    		mActivity = activity;
    	}
    	
    	protected List<Repository> doInBackground(String... param) {
    		
    		RepositoryService service = new RepositoryService();
        	
    		try {
				listRepositories =  service.getRepositories(param[0]);
				
				for (Repository repo : listRepositories) 
					Log.i("INFO", repo.getName() + " Watchers: " + repo.getWatchers() + " IDs: " + repo.getId());
					
				results = listRepositories;
			} catch (IOException e1) {
				e1.printStackTrace();
			}
    		
    		Collections.sort(results, REPOSITORY_COMPARATOR);
    		
            return results;
        }
    	
        @Override
        protected void onProgressUpdate(Void... values) {      
        	
        }
        
        protected void onPostExecute(List<Repository> result) {
        	if (result != null)
        		mActivity.setListResults(result);
        	else
				mActivity.setErrorMsj("Repositories not Found or User no exist!");
        }
    }
    
    public void setErrorMsj(String msj) {
		Toast.makeText(getActivity().getApplicationContext(), msj, Toast.LENGTH_LONG).show();
	}

	private void setListResults(List<Repository> results) {
    	toMyList(results, false);
    }
    
    private void toMyList(List<Repository> results, boolean doSave) {
       	for (Repository result : results)
    		lista.add(new WrapperItem(result.getName(), 
    								  result,
    								  result.getDescription(),
    								  result.getLanguage()));
    	
    	adapter.notifyDataSetChanged();
	}
    
    public static final Comparator<Repository> REPOSITORY_COMPARATOR = new Comparator<Repository>() {

		@Override
		public int compare(Repository repo1, Repository repo2) {
			if ( repo1 != null && repo2 != null )
				return repo1.getName().compareToIgnoreCase(repo2.getName());
			return 0;
		}
    	
    };
}
