package diy.net.menzap.adapter;

/**
 * Created by viveksethia on 22/09/16.
 */

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.like.LikeButton;
import com.like.OnLikeListener;

import java.io.File;
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

        // if vegetarian
        if (this.menus.get(position).getCategory() == 1) {
            menuName.setTextColor(Color.parseColor("#239957"));
        }

        EditText editText = (EditText) row.findViewById(R.id.count);
        long count = this.menus.get(position).getLikeCount();
        editText.setText(String.valueOf(count));

        String menu = this.menus.get(position).getName();

        File imgFile = new File(Environment.getExternalStorageDirectory()
                + File.separator + "Menzap/Images/"+menu+".jpg");

        if(imgFile.exists()){
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            ImageView myImage = (ImageView) row.findViewById(R.id.imageView);
            myImage.setImageBitmap(myBitmap);
        }

        LikeButton btnLike = (LikeButton)row.findViewById(R.id.btnLike);
        LikeButton btnFavourite = (LikeButton)row.findViewById(R.id.btnFavourite);

        SharedPreferences pref = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        int isAdmin = pref.getInt("isAdmin", 0);

        if(isAdmin == 1) {
            btnLike.setEnabled(false);
            btnFavourite.setVisibility(View.GONE);
        }

        if(count > 0)
            btnLike.setLiked(true);

        btnLike.setTag("like-"+position);
        btnFavourite.setTag("fav-"+ position);

        btnLike.setOnLikeListener(this);
        btnFavourite.setOnLikeListener(this);

        if (this.menus.get(position).getIsLiked() == 1) {
            btnLike.setLiked(true);
        }

        if (this.menus.get(position).getIsFavourite() == 1) {
            btnFavourite.setLiked(true);
        }

        return row;
    }

    @Override
    public void liked(LikeButton likeButton) {
        String tag = (String) likeButton.getTag();
        String[] parts = tag.split("-");

        Menu menu = this.menus.get(Integer.parseInt(parts[1]));

        //Fetching sender details from preferences
        SharedPreferences pref = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String emailId = pref.getString("emailId", "");
        String sender = emailId;

        if (parts[0].equals("like")) {
            long likeCount = menu.getLikeCount();
            menu.setIsLiked(1);
            likeCount++;
            menu.setLikeCount(likeCount);

            MenuMessage msg = new MenuMessage("LIKE", sender, menu);
            DataHolder.getInstance().getHelper().saveAndPublish(msg.getScampiMsgObj());
            this.menuDBHelper.update(menu);
            this.notifyDataSetChanged();
        } else {
            menu.setIsFavourite(1);
            this.menuDBHelper.update(menu);
        }
    }

    @Override
    public void unLiked(LikeButton likeButton) {
        String tag = (String) likeButton.getTag();
        String[] parts = tag.split("-");

        Menu menu = this.menus.get(Integer.parseInt(parts[1]));

        //Fetching sender details from preferences
        SharedPreferences pref = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String emailId = pref.getString("emailId", "");
        String sender = emailId;

        if (parts[0].equals("like")) {
            long likeCount = menu.getLikeCount();
            if (likeCount > 0) {
                likeCount--;
                menu.setLikeCount(likeCount);
            }
            menu.setIsLiked(0);
            this.menuDBHelper.update(menu);
            MenuMessage msg = new MenuMessage("DISLIKE", sender, menu);
            DataHolder.getInstance().getHelper().saveAndPublish(msg.getScampiMsgObj());
            this.notifyDataSetChanged();
        } else {
            menu.setIsFavourite(0);
            this.menuDBHelper.update(menu);
        }
    }
}
