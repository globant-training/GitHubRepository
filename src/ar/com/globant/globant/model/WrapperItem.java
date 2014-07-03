package ar.com.globant.globant.model;

import org.eclipse.egit.github.core.Repository;


public class WrapperItem {

	private String title;
	private Repository repo;
	private String description;
	private String language;
	

	public WrapperItem(String title, Repository repo, String description,
			String language) {
		super();
		this.title = title;
		this.repo = repo;
		this.description = description;
		this.language = language;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}
	
}

