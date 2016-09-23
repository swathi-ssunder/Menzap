package diy.net.menzap.adapter;

/**
 * Created by viveksethia on 22/09/16.
 */

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import diy.net.menzap.R;
import diy.net.menzap.model.Menu;

public class MenuAdapter extends ArrayAdapter {

    private Context context;
    private ArrayList<Menu> menus;
    private int resource;

    public MenuAdapter(Context context, int resource, ArrayList<Menu> menus) {
        super(context, resource, menus);

        this.context = context;
        this.resource = resource;
        this.menus = menus;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;

        if (row == null) {
            LayoutInflater inflater = ((Activity)this.context).getLayoutInflater();
            row = inflater.inflate(this.resource, parent, false);
        }
        TextView menuName = (TextView)row.findViewById(R.id.menuName);
        menuName.setText(this.menus.get(position).getName());

        return row;
    }
}
