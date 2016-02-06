package com.codepath.cpw1instagram;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class PhotoActivity extends AppCompatActivity {
  public static final String CLIENT_ID = "e05c462ebd86446ea48a5af73769b602";
  private ArrayList<InstagramPhoto> photos;
  private InstagramPhotosAdapter aPhotos;
  private SwipeRefreshLayout swipeContainer;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_photos);
    // Lookup the swipe container view
    swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
    // Setup refresh listener which triggers new data loading
    swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override
      public void onRefresh() {
        fetchPopularPhotos();
      }
    });
    // Configure the refreshing colors
    swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
      android.R.color.holo_green_light,
      android.R.color.holo_orange_light,
      android.R.color.holo_red_light);

    photos = new ArrayList<>();
    aPhotos = new InstagramPhotosAdapter(this, photos);
    ListView lvPhotos = (ListView)findViewById(R.id.lvPhotos);
    lvPhotos.setAdapter(aPhotos);
    fetchPopularPhotos();
  }

  public void fetchPopularPhotos() {
    String url = "https://api.instagram.com/v1/media/popular?client_id=" + CLIENT_ID;
    AsyncHttpClient client = new AsyncHttpClient();
    client.get(url, null, new JsonHttpResponseHandler() {
      @Override
      public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        JSONArray photosJson = null;
        aPhotos.clear();
        try {
          photosJson = response.getJSONArray("data");
          for (int i = 0; i < photosJson.length(); i++) {
            JSONObject photoJson = photosJson.getJSONObject(i);
            InstagramPhoto photo = new InstagramPhoto();
            photo.id = photoJson.getString("id");
            photo.caption = "";
            if (photoJson.has("caption") && !photoJson.isNull("caption")) {
              photo.caption = photoJson.getJSONObject("caption").getString("text");
            }
            photo.imageUrl = photoJson.getJSONObject("images").getJSONObject("standard_resolution").getString("url");
            photo.likesCount = photoJson.getJSONObject("likes").getInt("count");
            photo.userName = photoJson.getJSONObject("user").getString("username");
            photo.profileUrl = photoJson.getJSONObject("user").getString("profile_picture");
            photo.publishTime = photoJson.getInt("created_time");
            // Get video info.
            String type = photoJson.getString("type");
            if (type.equals("video")) {
              photo.isVideo = true;
              photo.videoUrl = photoJson.getJSONObject("videos").getJSONObject("standard_resolution").getString("url");
            } else {
              photo.isVideo = false;
              photo.videoUrl = null;
            }
            // Get all comments.
            photo.comments = new ArrayList<Comment>();
            JSONObject commentsObj = photoJson.getJSONObject("comments");
            photo.commentCount = commentsObj.getInt("count");
            JSONArray allComments = commentsObj.getJSONArray("data");
            for (int j = 0; j < allComments.length(); j++) {
              JSONObject commentObj = allComments.getJSONObject(j);
              Comment comment = new Comment();
              comment.createdTime = commentObj.getInt("created_time");
              comment.text = commentObj.getString("text");
              JSONObject from = commentObj.getJSONObject("from");
              comment.profileUrl = from.getString("profile_picture");
              comment.userName = from.getString("username");
              photo.comments.add(comment);
            }
            photos.add(photo);
          }
          swipeContainer.setRefreshing(false);
        } catch (JSONException e) {
          e.printStackTrace();
        }
        aPhotos.notifyDataSetChanged();
      }
    });
  }
}
