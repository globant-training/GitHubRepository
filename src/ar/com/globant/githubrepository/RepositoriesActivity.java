package ar.com.globant.githubrepository;

import java.io.IOException;

import org.eclipse.egit.github.core.MergeStatus;
import org.eclipse.egit.github.core.PullRequest;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.RepositoryId;
import org.eclipse.egit.github.core.service.PullRequestService;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import ar.com.globant.githubrepository.adapter.MyFragmentPageAdapter;
import ar.com.globant.githubrepository.fragments.MyPullRequestViewFragment;

public class RepositoriesActivity extends FragmentActivity {
	
	private String username = null;
	private String password = null;
	
	private ViewPager viewPager;
	private MyFragmentPageAdapter adapterViewPage;
	
	private int myPullRequestViewFragment;
	private Repository selectedRepo;
	
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_slide);
        
        username = getIntent().getExtras().getString("username"); 
        password = getIntent().getExtras().getString("password");
        
		viewPager = (ViewPager) findViewById(R.id.pager);
		adapterViewPage = new MyFragmentPageAdapter(getSupportFragmentManager(), username, password);
		viewPager.setAdapter(adapterViewPage);
    }
    
	
	public void selectRepo(View v) {
		Button button = (Button) v;
		Repository repo = (Repository) button.getTag();
		
		selectedRepo = repo; 
		
		int B = this.getMyPullRequestViewFragment();
		
		MyPullRequestViewFragment myPullRequestViewFragment = (MyPullRequestViewFragment) this.getSupportFragmentManager().findFragmentById(B);
		
		myPullRequestViewFragment.setRepository(repo);
		
		viewPager.setCurrentItem(1, true);
	}
	
	public void mergeRepo(View v) {	
		PullRequest pullrequest = (PullRequest)v.getTag();
		
		v.setEnabled(false);
		
		//TODO msj
		try {
			mergePullRequest(pullrequest, "");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
    public void mergePullRequest(PullRequest pr, String mergeMsj) throws IOException {
    	MergePRTask mt = new MergePRTask(RepositoriesActivity.this);
    	
    	mt.execute(pr);
    }
    
    // AsyncTask
    private static class MergePRTask extends AsyncTask<PullRequest, Void, MergeStatus> {
        private RepositoriesActivity mActivity;
        
    	public MergePRTask(RepositoriesActivity activity) {
    		attach(activity);
		}
    	
		public void detach() {
    		mActivity = null;
    	}
    	
    	public void attach(RepositoriesActivity activity) {
    		mActivity = activity;
    	}
    	
    	// TODO Msj
    	protected MergeStatus doInBackground(PullRequest... param) {
    		
    		PullRequestService service = new PullRequestService();

    		service.getClient().setCredentials(mActivity.getUsername(), 
    										   mActivity.getPassword());
    		RepositoryId repoId = RepositoryId.createFromId(mActivity.getSelectedRepo().generateId());
    		
    		MergeStatus status = null;
    		try {
				if (service.getPullRequest(repoId, param[0].getNumber()).isMergeable())
					status  = service.merge(repoId, param[0].getNumber(), "-");
			} catch (IOException e) {
				e.printStackTrace();
			}
    		
            return status;
        }
    	
        @Override
        protected void onProgressUpdate(Void... values) {
        	
        }
        
        protected void onPostExecute(MergeStatus status) {
        	mActivity.smashResult(status.getMessage());
        	
        	
        }
    }
    
    private void smashResult(String message) {
		Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
	}
    
	public int getMyPullRequestViewFragment() {
		return myPullRequestViewFragment;
	}

	public void setMyPullRequestViewFragment(int myPullRequestViewFragment) {
		this.myPullRequestViewFragment = myPullRequestViewFragment;
	}


	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public Repository getSelectedRepo() {
		return selectedRepo;
	}


	public void setSelectedRepo(Repository selectedRepo) {
		this.selectedRepo = selectedRepo;
	}
}
