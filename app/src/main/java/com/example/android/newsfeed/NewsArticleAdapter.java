package com.example.android.newsfeed;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class NewsArticleAdapter extends ArrayAdapter<NewsArticle> {

    public NewsArticleAdapter(Activity context, ArrayList<NewsArticle> newsArticles) {
        super(context, 0, newsArticles);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        final NewsArticle currentnewsArticle = getItem(position);

        ImageView thumbnailImageView = (ImageView) listItemView.findViewById(R.id.thumbnail);
        //new ImageLoadTask(currentnewsArticle.getImageId(), thumbnailImageView).execute();

        Picasso.with(getContext()).load(currentnewsArticle.getImageId()).into(thumbnailImageView);

        TextView headlineTextView = (TextView) listItemView.findViewById(R.id.headline);
        headlineTextView.setText(String.valueOf(currentnewsArticle.getHeadline()));

        TextView timestampTextView = (TextView) listItemView.findViewById(R.id.timestamp);
        timestampTextView.setText(String.valueOf(currentnewsArticle.getPublicationDate()));

        LinearLayout listItem = (LinearLayout) listItemView.findViewById(R.id.list_item);
        listItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse(currentnewsArticle.getUrl()); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                getContext().startActivity(intent);
            }
        });
        return listItemView;
    }
}
