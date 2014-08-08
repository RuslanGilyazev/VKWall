package com.syswow.vkwall;

import android.app.Activity;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import com.syswow.vkwall.R;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class News extends Activity {
    ArrayList<Post> posts = new ArrayList<Post>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        getNews();
    }

    //Request to VK
    public void getNews() {
        VKRequest newsRequest = new VKRequest("wall.get", VKParameters.from("count", 2,
                "filter", "all", "extended", 1));
        newsRequest.executeWithListener(new VKRequest.VKRequestListener() {

            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                getNewsData(response.json);
            }

            @Override
            public void onError(VKError error) {
                super.onError(error);
            }

            @Override
            public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {
                super.attemptFailed(request, attemptNumber, totalAttempts);
            }
        });
    }

    public void getNewsData(JSONObject jsonObject) {
        JSONObject response;
        JSONArray items;

        try {
            response = jsonObject.getJSONObject("response");
            items = response.getJSONArray("items");

            //Get all news
            for(int itemsCount = 0; itemsCount < items.length(); itemsCount++ ) {
                Post post  = new Post(
                        items.getJSONObject(itemsCount).getInt("id"),
                        items.getJSONObject(itemsCount).getInt("from_id"),
                        items.getJSONObject(itemsCount).getInt("owner_id"),
                        items.getJSONObject(itemsCount).getLong("date"),
                        items.getJSONObject(itemsCount).getString("text"));
                posts.add(post);
            }

        } catch (JSONException e) {
            Log.d("VKWALL", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.news, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
