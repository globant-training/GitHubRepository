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

public class GitHubMainActivity extends ActionBarActivity {

	private Button mSigninButton;
	private EditText mEmailEdit = null;
	private EditText mPasswordEdit = null;
	private CheckBox mCheckShowPassword;
	
	private static GitHubClient client;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEmailEdit = (EditText) findViewById(R.id.emailEdit);
		mPasswordEdit = (EditText) findViewById(R.id.passwordEdit);
		
        mSigninButton = (Button) findViewById(R.id.signinButton);
        // TODO Remove Anonymous Class
        mSigninButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
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
					Toast.makeText(getApplicationContext(), "Login can't be blank", Toast.LENGTH_SHORT).show();
				}
			}
		});
        
        mCheckShowPassword = (CheckBox)findViewById(R.id.checkShowPassword);
        // TODO Remove Anonymous Class
        mCheckShowPassword.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if ( ((CheckBox)v).isChecked() ) {
					mPasswordEdit.setTransformationMethod(null);
				} else {
					mPasswordEdit.setTransformationMethod(new PasswordTransformationMethod());
				}
			}
		});
    }
}
