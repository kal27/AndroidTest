package developer.test.com.androiddevelopertest.Utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import developer.test.com.androiddevelopertest.R;
import developer.test.com.androiddevelopertest.ResponseClasses.Item;

public class ReposArrayAdapter extends ArrayAdapter<Item> {
    private Context context;
    private ArrayList<Item> items;


    public ReposArrayAdapter(Context context, ArrayList<Item> objects) {
        super(context, R.layout.single_item_layout, objects);
        this.context = context;
        items = objects;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = inflater.inflate(R.layout.single_item_layout, parent, false);

            convertView.setTag(viewHolder);

            viewHolder.name = (TextView) convertView.findViewById(R.id.name);
            viewHolder.owner = (TextView) convertView.findViewById(R.id.owner);
            viewHolder.size = (TextView) convertView.findViewById(R.id.size);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.name.setText(items.get(position).getName());
        viewHolder.owner.setText(items.get(position).getOwner().getLogin());
        viewHolder.size.setText(String.valueOf(items.get(position).getSize()));

        return convertView;
    }

    private static class ViewHolder {
        private TextView name;
        private TextView owner;
        private TextView size;
    }

}