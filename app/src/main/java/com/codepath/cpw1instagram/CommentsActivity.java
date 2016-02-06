package com.codepath.cpw1instagram;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Spanned;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class CommentsActivity extends AppCompatActivity {
  ArrayList<Spanned> comments;
  ArrayAdapter<Spanned> adapter;
  String postId;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_comments);

    postId = getIntent().getStringExtra("post_id");
    comments = new ArrayList<>();
    ListView lvComments = (ListView)findViewById(R.id.lvComments);
    adapter = new ArrayAdapter<Spanned>(this, android.R.layout.simple_list_item_1, comments);
    lvComments.setAdapter(adapter);
    fetchComments();
  }

  private void fetchComments() {
    comments.clear();
    adapter.notifyDataSetChanged();
    String url = "https://api.instagram.com/v1/media/" + postId + "/comments?client_id=" + PhotoActivity.CLIENT_ID;
    AsyncHttpClient client = new AsyncHttpClient();
    client.get(url, new JsonHttpResponseHandler() {
      @Override
      public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        try {
          JSONArray commentsArray = response.getJSONArray("data");
          for (int i = 0; i < commentsArray.length(); i++) {
            JSONObject commentObj = commentsArray.getJSONObject(i);
            Comment c = new Comment();
            c.text = commentObj.getString("text");
            c.userName = commentObj.getJSONObject("from").getString("username");
            comments.add(c.shortLine());
          }
          adapter.notifyDataSetChanged();
        } catch (JSONException e) {
          e.printStackTrace();
        }
      }
    });
  }
}
