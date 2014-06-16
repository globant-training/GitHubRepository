package ar.com.globant.githubrepository.adapter;

import java.util.List;

import org.eclipse.egit.github.core.Repository;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import ar.com.globant.githubrepository.R;
import ar.com.globant.globant.model.WrapperItem;

public class ListRepoCustomAdapter extends ArrayAdapter<WrapperItem> {
	
	private Context mContext;
	private List<WrapperItem> lista;
	private int layout;
	
	
	public ListRepoCustomAdapter(Context myViewFragment, int textViewResourceId, List<WrapperItem> lista) {
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
			itemHolder.mTextTitle   = (TextView) convertView.findViewById(R.id.reporequestText);
			itemHolder.mDescription = (TextView) convertView.findViewById(R.id.description);
			itemHolder.mLanguage    = (TextView) convertView.findViewById(R.id.language);
			itemHolder.mButton 	    = (Button) convertView.findViewById(R.id.selectButton);
			
			convertView.setTag(itemHolder);
		} else {
			itemHolder = (ItemHolder) convertView.getTag(); 
		}
		
		WrapperItem elemento = lista.get(position);
		
		itemHolder.mTextTitle.setText(elemento.getTitle());
		
		String description = elemento.getDescription();
		if ( description != null && !description.isEmpty() )
			itemHolder.mDescription.setText(description);
		else
			itemHolder.mDescription.setText(R.string.no_mesj);
				
		String language = elemento.getLanguage();
		if ( language != null && !language.isEmpty() )
			itemHolder.mLanguage.setText(language);
		else
			itemHolder.mLanguage.setText(R.string.no_mesj);
		
		itemHolder.mButton.setTag(elemento.getRepo());
		
		return convertView;
	}
	
	public static class ItemHolder {
		TextView mTextTitle;
		TextView mDescription;
		TextView mLanguage;
		Button mButton;
	}
	
	public void setData(List<Repository> data) {
		for (Repository repo : data) 
			lista.add(new WrapperItem(repo.getName(), 
									  repo,
									  repo.getDescription(),
									  repo.getLanguage()));
	}
}
