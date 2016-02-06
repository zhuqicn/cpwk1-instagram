package com.codepath.cpw1instagram;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.Date;
import java.util.List;

class CropSquareTransformation implements Transformation {
  @Override public Bitmap transform(Bitmap source) {
    int size = Math.min(source.getWidth(), source.getHeight());
    int x = (source.getWidth() - size) / 2;
    int y = (source.getHeight() - size) / 2;
    Bitmap result = Bitmap.createBitmap(source, x, y, size, size);
    if (result != source) {
      source.recycle();
    }
    return result;
  }

  @Override public String key() { return "square()"; }
}

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
    TextView tvCommentCount = (TextView)convertView.findViewById(R.id.tvCommentCount);
    ImageView ivPhoto = (ImageView)convertView.findViewById(R.id.ivPhoto);
    ImageView ivProfile = (ImageView)convertView.findViewById(R.id.ivProfile);
    ImageButton ibPlay = (ImageButton)convertView.findViewById(R.id.ibPlay);
    TextView tvComment1 = (TextView) convertView.findViewById(R.id.tvComment1);
    TextView tvComment2 = (TextView)convertView.findViewById(R.id.tvComment2);
    TextView tvAllComments = (TextView)convertView.findViewById(R.id.tvViewAllComments);

    ibPlay.setVisibility(View.INVISIBLE);
    ivPhoto.setImageResource(0);
    ivProfile.setImageResource(0);

    if (photo.caption.isEmpty()) {
      tvCaption.setVisibility(View.GONE);
    } else {
      tvCaption.setVisibility(View.VISIBLE);
      tvCaption.setText(photo.caption);
    }
    tvUserName.setText(photo.userName);
    tvTime.setText(getRelativeTime(photo.publishTime));
    tvLikeCount.setText("" + photo.likesCount);
    tvCommentCount.setText("" + photo.commentCount);

    // Get Instagram pic.
    Picasso
      .with(getContext())
      .load(photo.imageUrl)
      .placeholder(R.drawable.placeholder)
      .transform(new CropSquareTransformation())
      .into(ivPhoto);

    // Transformation to make profile pic as circle.
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
    if (photo.commentCount > 2) {
      tvAllComments.setText("View all " + photo.commentCount + " comments");
      tvAllComments.setVisibility(View.VISIBLE);
      final String id = photo.id;
      tvAllComments.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          Intent i = new Intent(getContext(), CommentsActivity.class);
          i.putExtra("post_id", id);
          getContext().startActivity(i);
        }
      });
    } else {
      tvAllComments.setVisibility(View.INVISIBLE);
    }

    if (photo.isVideo) {
      ibPlay.setVisibility(View.VISIBLE);
      final String videoUrl = photo.videoUrl;
      ibPlay.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          Intent myIntent = new Intent(getContext(), VideoViewActivity.class);
          myIntent.putExtra("video_url", videoUrl);
          getContext().startActivity(myIntent);
        }
      });
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
