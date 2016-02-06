package com.codepath.cpw1instagram;

import java.util.ArrayList;

public class InstagramPhoto {
  public String caption;
  public String imageUrl;
  public String videoUrl;
  public boolean isVideo;
  public String userName;
  public String profileUrl;
  public int publishTime;
  public int likesCount;
  public int commentCount;
  public ArrayList<Comment> comments;

  public InstagramPhoto() {
    comments = new ArrayList<>();
  }
}
