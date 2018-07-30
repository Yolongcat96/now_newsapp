package com.example.android.now_newsapp;

import android.app.Activity;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ArticleAdapter extends ArrayAdapter<Article> {

    public ArticleAdapter(Activity context, ArrayList<Article> articles) {
        super(context, 0, articles);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.article_list, parent, false);
        }

        // Get the {@link article} object located at this position in the list
        Article currentArticle = getItem(position);

        // Find the ImageView in the article_list.xml layout with the ID version_name
        ImageView thumbnailView = (ImageView) listItemView.findViewById(R.id.article_thumbnail);
        String imageUrl = currentArticle.getaImageUrl();
        // Set the thumbnail image to the imageView
        if (imageUrl != null) {
            Picasso.get().load(imageUrl).into(thumbnailView);
        } else {
            // the image place holder is chosen based on the sectionName
            Picasso.get().load(getImageHolder(currentArticle.getaSectionId())).into(thumbnailView);
        }

        // Find the TextView in the article_list.xml
        TextView titleTextView = (TextView) listItemView.findViewById(R.id.article_title);
        // Set the title to the textview
        titleTextView.setText(currentArticle.getaTitle());

        // Find the TextView in the article_list.xml
        TextView dateTextView = (TextView) listItemView.findViewById(R.id.article_date);
        // Set the title to the textview
        dateTextView.setText(currentArticle.getaPublishDate());

        // Find the TextView in the article_list.xml
        TextView authorTextView = (TextView) listItemView.findViewById(R.id.article_author);
        // Set the title to the textview
        authorTextView.setText(currentArticle.getaAuthorName());

        // Return the whole list item layout (containing 1 thumbnailview and 3 textviews)
        // so that it can be shown in the ListView
        return listItemView;
    }

    private int getImageHolder(String sectionName) {

        if (sectionName.compareTo("technology") == 0) {
            return R.drawable.technology_placeholder;
        } else if (sectionName.compareTo("education") == 0) {
            return R.drawable.education_placeholder;
        } else if (sectionName.compareTo("sports") == 0) {
            return R.drawable.sports_placeholder;
        } else if (sectionName.compareTo("politics") == 0) {
            return R.drawable.polictics_placeholder;
        } else {
            return R.drawable.sports_placeholder;
        }
    }


}
