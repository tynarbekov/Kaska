package io.jachoteam.kaska.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.jachoteam.kaska.FollowersListActivity;
import io.jachoteam.kaska.R;
import io.jachoteam.kaska.data.firebase.FirebaseUsersRepository;
import io.jachoteam.kaska.models.User;
import io.jachoteam.kaska.screens.common.GlideApp;

public class FollowersAdapter extends ArrayAdapter<User> {
    private Context mContext;
    private List<User> usersList = new ArrayList<>();
    FirebaseUsersRepository firebaseUsersRepository;

    public FollowersAdapter(@NonNull Context context, ArrayList<User> list) {
        super(context, 0, list);
        mContext = context;
        usersList = list;
        firebaseUsersRepository = new FirebaseUsersRepository();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.add_friends_item, parent, false);

        final User user = usersList.get(position);

        TextView name = (TextView) listItem.findViewById(R.id.username_text);
        TextView nameText = (TextView) listItem.findViewById(R.id.name_text);
        Button unfollowButton = (Button) listItem.findViewById(R.id.unfollow_btn);
        Button followButton = (Button) listItem.findViewById(R.id.follow_btn);
//        followButton.setVisibility(View.GONE);
//        unfollowButton.setVisibility(View.GONE);
        name.setText(user.getUsername());
        nameText.setText(user.getName());

//        unfollowButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.i("unfollow", user.toString());
//                firebaseUsersRepository.deleteFollow(((FollowersListActivity)mContext).uid, user.getUid());
//            }
//        });

        CircleImageView photo_image= (CircleImageView) listItem.findViewById(R.id.photo_image);
        GlideApp.with(mContext).load(user.getPhoto()).fallback(R.drawable.person).into(photo_image);

        return listItem;
    }
}
