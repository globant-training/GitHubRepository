package ar.com.globant.githubrepository.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import ar.com.globant.githubrepository.R;
import ar.com.globant.globant.model.WrapperPRItem;

public class ListPullRequestCustomAdapter extends ArrayAdapter<WrapperPRItem> {
	
	private Context mContext;
	private List<WrapperPRItem> lista;
	private int layout;
		
	public ListPullRequestCustomAdapter(Context myViewFragment, int textViewResourceId, List<WrapperPRItem> lista) {
		super(myViewFragment, textViewResourceId, lista);

		this.layout = textViewResourceId;
		this.mContext = myViewFragment;
		this.lista = lista;
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ItemHolder itemHolder;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(layout, parent, false);
			
			itemHolder = new ItemHolder();
			itemHolder.mTextTitle = (TextView) convertView.findViewById(R.id.pullrequestText);
			itemHolder.mButton 	  = (Button) convertView.findViewById(R.id.mergeButton);
			
			convertView.setTag(itemHolder);
		} else {
			itemHolder = (ItemHolder) convertView.getTag(); 
		}
		
		WrapperPRItem elemento = lista.get(position);
		
		itemHolder.mTextTitle.setText(elemento.getTitle());
		itemHolder.mButton.setTag(elemento.getPullRequest());
		
		return convertView;
	}
	
	public static class ItemHolder {
		TextView mTextTitle;
		Button mButton;
	}
}
