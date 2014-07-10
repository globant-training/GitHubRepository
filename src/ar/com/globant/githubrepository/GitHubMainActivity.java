package ar.com.globant.githubrepository;

import java.util.Map;
import java.util.Set;

import org.eclipse.egit.github.core.client.GitHubClient;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

public class GitHubMainActivity extends ActionBarActivity implements OnItemClickListener {

	private Button mSigninButton;
	private AutoCompleteTextView mEmailEdit = null;
	private EditText mPasswordEdit = null;
	private CheckBox mCheckShowPassword;
	
	public SharedPreferences sp;
	
	private static GitHubClient client = null;
	
	private static String[] usernames = new String[] {
       
    };
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        setTitle(null);
        
        usernames = getUser();
        
        mPasswordEdit = (EditText) findViewById(R.id.passwordEdit);
        mEmailEdit = (AutoCompleteTextView) findViewById(R.id.emailEdit);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
																android.R.layout.simple_dropdown_item_1line, 
																usernames);
		mEmailEdit.setAdapter(adapter);
		mEmailEdit.setOnItemClickListener(this);
		
        mSigninButton = (Button) findViewById(R.id.signinButton);
        // TODO Remove Anonymous Class
        mSigninButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				String username = new String(mEmailEdit.getText().toString());
				String password = new String(mPasswordEdit.getText().toString());
				
				if ( !username.isEmpty() && !password.isEmpty() ) {
					client = new GitHubClient();
					client.setCredentials(username, password);
					
					Intent intentPullRequest = new Intent(getApplicationContext(), RepositoriesActivity.class);
					intentPullRequest.putExtra("username", username);
					intentPullRequest.putExtra("password", password);
					startActivity(intentPullRequest);
				} else { 
						if ( username.isEmpty() ) {
							Crouton.makeText(GitHubMainActivity.this, R.string.login_username_error_msj, Style.ALERT).show();
						} else {
							Crouton.makeText(GitHubMainActivity.this, R.string.login_password_error_msj, Style.ALERT).show();
						}
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
    
    private String[] getUser() {
    	sp = this.getSharedPreferences(getApplicationContext().getPackageName(), Context.MODE_PRIVATE);
        Set<String> keys = sp.getAll().keySet();
        
        return keys.toArray(new String[keys.size()]);
	}

	@Override
    protected void onDestroy() {
    	super.onDestroy();
    	
    	Crouton.cancelAllCroutons();
    }
    
    // OnItemClickListener method
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		String value = sp.getString(parent.getItemAtPosition(position).toString(), "");
		if ( value != null && value.length() > 0 )
			mPasswordEdit.setText( value );
	}
}
