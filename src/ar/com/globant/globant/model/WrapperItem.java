package ar.com.globant.globant.model;

import org.eclipse.egit.github.core.Repository;


public class WrapperItem {

	private String title;
	private Repository repo;

	public WrapperItem(String title, Repository repo) {
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

	public Repository getRepo() {
		return repo;
	}

	public void setRepo(Repository repo) {
		this.repo = repo;
	}
	
}

