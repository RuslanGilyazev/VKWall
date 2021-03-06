package com.syswow.vkwall;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vk.sdk.VKUIHelper;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Wall extends Activity {
    ArrayList<Post> posts = new ArrayList<Post>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wall);
        getWall();
    }

    //Request to VK
    public void getWall() {
        VKRequest newsRequest = new VKRequest("wall.get", VKParameters.from("count", 20,
                "filter", "all", "extended", 1));
        newsRequest.executeWithListener(new VKRequest.VKRequestListener() {

            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                getWallData(response.json);
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

    public void getWallData(JSONObject jsonObject) {
        JSONObject response;
        JSONArray items;
        JSONArray profiles;

        LinearLayout linLayout = (LinearLayout) findViewById(R.id.linLayout);
        LayoutInflater ltInflater = getLayoutInflater();

        try {
            response = jsonObject.getJSONObject("response");
            items = response.getJSONArray("items");
            profiles = response.getJSONArray("profiles");

            //Get all news
            for(int itemsCount = 0; itemsCount < items.length(); itemsCount++ ) {
                Post post  = new Post(
                        profiles,
                        items.getJSONObject(itemsCount).getInt("id"),
                        items.getJSONObject(itemsCount).getInt("from_id"),
                        items.getJSONObject(itemsCount).getInt("owner_id"),
                        items.getJSONObject(itemsCount).getLong("date"),
                        items.getJSONObject(itemsCount).getString("text"));
                posts.add(post);

                View item = ltInflater.inflate(R.layout.post, linLayout, false);
                TextView postName = (TextView) item.findViewById(R.id.news_name);
                TextView postText = (TextView) item.findViewById(R.id.news_post);
                TextView postData = (TextView) item.findViewById(R.id.news_data);
                ImageView postAvatar = (ImageView) item.findViewById(R.id.news_ava);

                ImagesDownloader imagesDownloader = new ImagesDownloader();
                imagesDownloader.setImageView(postAvatar);
                imagesDownloader.execute(post.getAvatar50url());

                postName.setText(post.getAuthor());
                postText.setText(post.getText());
                postData.setText(post.getDate());

                item.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
                linLayout.addView(item);
            }

        } catch (JSONException e) {
            Log.d("VKWALL", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        VKUIHelper.onResume(this);
    }

    @Override
    protected  void onDestroy() {
        super.onDestroy();
        VKUIHelper.onDestroy(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        VKUIHelper.onActivityResult(requestCode, resultCode, data);
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
