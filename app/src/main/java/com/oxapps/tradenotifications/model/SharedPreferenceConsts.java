/*
 * Copyright 2017 Flynn van Os
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

package com.oxapps.tradenotifications.model;

/**
 * Constants for the application's SharedPreferences
 */
public final class SharedPreferenceConsts {

    /**
     * Preference key for the Steam API key
     */
    public static final String API_KEY = "api_key";

    /**
     * Preference key for the trade checking period
     */
    public static final String NOTIF_REFRESH_DELAY = "delay";

    /**
     * Default trade checking delay in seconds
     */
    public static final long DEFAULT_REFRESH_DELAY = 900;

    /**
     * Preference key for the Steam username
     */
    public static final String USERNAME = "username";

    /**
     * Preference key indicating whether it is a custom profile url or not
     */
    public static final String PROFILE = "isProfile";

    /**
     * Preference key indicating the last time a notification was removed
     */
    public static final String LAST_DELETE_KEY = "lastDelete";

    /**
     * Preference key indicating the last time the application checked for new trade offers
     */
    public static final String LAST_CHECK_KEY = "lastChecked";
}
