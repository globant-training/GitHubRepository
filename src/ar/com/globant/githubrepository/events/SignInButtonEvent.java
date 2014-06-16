package ar.com.globant.githubrepository.events;

import java.util.EventObject;

public class SignInButtonEvent extends EventObject{


	private static final long serialVersionUID = 8114224468238238124L;

	public SignInButtonEvent(Object source) {
		super(source);
	}

}
