/* Copyright (C) 2007-2024 Crafter Software Corporation. All Rights Reserved.
*
* This program is free software: you can redistribute it and/or modify
        * it under the terms of the GNU General Public License version 3 as published by
        * the Free Software Foundation.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
        * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
        * along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package org.craftercms.cli.utils

import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl

class BasicCookieJar implements CookieJar {
    List<Cookie> cookies = []

    @Override
    void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        this.cookies.addAll(cookies)
    }

    @Override
    List<Cookie> loadForRequest(HttpUrl url) {
        return cookies
    }
}