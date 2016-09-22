package diy.net.menzap.helper;

import android.content.Context;

/**
 * Created by swathissunder on 22/09/16.
 */

public class DataHolder {
    private AppLibHelper helper;

    public AppLibHelper getHelper() {
        return helper;
    }
    public void initHelper(Context context) {
        this.helper = new AppLibHelper(context);
        this.helper.initService();
    }

    public void destroyHelper() {
        this.helper.destroy();
    }
    private static final DataHolder holder = new DataHolder();
    public static DataHolder getInstance() {
        return holder;
    }
}