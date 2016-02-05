package com.codepath.cpw1instagram;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.Date;
import java.util.List;

public class InstagramPhotosAdapter extends ArrayAdapter<InstagramPhoto>{
  public InstagramPhotosAdapter(Context context, List<InstagramPhoto> objects) {
    super(context, android.R.layout.simple_list_item_1 , objects);
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {




    InstagramPhoto photo = getItem(position);
    if (convertView == null) {
      convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_photo, parent, false);
    }
    TextView tvCaption = (TextView)convertView.findViewById(R.id.tvCaption);
    TextView tvUserName = (TextView)convertView.findViewById(R.id.tvUserName);
    TextView tvTime = (TextView)convertView.findViewById(R.id.tvTime);
    TextView tvLikeCount = (TextView)convertView.findViewById(R.id.tvLikeCount);
    ImageView ivPhoto = (ImageView)convertView.findViewById(R.id.ivPhoto);
    ImageView ivProfile = (ImageView)convertView.findViewById(R.id.ivProfile);
    TextView tvComment1 = (TextView)convertView.findViewById(R.id.tvComment1);
    TextView tvComment2 = (TextView)convertView.findViewById(R.id.tvComment2);

    ivPhoto.setImageResource(0);
    ivProfile.setImageResource(0);

    tvCaption.setText(photo.caption);
    tvUserName.setText(photo.userName);
    tvTime.setText(getRelativeTime(photo.publishTime));
    tvLikeCount.setText("" + photo.likesCount);

    // Get Instagram pic and profile pic.
    Picasso.with(getContext()).load(photo.imageUrl).into(ivPhoto);
    Transformation transformation = new RoundedTransformationBuilder()
      .borderColor(Color.WHITE)
      .borderWidthDp(1)
      .cornerRadiusDp(25)
      .oval(false)
      .build();
    Picasso.with(getContext()).load(photo.profileUrl).fit().transform(transformation).into(ivProfile);

    if (photo.comments.size() > 0) {
      tvComment1.setVisibility(View.VISIBLE);
      Comment c = photo.comments.get(0);
      tvComment1.setText(c.shortLine());
    } else {
      tvComment1.setVisibility(View.GONE);
    }
    if (photo.comments.size() > 1) {
      tvComment2.setVisibility(View.VISIBLE);
      Comment c = photo.comments.get(1);
      tvComment2.setText(c.shortLine());
    } else {
      tvComment2.setVisibility(View.GONE);
    }
    return convertView;
  }

  private String getRelativeTime(int createdTime) {
    Date date= new Date();
    long time = date.getTime() / 1000;
    long diff = time - createdTime;
    if (diff < 60) {
      return diff + "s";
    }
    diff /= 60;
    if (diff < 60) {
      return diff + "m";
    }
    diff /= 60;
    if (diff < 24) {
      return diff + "h";
    }
    return (diff / 24) + "d";
  }
}
