package ar.com.globant.githubrepository.dialog;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import ar.com.globant.githubrepository.R;
import ar.com.globant.githubrepository.RepositoriesActivity;

public class MyMergeDialogFragment extends DialogFragment {
	
	private RepositoriesActivity mActivity = null;
	

	public static MyMergeDialogFragment newInstance() {
        return new MyMergeDialogFragment();
    }
    
	public void setActivity(RepositoriesActivity activity) {
    	mActivity = activity;
    }
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	
        View v = inflater.inflate(R.layout.dialog_merge_layout, container, false);
        
        // TODO Remove Anonymous Class
        Button mergeButton = (Button) v.findViewById(R.id.mergeCommitMsjButton);
        mergeButton.setOnClickListener(new OnClickListener() {
		
			@Override
			public void onClick(View v) {
				mActivity.doMergeRepo();
			}
		});
        
        getDialog().setTitle(R.string.merge_title);

        return v;
    }

}
