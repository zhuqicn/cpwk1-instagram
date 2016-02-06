package com.codepath.cpw1instagram;

import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.MediaController;
import android.widget.VideoView;

public class VideoViewActivity extends AppCompatActivity {

  // Declare variables
  ProgressDialog pDialog;
  VideoView videoview;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_video_view);

    // Get video url.
    String videoURL = getIntent().getStringExtra("video_url");
    videoview = (VideoView) findViewById(R.id.videoView);
    // Execute StreamVideo AsyncTask
    // Create a progressbar
    pDialog = new ProgressDialog(VideoViewActivity.this);
    // Set progressbar title
    pDialog.setTitle("Android Video Streaming Tutorial");
    // Set progressbar message
    pDialog.setMessage("Buffering...");
    pDialog.setIndeterminate(false);
    pDialog.setCancelable(false);
    // Show progressbar
    pDialog.show();

    try {
      // Start the MediaController
      MediaController mediacontroller = new MediaController(
        VideoViewActivity.this);
      mediacontroller.setAnchorView(videoview);
      // Get the URL from String VideoURL
      Uri video = Uri.parse(videoURL);
      videoview.setMediaController(mediacontroller);
      videoview.setVideoURI(video);

    } catch (Exception e) {
      Log.e("Error", e.getMessage());
      e.printStackTrace();
    }

    videoview.requestFocus();
    videoview.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
      // Close the progress bar and play the video
      public void onPrepared(MediaPlayer mp) {
        pDialog.dismiss();
        videoview.start();
      }
    });
  }
}
