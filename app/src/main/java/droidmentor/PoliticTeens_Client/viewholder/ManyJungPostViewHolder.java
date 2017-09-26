package droidmentor.PoliticTeens_Client.viewholder;

/**
 * Created by Eun bee on 2016-delete_things-19.
 */


import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import droidmentor.PoliticTeens_Client.GetUserId;
import droidmentor.PoliticTeens_Client.R;
import droidmentor.PoliticTeens_Client.models.Jundang;
import droidmentor.PoliticTeens_Client.models.JungPost;

public class ManyJungPostViewHolder extends RecyclerView.ViewHolder {

    public TextView bodyView;
    public ImageView mydang_color;
    public TextView mydang_name;
    public TextView mydang_people;
    public TextView mydang_ideology;
    public GradientDrawable color;

    public ManyJungPostViewHolder(View rootView) {
        super(rootView);

        mydang_color = (ImageView)rootView.findViewById(R.id.myjungdang_color);
        color = (GradientDrawable) mydang_color.getBackground();
        mydang_name = (TextView)rootView.findViewById(R.id.mydang_name);
        mydang_people = (TextView)rootView.findViewById(R.id.mydang_people);
        mydang_ideology = (TextView)rootView.findViewById(R.id.mydang_ideology);

    }

    public void bindToPost(Jundang post) {

        Log.d("ggg",""+post.just_name);
        mydang_name.setText(post.just_name);
        Log.d("ggg",""+post.just_color);
        color.setColor(Color.parseColor("#" + post.just_color));

        mydang_people.setText(post.partisian+"");
        mydang_ideology.setText(post.just_idea);
    }
}