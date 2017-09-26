package droidmentor.PoliticTeens_Client.viewholder;

/**
 * Created by Eun bee on 2016-delete_things-19.
 */


import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import droidmentor.PoliticTeens_Client.R;
import droidmentor.PoliticTeens_Client.models.JungConPost;
import droidmentor.PoliticTeens_Client.models.JungPost;

public class JungConPostViewHolder extends RecyclerView.ViewHolder {

    public TextView titleView;
    public TextView authorView;
    public ImageView starView;
    public TextView numStarsView;
    public ImageView jungColorView;
    public GradientDrawable color;
    public TextView bodyView;

    public JungConPostViewHolder(View itemView) {
        super(itemView);

        titleView = (TextView) itemView.findViewById(R.id.post_title);
        authorView = (TextView) itemView.findViewById(R.id.post_author);
        starView = (ImageView) itemView.findViewById(R.id.star);
        numStarsView = (TextView) itemView.findViewById(R.id.post_num_stars);
        jungColorView = (ImageView) itemView.findViewById(R.id.dang_color);
        color = (GradientDrawable)jungColorView.getBackground();
        //bodyView = (TextView) itemView.findViewById(R.id.post_body);
    }

    public void bindToPost(JungConPost post, View.OnClickListener starClickListener) {
        titleView.setText(post.title);
        authorView.setText(post.author);
        numStarsView.setText(String.valueOf(post.starCount));
        color.setColor(Color.parseColor(post.color));
        //bodyView.setText(post.body);

        starView.setOnClickListener(starClickListener);
    }
}