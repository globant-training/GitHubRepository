package ar.com.globant.githubrepository.events;

import java.util.EventObject;

import android.view.View;

public class CheckButtonEvent extends EventObject {
	
	
	private static final long serialVersionUID = 3997673308377941856L;
	
	private View view;
	
	
	public CheckButtonEvent(View v) {
		super(v);
		
		view = v;
	}
	
	public View getView() {
		return view;
	}


	public void setView(View view) {
		this.view = view;
	}

}
