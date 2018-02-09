package com.example.natan.backgroundtasks.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Parcelable;
import android.preference.PreferenceManager;

import com.example.natan.backgroundtasks.R;

import java.security.PublicKey;

/**
 * Created by natan on 2/7/2018.
 */

public class PrefrencesKeys {
    public final static String Parcelable_key = "parcel";

    public final static String onSaveInstance_Key="onSave";


    public static boolean areNotificationsEnabled(Context context) {


        String key = context.getString(R.string.Noti_key);


        boolean notificationDefault = context.getResources().getBoolean(R.bool.show_notifications_by_default);


        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);


        boolean shouldDisplayNotification = sp.getBoolean(key, notificationDefault);


        return shouldDisplayNotification;


    }


}
