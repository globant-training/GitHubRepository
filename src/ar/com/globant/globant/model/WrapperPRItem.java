package ar.com.globant.globant.model;

import org.eclipse.egit.github.core.PullRequest;


public class WrapperPRItem {

	private String title;
	private PullRequest repo;

	public WrapperPRItem(String title, PullRequest repo) {
		super();
		this.title = title;
		this.repo = repo;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public PullRequest getPullRequest() {
		return repo;
	}

	public void setPullRequest(PullRequest repo) {
		this.repo = repo;
	}
	
}

