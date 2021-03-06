package ar.com.globant.githubrepository;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class MyFragmentPageAdapter extends FragmentStatePagerAdapter {

	private static final int NUM_PAGES = 2;
	private String username = null;
	private String password = null;
	
	
	public MyFragmentPageAdapter(FragmentManager fm, String username, String password) {
		super(fm);
		
		this.username = username;
		this.password = password;
	}
	
	@Override
	public Fragment getItem(int position) {
		switch (position) {
		case 0: 
			return MyRepoViewFragment.newInstance("Repositories", username);
		case 1:
			return MyPullRequestViewFragment.newInstance("Pull Request", username, password);
		default:
			return null;
		}
	}
	
	@Override
	public int getCount() {
		return NUM_PAGES;
	}
	
}
