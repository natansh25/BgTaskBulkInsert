package com.example.natan.backgroundtasks.BackGroundTasks;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;

import com.example.natan.backgroundtasks.Database.Contract;
import com.example.natan.backgroundtasks.Network.NetworkUtils;

import org.json.JSONException;

import java.net.URL;

/**
 * Created by natan on 2/8/2018.
 */

public class SyncTask {

    synchronized public static void syncContacts(Context context) throws JSONException {

        URL url = NetworkUtils.buildURl();
        ContentValues[] contentValues = NetworkUtils.fetchContactData(url);

        if (contentValues != null && contentValues.length != 0) {
            ContentResolver contentResolver = context.getContentResolver();


            //Deleting the old data
            contentResolver.delete(Contract.Fav.CONTENT_URI, null, null);

            //Bulk inserting the new data into the database
            contentResolver.bulkInsert(Contract.Fav.CONTENT_URI, contentValues);

            Notif.notify(context);

        }


    }


}
