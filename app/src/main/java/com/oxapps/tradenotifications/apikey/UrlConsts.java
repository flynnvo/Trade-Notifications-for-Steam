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

package com.oxapps.tradenotifications.apikey;

/**
 * Constants for URLs used
 */
public final class UrlConsts {

    /**
     * URL for the initial login screen
     */
    public static final String LOGIN_URL = "https://steamcommunity.com/login/home/?goto=0";

    /**
     * URL to register the API key on
     */
    public static final String API_KEY_URL = "https://steamcommunity.com/dev/registerkey";

    /**
     * Regex for matching the HTTP version of the profile home URL
     */
    public static final String HOME_URL_HTTP = "http://steamcommunity.com/(id|profiles)/(\\w)+/home";

    /**
     * Regex for matching the HTTPS version of the profile home URL
     */
    public static final String HOME_URL_HTTPS = "https://steamcommunity.com/(id|profiles)/(\\w)+/home";
}
