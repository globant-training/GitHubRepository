package ar.com.globant.githubrepository;

import org.eclipse.egit.github.core.client.GitHubClient;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import ar.com.globant.githubrepository.events.CheckButtonEvent;
import ar.com.globant.githubrepository.events.SignInButtonEvent;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

public class GitHubMainActivity extends ActionBarActivity {

	private Button mSigninButton;
	private EditText mEmailEdit = null;
	private EditText mPasswordEdit = null;
	private CheckBox mCheckShowPassword;
	
	private static GitHubClient client = null;
	
	private Bus bus = new Bus();
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        bus.register(this);
        
        mEmailEdit = (EditText) findViewById(R.id.emailEdit);
		mPasswordEdit = (EditText) findViewById(R.id.passwordEdit);
		
        mSigninButton = (Button) findViewById(R.id.signinButton);
        mSigninButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				bus.post(new SignInButtonEvent(this));
			}
		});
        
        mCheckShowPassword = (CheckBox)findViewById(R.id.checkShowPassword);
        // TODO Remove Anonymous Class
        mCheckShowPassword.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				bus.post(new CheckButtonEvent(v));
			}
		});
    }
    
    @Subscribe
    public void handlerSignInButtonPress(SignInButtonEvent e) {
    	String username = new String(mEmailEdit.getText().toString());
		String password = new String(mPasswordEdit.getText().toString());
		
		if (!username.isEmpty() && !password.isEmpty()) {
			client = new GitHubClient();
			client.setCredentials(username, password);
			
			Intent intentPullRequest = new Intent(getApplicationContext(), RepositoriesActivity.class);
			intentPullRequest.putExtra("username", username);
			intentPullRequest.putExtra("password", password);
			startActivity(intentPullRequest);
		} else {
			Toast.makeText(getApplicationContext(), R.string.login_error_msj, Toast.LENGTH_SHORT).show();
		}
    }
    
    @Subscribe
    public void handleShowPassword(CheckButtonEvent e) {
    	if ( ((CheckBox)e.getView()).isChecked() ) {
			mPasswordEdit.setTransformationMethod(null);
		} else {
			mPasswordEdit.setTransformationMethod(new PasswordTransformationMethod());
		}
    }
    
    @Override
    protected void onDestroy() {
       	super.onDestroy();
       	
       	bus.unregister(this);
    }
}
