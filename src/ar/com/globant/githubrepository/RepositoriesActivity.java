package ar.com.globant.githubrepository;

import java.io.IOException;

import org.eclipse.egit.github.core.MergeStatus;
import org.eclipse.egit.github.core.PullRequest;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.RepositoryId;
import org.eclipse.egit.github.core.service.PullRequestService;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import ar.com.globant.githubrepository.adapter.MyFragmentPageAdapter;
import ar.com.globant.githubrepository.dialog.MyDialogFragment;
import ar.com.globant.githubrepository.dialog.MyMergeDialogFragment;
import ar.com.globant.githubrepository.fragments.MyPullRequestViewFragment;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

public class RepositoriesActivity extends ActionBarActivity implements ActionBar.TabListener{
	
	
	private String username = null;
	private String password = null;
	
	private ViewPager viewPager;
	private MyFragmentPageAdapter adapterViewPage;
	
	private DialogFragment mDialogLoaging;
	private MyMergeDialogFragment mDialogMerge;
	
	private MergePRTask mt = null;
	
	private int myPullRequestViewFragment;
	private int myRepoViewListFragment;
	private Repository selectedRepo;
	private boolean pressSelected = false;
	
	private PullRequest pullrequest = null;
	private String searchVoiceText = null;
	
	
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_slide);
        
        setTitle(null);
        
        SharedPreferences sp = this.getSharedPreferences(getApplicationContext().getPackageName(), Context.MODE_PRIVATE);
        username = getIntent().getExtras().getString("username");
        if ( username == null )
        	username = sp.getString("username", null);
        
        password = getIntent().getExtras().getString("password");
        if ( password == null )
        	password = sp.getString("password", null);
        
 		final ActionBar actionBar = getSupportActionBar();
 		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
 		actionBar.setHomeButtonEnabled(true);
 		actionBar.setDisplayHomeAsUpEnabled(true);
 		
		viewPager = (ViewPager) findViewById(R.id.pager);
		adapterViewPage = new MyFragmentPageAdapter(getSupportFragmentManager(), username, password);
		viewPager.setAdapter(adapterViewPage);
		
        // TODO Remove Anonymous Class
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int pageNumber) {
				if (pageNumber == 1 && pressSelected) {
					
					FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
					mDialogLoaging = MyDialogFragment.newInstance();
					mDialogLoaging.show(ft, "dialog");
				} else if ( selectedRepo == null ){
					int B = getMyPullRequestViewFragment();
					MyPullRequestViewFragment myPullRequestViewFragment = (MyPullRequestViewFragment) getSupportFragmentManager().findFragmentById(B);
					
					myPullRequestViewFragment.notifyNoRepositorySelected();
				}
				
				pressSelected = false;
				
				actionBar.setSelectedNavigationItem(pageNumber);
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
    
    @Override
    protected void onStart() {
    	super.onStart();
    	
    	// Search Voice 
        final Intent queryIntent = getIntent();
        final String queryAction = getIntent().getAction();
        if ( Intent.ACTION_SEARCH.equals( queryAction ) )
            setSearchVoiceText( queryIntent.getStringExtra(SearchManager.QUERY) );
    }
    
    @Override
    protected void onStop() {
		SharedPreferences sp = getSharedPreferences(getApplicationContext().getPackageName(), Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		
		editor.putString("username", username);
		editor.putString("password", password);
		editor.commit();
		
    	super.onStop();
    }
    
    @Override
    protected void onDestroy() {
    	super.onDestroy();
    	
    	Crouton.cancelAllCroutons();
    	
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
    	if ( mt != null )
    		mt.detach();
    	
    	return mt;
    }
    
    public void dialogMergeDialogDismiss() {
		mDialogMerge.dismiss();
	}

	private void smashResult(String message) {
		Crouton.makeText(RepositoriesActivity.this, message, Style.INFO).show();
	}
    
	public int getMyPullRequestViewFragment() {
		return myPullRequestViewFragment;
	}
	
	public void setMyPullRequestViewFragment(int myPullRequestViewFragment) {
		this.myPullRequestViewFragment = myPullRequestViewFragment;
	}
	
	public int getMyRepoViewListFragment() {
		return myPullRequestViewFragment;
	}
	
	public void setMyRepoViewListFragment(int myRepoViewListFragment) {
		this.myRepoViewListFragment = myRepoViewListFragment;
	}
	
	public String getSearchVoiceText() {
		return searchVoiceText;
	}

	public void setSearchVoiceText(String searchVoiceText) {
		this.searchVoiceText = searchVoiceText;
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
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			Intent intent = new Intent(this, GitHubMainActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			
			startActivity(intent);
			
			break;

		default:
			break;
		}
		
		return super.onOptionsItemSelected(item);
	}
}
