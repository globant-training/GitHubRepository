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
import ar.com.globant.globant.model.WrapperItem;

public class ListCustomAdapter extends ArrayAdapter<WrapperItem> {
	
	private Context mContext;
	private List<WrapperItem> lista;
	private int layout;
		
	public ListCustomAdapter(Context myViewFragment, int textViewResourceId, List<WrapperItem> lista) {
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
			itemHolder.mTextTitle = (TextView) convertView.findViewById(R.id.reporequestText);
			itemHolder.mButton 	  = (Button) convertView.findViewById(R.id.selectButton);
			
			
			convertView.setTag(itemHolder);
		} else {
			itemHolder = (ItemHolder) convertView.getTag(); 
		}
		
		WrapperItem elemento = lista.get(position);
		
		itemHolder.mTextTitle.setText(elemento.getTitle());
		itemHolder.mButton.setTag(elemento.getRepo());
		
		return convertView;
	}
	
	public static class ItemHolder {
		TextView mTextTitle;
		Button mButton;
	}
}
