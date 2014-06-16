package ar.com.globant.githubrepository.events;

import java.util.EventObject;

import android.support.v7.app.ActionBar;

public class PageSelectEvent extends EventObject {

	private static final long serialVersionUID = -1697140557653708942L;
	
	private int pageNumber;

	private ActionBar actionBar;
	
	
	public PageSelectEvent(Object source) {
		super(source);
	}
	
	public PageSelectEvent(int pageNumber, ActionBar actionBar) {
		super(pageNumber);
		
		this.pageNumber = pageNumber;
		this.actionBar  = actionBar ;
	}
	
	
	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public ActionBar getActionBar() {
		return actionBar;
	}

	public void setActionBar(ActionBar actionBar) {
		this.actionBar = actionBar;
	}
	
}
