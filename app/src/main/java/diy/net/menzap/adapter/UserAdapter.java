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

import com.like.LikeButton;
import com.like.OnLikeListener;

import java.util.ArrayList;

import diy.net.menzap.R;
import diy.net.menzap.helper.UserDBHelper;
import diy.net.menzap.model.User;

public class UserAdapter extends ArrayAdapter implements OnLikeListener {

    private Context context;
    private ArrayList<User> users;
    private int resource;
    private UserDBHelper userDBHelper;

    public UserAdapter(Context context, int resource, ArrayList<User> users) {
        super(context, resource, users);

        this.context = context;
        this.resource = resource;
        this.users = users;

        this.userDBHelper = new UserDBHelper(this.context);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;

        if (row == null) {
            LayoutInflater inflater = ((Activity)this.context).getLayoutInflater();
            row = inflater.inflate(this.resource, parent, false);
        }
        TextView userName = (TextView)row.findViewById(R.id.userName);
        userName.setText(this.users.get(position).getName());

        LikeButton btnLike = (LikeButton)row.findViewById(R.id.btnLike);
        btnLike.setTag(position);
        btnLike.setOnLikeListener(this);

        if(this.users.get(position).getIsFriend() == 1) {
            btnLike.setLiked(true);
        }

        return row;
    }

    @Override
    public void liked(LikeButton likeButton) {
        int position = (int)likeButton.getTag();
        User user = this.users.get(position);

        user.setIsFriend(1);
        this.userDBHelper.update(user);
    }

    @Override
    public void unLiked(LikeButton likeButton) {
        int position = (int)likeButton.getTag();
        User user = this.users.get(position);

        user.setIsFriend(0);
        this.userDBHelper.update(user);
    }
}
