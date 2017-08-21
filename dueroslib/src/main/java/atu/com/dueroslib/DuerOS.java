package atu.com.dueroslib;

import android.app.Application;
import android.content.Context;

/**
 * Created by atu on 2017/8/19.
 */

public class DuerOS {

    public static Context ctx;

    public static void init(Context context) {
        if (ctx == null) {
            ctx = context.getApplicationContext();
        }
    }
}
