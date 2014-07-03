package ar.com.globant.githubrepository.dialog;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import ar.com.globant.githubrepository.R;

public class MyDialogFragment extends DialogFragment {
	
	
    public static MyDialogFragment newInstance() {
        return new MyDialogFragment();
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	
        View v = inflater.inflate(R.layout.dialog_layout, container, false);
        
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        
        return v;
    }
}
