/*
 * Copyright 2016 Flynn van Os
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.oxapps.tradenotifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;

import com.oxapps.tradenotifications.model.IntentConsts;
import com.oxapps.tradenotifications.model.SharedPreferenceConsts;

public class NotificationDeleteReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        long newRequestTime = System.currentTimeMillis() / 1000;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        long lastCheckTime = prefs.getLong(BackgroundTaskService.LAST_CHECK_KEY, newRequestTime);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong(BackgroundTaskService.LAST_DELETE_KEY, lastCheckTime);
        editor.apply();

        if(intent.getBooleanExtra(IntentConsts.NOTIFICATION_CLICKED, false)) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW);
            String username = prefs.getString(SharedPreferenceConsts.USERNAME, "me");
            boolean isProfile = prefs.getBoolean(SharedPreferenceConsts.PROFILE, false);
            String identifier = isProfile ? "profile/" : "id/";
            String url = "https://steamcommunity.com/" + identifier + username + "/tradeoffers";
            browserIntent.setData(Uri.parse(url));
            browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.getApplicationContext().startActivity(browserIntent);
        }
    }
}
