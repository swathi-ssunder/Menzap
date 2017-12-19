package diy.net.menzap.adapter;

/**
 * Created by swathissunder on 20/09/16.
 */

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.like.LikeButton;
import com.like.OnLikeListener;

import java.util.ArrayList;

import diy.net.menzap.R;
import diy.net.menzap.helper.EventDBHelper;
import diy.net.menzap.model.Event;

public class EventAdapter extends ArrayAdapter implements OnLikeListener {

    private Context context;
    private ArrayList<Event> events;
    private int resource;
    private EventDBHelper eventDBHelper;


    public EventAdapter(Context context, int resource, ArrayList<Event> events) {
        super(context, resource, events);

        this.context = context;
        this.resource = resource;
        this.events = events;

        this.eventDBHelper = new EventDBHelper(this.context);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;

        if (row == null) {
            LayoutInflater inflater = ((Activity)this.context).getLayoutInflater();
            row = inflater.inflate(this.resource, parent, false);
        }
        TextView eventName = (TextView)row.findViewById(R.id.eventName);
        eventName.setText(this.events.get(position).getName());

        LikeButton btnLike = (LikeButton)row.findViewById(R.id.btnLike);
        btnLike.setTag(position);
        btnLike.setOnLikeListener(this);

        if(this.events.get(position).getIsInterested() == 1) {
            btnLike.setLiked(true);
        }

        return row;
    }

    @Override
    public void liked(LikeButton likeButton) {
        int position = (int)likeButton.getTag();
        Event event = this.events.get(position);

        event.setIsInterested(1);
        this.eventDBHelper.update(event);
    }

    @Override
    public void unLiked(LikeButton likeButton) {
        int position = (int)likeButton.getTag();
        Event event = this.events.get(position);

        event.setIsInterested(0);
        this.eventDBHelper.update(event);
    }
}
