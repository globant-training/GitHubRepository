package ar.com.globant.githubrepository.events;

import java.util.EventObject;

import android.view.View;

public class MergeButtonEvent extends EventObject {

	private static final long serialVersionUID = 8461415064441815064L;
	
	private View view;
	
	
	public MergeButtonEvent(View v) {
		super(v);
		
		setView(v);
	}


	public View getView() {
		return view;
	}


	public void setView(View view) {
		this.view = view;
	}

}
