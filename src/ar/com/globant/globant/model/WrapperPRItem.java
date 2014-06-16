package ar.com.globant.globant.model;

import org.eclipse.egit.github.core.PullRequest;


public class WrapperPRItem {

	private String title;
	private PullRequest pr;

	
	public WrapperPRItem(String title, PullRequest pullrequest) {
		super();
		this.title = title;
		this.pr = pullrequest;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public PullRequest getPullRequest() {
		return pr;
	}

	public void setPullRequest(PullRequest pullrequest) {
		this.pr = pullrequest;
	}
	
}

