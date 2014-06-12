package ar.com.globant.githubrepository.adapter;

import java.util.Locale;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import ar.com.globant.githubrepository.fragments.MyPullRequestViewFragment;
import ar.com.globant.githubrepository.fragments.MyRepoViewListFragment;

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
			return MyRepoViewListFragment.newInstance(username);
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
	
	@Override
	public CharSequence getPageTitle(int position) {
		Locale l = Locale.getDefault();
		
		switch (position) {
		case 0:
			return "Repositories";
		case 1:
			return "Pull Requests";
		
		}
		return null;
	}
}
