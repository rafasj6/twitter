package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.util.Log;

import com.codepath.oauth.OAuthBaseClient;
import com.github.scribejava.apis.TwitterApi;
import com.github.scribejava.core.builder.api.BaseApi;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/scribejava/scribejava/tree/master/scribejava-apis/src/main/java/com/github/scribejava/apis
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterClient extends OAuthBaseClient {
	public static final BaseApi REST_API_INSTANCE = TwitterApi.instance(); // Change this
	public static final String REST_URL = "https://api.twitter.com/1.1"; // Change this, base API URL
	public static final String REST_CONSUMER_KEY = "OgqdhVqpvn1tkeDyNEAXuh1s3";       // Change this
	public static final String REST_CONSUMER_SECRET = "8zIqEJz1Gt5XNVcn73kUv1myXjeVbpMtpV5dIUk6wwOkKkl0rL"; // Change this

	// Landing page to indicate the OAuth flow worked in case Chrome for Android 25+ blocks navigation back to the app.
	public static final String FALLBACK_URL = "https://codepath.github.io/android-rest-client-template/success.html";

	// See https://developer.chrome.com/multidevice/android/intents
	public static final String REST_CALLBACK_URL_TEMPLATE = "intent://%s#Intent;action=android.intent.action.VIEW;scheme=%s;package=%s;S.browser_fallback_url=%s;end";

	public TwitterClient(Context context) {
		super(context, REST_API_INSTANCE,
				REST_URL,
				REST_CONSUMER_KEY,
				REST_CONSUMER_SECRET,
				String.format(REST_CALLBACK_URL_TEMPLATE, context.getString(R.string.intent_host),
						context.getString(R.string.intent_scheme), context.getPackageName(), FALLBACK_URL));
	}
	// CHANGE THIS
	// DEFINE METHODS for different API endpoints here
	public void getHomeTimeline(AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/home_timeline.json");
		// Can specify query string params directly or through RequestParams.
		RequestParams params = new RequestParams();
		params.put("count", 25);
		params.put("since_id",1); // in future for efresh;
		client.get(apiUrl, params, handler);
	}

	public void sendTweet(String message, long reply_id,  AsyncHttpResponseHandler handler) {
		Log.d("Twitter Client", message);
		String apiUrl = getApiUrl("statuses/update.json");
		// Can specify query string params directly or through RequestParams.
		RequestParams params = new RequestParams();
		//thanks to the Boolaj
		if (reply_id != -1 ){
			params.put("in_reply_to_status_id", reply_id);

			Log.d("SendTweet:",String.valueOf(reply_id));
		}
		params.put("status", message);
		client.post(apiUrl, params, handler);
	}

	public void sendRetweet(long retweet_id,  AsyncHttpResponseHandler handler) {
		Log.d("Twitter Client", String.valueOf(retweet_id));
		String apiUrl = getApiUrl("statuses/retweet/:id");
		// Can specify query string params directly or through RequestParams.
		RequestParams params = new RequestParams();
		//thanks to the Boolaj

		params.put("id", retweet_id);
		client.post(apiUrl, params, handler);
	}


	public void sendFavorite(long favorite_id,  AsyncHttpResponseHandler handler){

		Log.d("Twitter Client", "favoriting" + String.valueOf(favorite_id));
		String apiUrl = getApiUrl("favorites/create.json");
		// Can specify query string params directly or through RequestParams.
		RequestParams params = new RequestParams();

		params.put("id", favorite_id);
		client.post(apiUrl, params, handler);


	}

	public void sendUnfavorite(long unfavorite_id,  AsyncHttpResponseHandler handler){

		Log.d("Twitter Client", "unfavoriting" + String.valueOf(unfavorite_id));
		String apiUrl = getApiUrl("favorites/destroy.json");
		// Can specify query string params directly or through RequestParams.
		RequestParams params = new RequestParams();

		params.put("id", unfavorite_id);
		client.post(apiUrl, params, handler);


	}

	/* 1. Define the endpoint URL with getApiUrl and pass a relative path to the endpoint
	 * 	  i.e getApiUrl("statuses/home_timeline.json");
	 * 2. Define the parameters to pass to the request (query or body)
	 *    i.e RequestParams params = new RequestParams("foo", "bar");
	 * 3. Define the request method and make a call to the client
	 *    i.e client.get(apiUrl, params, handler);
	 *    i.e client.post(apiUrl, params, handler);
	 */
}
