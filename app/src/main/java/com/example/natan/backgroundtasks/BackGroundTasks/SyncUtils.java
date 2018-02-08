package com.example.natan.backgroundtasks.BackGroundTasks;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

/**
 * Created by natan on 2/7/2018.
 */

public class SyncUtils {


    public static void startImmediateSync(@NonNull final Context context) {
        Intent intentToSyncImmediately = new Intent(context, MyService.class);
        context.startService(intentToSyncImmediately);
    }


}
