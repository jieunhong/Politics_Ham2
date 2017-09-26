package droidmentor.PoliticTeens_Client.viewholder;

/**
 * Created by Eun bee on 2016-delete_things-19.
 */


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import droidmentor.PoliticTeens_Client.R;
import droidmentor.PoliticTeens_Client.models.JungPost;

public class JungPostViewHolder extends RecyclerView.ViewHolder {

    public TextView titleView;
    public TextView authorView;
    public ImageView starView;
    public TextView numStarsView;
    public TextView category;
    public TextView bodyView;

    public JungPostViewHolder(View itemView) {
        super(itemView);

        titleView = (TextView) itemView.findViewById(R.id.post_title);
        authorView = (TextView) itemView.findViewById(R.id.post_author);
        starView = (ImageView) itemView.findViewById(R.id.star);
        numStarsView = (TextView) itemView.findViewById(R.id.post_num_stars);
        category = (TextView) itemView.findViewById(R.id.category);
        //bodyView = (TextView) itemView.findViewById(R.id.post_body);
    }

    public void bindToPost(JungPost post, View.OnClickListener starClickListener) {
        titleView.setText(post.title);
        authorView.setText(post.author);
        numStarsView.setText(String.valueOf(post.starCount));
        category.setText(post.category);
        //bodyView.setText(post.body);

        starView.setOnClickListener(starClickListener);
    }
}