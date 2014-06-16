package ar.com.globant.githubrepository.dialog;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import ar.com.globant.githubrepository.R;
import ar.com.globant.githubrepository.RepositoriesActivity;
import ar.com.globant.githubrepository.events.MergeButtonEvent;

public class MyMergeDialogFragment extends DialogFragment {
	
	private RepositoriesActivity mActivity = null;
	
	private Bus bus = new Bus();

	
	public static MyMergeDialogFragment newInstance() {
        return new MyMergeDialogFragment();
    }
    
	public void setActivity(RepositoriesActivity activity) {
    	mActivity = activity;
    }
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
        bus.register(this);
	}
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	
        View rootView = inflater.inflate(R.layout.dialog_merge_layout, container, false);
        
        Button mergeButton = (Button) rootView.findViewById(R.id.mergeCommitMsjButton);
        mergeButton.setOnClickListener(new OnClickListener() {
		
			@Override
			public void onClick(View v) {
				bus.post(new MergeButtonEvent(v));
			}
		});
        
        getDialog().setTitle(R.string.merge_title);
        
        return rootView;
    }
    
    @Subscribe
    public void handlerMergeButton(MergeButtonEvent e) {
		EditText et = (EditText) e.getView().getRootView().findViewById(R.id.commitMsjText);
		
		String commitText = et.getText().toString();
		
		mActivity.doMergeRepo(commitText);
    }
    
    @Override
    public void onDestroy() {
    	super.onDestroy();
    	
    	bus.unregister(this);
    }
}
