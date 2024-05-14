/*
 * Copyright (C) 2007-2024 Crafter Software Corporation. All Rights Reserved.
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

import okhttp3.*
import groovy.json.JsonBuilder
import groovy.json.JsonSlurper

class HttpClient {
    private static HttpClient instance
    private final String baseUri
    private final String authorizationHeader
    private final OkHttpClient client
    private final BasicCookieJar cookieJar

    static final String AUTH_HEADER = 'Authorization'

    static HttpClient getInstance(config) {
        if (instance == null) {
            instance = new HttpClient(config)
        }
        return instance
    }

    static HttpClient resetClient(config) {
        instance = null
        return getInstance(config)
    }

    HttpClient(config) {
        this.baseUri = config.url
        this.cookieJar = new BasicCookieJar()
        this.client = new OkHttpClient().newBuilder().cookieJar(cookieJar).build()
        if (config.token) {
            authorizationHeader = "Bearer ${config.token}"
        } else {
            authorizationHeader = "Basic ${"${config.username}:${config.password}".bytes.encodeBase64()}"
        }
    }

    /**
     * HTTP GET method
     * @param path API path
     * @param queryParams URI query parameters
     * @return response
     */
    def get(String path, Map queryParams = null) {
        HttpUrl.Builder httpBuilder = HttpUrl.parse(baseUri + path).newBuilder()
        if (queryParams != null) {
            queryParams.each { k, v -> httpBuilder.addQueryParameter(k, v) }
        }
        Request request = new Request.Builder()
                .url(httpBuilder.build())
                .addHeader(AUTH_HEADER, authorizationHeader)
                .build()

        Response response = client.newCall(request).execute()
        if (!response.successful) {
            displayResponseDetail(response)
            return null
        }

        return new JsonSlurper().parseText(response.body().string())
    }

    /**
     * HTTP POST method
     * @param path API path
     * @param data post data
     * @return response
     */
    def post(String path, Map data) {
        RequestBody body = RequestBody.create(new JsonBuilder(data).toString(), MediaType.parse('application/json; charset=utf-8'))
        Request request = new Request.Builder()
                .url(baseUri + path)
                .post(body)
                .addHeader(AUTH_HEADER, authorizationHeader)
                .build()

        Response response = client.newCall(request).execute()
        if (!response.successful) {
            displayResponseDetail(response)
            return null
        }

        return new JsonSlurper().parseText(response.body().string())
    }

    /**
     * HTTP POST method with form parameters
     * @param path API path
     * @param formParams form parameters
     */
    def postForm(String path, Map formParams) {
        FormBody.Builder formBuilder = new FormBody.Builder()
        formParams.each { k, v -> formBuilder.add(k, v) }
        RequestBody formBody = formBuilder.build()
        Request request = new Request.Builder()
                .url(baseUri + path)
                .post(formBody)
                .addHeader("Cookie", "XSRF-TOKEN=${formParams["_csrf"]}")
                .build()

        Response response = client.newCall(request).execute()
        if (!response.successful) {
            displayResponseDetail(response)
        }
    }

    /**
     * Display HTTP response object detail
     * @param response HTTP response object
     */
    private void displayResponseDetail(Response response) {
        println "Status Code: ${response.code()}"
        def body = new JsonSlurper().parseText(response.body().string())

        // API 1 format
        if (body.message) {
            println "Error Message: ${body.message}"
            return
        }

        // API 2 format
        println "Error Code: ${body.response.code}"
        println "Error Message: ${body.response.message}"
        if (body.response.remedialAction) {
            println "Error Action: ${body.response.remedialAction}"
        }
        if (body.response.documentationUrl) {
            println "Error Documentation: ${body.response.documentationUrl}"
        }
    }
}