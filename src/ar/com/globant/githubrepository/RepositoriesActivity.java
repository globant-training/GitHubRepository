package ar.com.globant.githubrepository;

import java.io.IOException;

import org.eclipse.egit.github.core.MergeStatus;
import org.eclipse.egit.github.core.PullRequest;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.RepositoryId;
import org.eclipse.egit.github.core.service.PullRequestService;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import ar.com.globant.githubrepository.adapter.MyFragmentPageAdapter;
import ar.com.globant.githubrepository.dialog.MyDialogFragment;
import ar.com.globant.githubrepository.dialog.MyMergeDialogFragment;
import ar.com.globant.githubrepository.events.PageSelectEvent;
import ar.com.globant.githubrepository.fragments.MyPullRequestViewFragment;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

public class RepositoriesActivity extends ActionBarActivity implements ActionBar.TabListener{
	
	
	private String username = null;
	private String password = null;
	
	private ViewPager viewPager;
	private MyFragmentPageAdapter adapterViewPage;
	
	private DialogFragment mDialogLoaging;
	private MyMergeDialogFragment mDialogMerge;
	
	private MergePRTask mt = null;
	
	private int myPullRequestViewFragment;
	private Repository selectedRepo;
	private boolean pressSelected = false;
	
	private PullRequest pullrequest = null;
	
	private Bus bus = new Bus();
	
	
	
	
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_slide);
        
        bus.register(this);
        
        final ActionBar actionBar =  getSupportActionBar();
 		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        
        username = getIntent().getExtras().getString("username"); 
        password = getIntent().getExtras().getString("password");
        
		viewPager = (ViewPager) findViewById(R.id.pager);
		adapterViewPage = new MyFragmentPageAdapter(getSupportFragmentManager(), username, password);
		viewPager.setAdapter(adapterViewPage);
		
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int pageNumber) {
				bus.post(new PageSelectEvent(pageNumber, actionBar));
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				
			}
		});
		
		for (int i = 0; i < adapterViewPage.getCount(); i++)
			actionBar.addTab(actionBar.newTab().setText(adapterViewPage.getPageTitle(i))
					                           .setTabListener(this));
    }
    
    @Subscribe
    public void handlerViewPagerPageSelect(PageSelectEvent e) {
    	int pageNumber = e.getPageNumber();
    	
    	if (pageNumber == 1 && pressSelected) {
			
			FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
			mDialogLoaging = MyDialogFragment.newInstance();
			mDialogLoaging.show(ft, "dialog");
		} else if ( selectedRepo == null ) {
			int B = getMyPullRequestViewFragment();
			MyPullRequestViewFragment myPullRequestViewFragment = (MyPullRequestViewFragment) getSupportFragmentManager().findFragmentById(B);
			
			myPullRequestViewFragment.notifyNoRepositorySelected();
		}
		
		pressSelected = false;
		
		e.getActionBar().setSelectedNavigationItem(pageNumber);
    }
    
    @Override
    protected void onDestroy() {
    	super.onDestroy();
    	
       	bus.unregister(this);

    	mDialogLoaging = null;
    }
    
	@Override
    protected void onResume() {
    	super.onResume();
    	
        mt = (MergePRTask) getLastCustomNonConfigurationInstance();
    }
	
	public void selectRepo(View v) {
		pressSelected  = true;
		
		Button button = (Button) v;
		Repository repo = (Repository) button.getTag();
		
		selectedRepo = repo; 
		
		int B = this.getMyPullRequestViewFragment();
		MyPullRequestViewFragment myPullRequestViewFragment = (MyPullRequestViewFragment) this.getSupportFragmentManager().findFragmentById(B);
		
		myPullRequestViewFragment.setRepository(repo);
		
		viewPager.setCurrentItem(1, true);
	}
	
	public void mergeRepo(View v) {
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		
		pullrequest = (PullRequest)v.getTag();
		v.setEnabled(false);
		
		mDialogMerge = MyMergeDialogFragment.newInstance();
		mDialogMerge.setActivity(this);
		mDialogMerge.show(ft, "dialog");
	}
	
	public void doMergeRepo(String commitText) {
		try {
			mergePullRequest(pullrequest, commitText);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
    public void mergePullRequest(PullRequest pr, String mergeMsj) throws IOException {
    	mt = new MergePRTask(RepositoriesActivity.this, mergeMsj);
    	
    	mt.execute(pr);
    }
    
    // AsyncTask
    private class MergePRTask extends AsyncTask<PullRequest, Void, MergeStatus> {
    	
        private RepositoriesActivity mActivity;
        private String msj;
        
    	public MergePRTask(RepositoriesActivity activity, String mergeMsj) {
    		msj = mergeMsj;
    		
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
					status  = service.merge(repoId, param[0].getNumber(), msj);
			} catch (IOException e) {
				e.printStackTrace();
			}
    		
            return status;
        }
    	
        @Override
        protected void onProgressUpdate(Void... values) {
        	
        }
        
        protected void onPostExecute(MergeStatus status) {
        	mActivity.dialogMergeDialogDismiss();
        	
        	mActivity.smashResult(status.getMessage());
        }
    }
    
    @Override
    public Object onRetainCustomNonConfigurationInstance() {
    	mt.detach();
    	
    	return mt;
    }
    
    public void dialogMergeDialogDismiss() {
		mDialogMerge.dismiss();
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

	public void setViewPage(int i) {
		viewPager.setCurrentItem(i, true);
	}
	
	public void dialogDismiss(){
		mDialogLoaging.dismiss();
	}
	
	// ActionBar.TabListener methods
	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
	}
	
	@Override
	public void onTabSelected(Tab tab, FragmentTransaction arg1) {
		viewPager.setCurrentItem(tab.getPosition());
	}
	
	@Override
	public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {
	}
}
