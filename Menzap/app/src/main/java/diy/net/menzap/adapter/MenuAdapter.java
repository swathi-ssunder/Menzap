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
import android.widget.EditText;
import android.widget.TextView;

import com.like.LikeButton;
import com.like.OnLikeListener;

import java.util.ArrayList;

import diy.net.menzap.R;
import diy.net.menzap.helper.DataHolder;
import diy.net.menzap.helper.MenuDBHelper;
import diy.net.menzap.model.Menu;
import diy.net.menzap.model.message.MenuMessage;

public class MenuAdapter extends ArrayAdapter implements OnLikeListener {

    private Context context;
    private ArrayList<Menu> menus;
    private int resource;
    private MenuDBHelper menuDBHelper;


    public MenuAdapter(Context context, int resource, ArrayList<Menu> menus) {
        super(context, resource, menus);

        this.context = context;
        this.resource = resource;
        this.menus = menus;

        this.menuDBHelper = new MenuDBHelper(this.context);

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

        EditText editText = (EditText) row.findViewById(R.id.count);
        long count = this.menus.get(position).getLikeCount();
        editText.setText(String.valueOf(count));

        LikeButton btnLike = (LikeButton)row.findViewById(R.id.btnLike);
        LikeButton btnFavourite = (LikeButton)row.findViewById(R.id.btnFavourite);
        btnLike.setTag("like-"+position);
        btnFavourite.setTag("fav-"+ position);
        btnLike.setOnLikeListener(this);
        btnFavourite.setOnLikeListener(this);


        if(this.menus.get(position).getIsLiked() == 1) {
            btnLike.setLiked(true);
        }

        if(this.menus.get(position).getIsFavourite() == 1) {
            btnFavourite.setLiked(true);
        }


        return row;
    }

    @Override
    public void liked(LikeButton likeButton) {
        String tag = (String) likeButton.getTag();
        String[] parts = tag.split("-");

        Menu menu = this.menus.get(Integer.parseInt(parts[1]));

        if(parts[0].equals("like")){
            long likeCount = menu.getLikeCount();
            menu.setIsLiked(1);
            likeCount ++;
            menu.setLikeCount(likeCount);
            MenuMessage msg = new MenuMessage("LIKE",menu.getSender(), menu);
            DataHolder.getInstance().getHelper().saveAndPublish(msg.getScampiMsgObj());
            this.menuDBHelper.update(menu);
            this.notifyDataSetChanged();
        }
        else{
            menu.setIsFavourite(1);
            this.menuDBHelper.update(menu);
        }
    }

    @Override
    public void unLiked(LikeButton likeButton) {
        String tag = (String) likeButton.getTag();
        String[] parts = tag.split("-");

        Menu menu = this.menus.get(Integer.parseInt(parts[1]));
        if(parts[0].equals("like")){
            long likeCount = menu.getLikeCount();
            if( likeCount > 0 ) {
                likeCount --;
                menu.setLikeCount(likeCount);
            }
            menu.setIsLiked(0);
            this.menuDBHelper.update(menu);
            MenuMessage msg = new MenuMessage("DISLIKE", menu.getSender(), menu);
            DataHolder.getInstance().getHelper().saveAndPublish(msg.getScampiMsgObj());
            this.notifyDataSetChanged();
        }
        else{
            menu.setIsFavourite(0);
            this.menuDBHelper.update(menu);
        }

    }
}
