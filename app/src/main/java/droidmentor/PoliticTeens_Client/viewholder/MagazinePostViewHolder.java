package droidmentor.PoliticTeens_Client.viewholder;

/**
 * Created by Eun bee on 2016-delete_things-19.
 */


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import droidmentor.PoliticTeens_Client.MainActivity;
import droidmentor.PoliticTeens_Client.R;
import droidmentor.PoliticTeens_Client.models.JungPost;
import droidmentor.PoliticTeens_Client.models.MagazinePost;

public class MagazinePostViewHolder extends RecyclerView.ViewHolder {


    public TextView titleView;
    public TextView contentView;
    public ImageView starView;
    public TextView numStarsView;
    public ImageView pictureView;

    public MagazinePostViewHolder(View itemView) {
        super(itemView);

        titleView = (TextView) itemView.findViewById(R.id.magazine_title);
        contentView = (TextView) itemView.findViewById(R.id.magazine_content);
        starView = (ImageView) itemView.findViewById(R.id.star);
        numStarsView = (TextView) itemView.findViewById(R.id.post_num_stars);
        pictureView = (ImageView) itemView.findViewById(R.id.magazine_picture);
        //bodyView = (TextView) itemView.findViewById(R.id.post_body);
    }

    public void bindToPost(MagazinePost post, View.OnClickListener starClickListener) {
        titleView.setText(post.title);
        contentView.setText(post.content);
        numStarsView.setText(String.valueOf(post.starCount));
        Picasso.with(itemView.getContext())
                .load(post.picture)
                .resize(100, 100)
                .centerCrop()
                .placeholder(R.drawable.aaa)
                .into(pictureView);
        //bodyView.setText(post.body);

        starView.setOnClickListener(starClickListener);
    }
}