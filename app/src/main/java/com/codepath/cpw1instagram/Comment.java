package com.codepath.cpw1instagram;

import android.text.Html;
import android.text.Spanned;

/**
 * Created by qi_zhu on 2/4/16.
 */
public class Comment {
  public int createdTime;
  public String userName;
  public String text;
  public String profileUrl;

  public Spanned shortLine() {
    return Html.fromHtml("<font color=#ADD8E6>" + this.userName + "</font> " + this.text);
  }
}
