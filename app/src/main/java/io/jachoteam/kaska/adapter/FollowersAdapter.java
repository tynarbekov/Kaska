package io.jachoteam.kaska.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.jachoteam.kaska.R;
import io.jachoteam.kaska.models.User;

public class FollowersAdapter extends ArrayAdapter<User> {
    private Context mContext;
    private List<User> usersList = new ArrayList<>();

    public FollowersAdapter(@NonNull Context context, ArrayList<User> list) {
        super(context, 0, list);
        mContext = context;
        usersList = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.add_friends_item, parent, false);

        User user = usersList.get(position);

        TextView name = (TextView) listItem.findViewById(R.id.username_text);
        name.setText(user.getUsername());

        CircleImageView photo_image= (CircleImageView) listItem.findViewById(R.id.photo_image);

        return listItem;
    }
}
