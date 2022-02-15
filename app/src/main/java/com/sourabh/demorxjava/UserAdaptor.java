package com.sourabh.demorxjava;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

public class UserAdaptor extends BaseAdapter {
    private List<UserInfo> userList = new ArrayList<>();
    private Context mcontext;

    public UserAdaptor(Context mcontext){
        this.mcontext = mcontext;
    }

    @Override public int getCount() {
        return userList.size();
    }

    @Override public UserInfo getItem(int position) {
        if (position < 0 || position >= userList.size()) {
            return null;
        } else {
            return userList.get(position);
        }
    }

    @Override public long getItemId(int position) {
        return position;
    }

    @Override public View getView(int position, View convertView, ViewGroup parent) {
        final View view = (convertView != null ? convertView : createView(parent));
        final UserViewHolder viewHolder = (UserViewHolder) view.getTag();
        viewHolder.setUserRelatedData(mcontext,getItem(position));
        return view;
    }

    public void setUserInfoList(@Nullable List<UserInfo> repos) {
        if (repos == null) {
            return;
        }
        userList.clear();
        userList.addAll(repos);
        notifyDataSetChanged();
    }

    private View createView(ViewGroup parent) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View view = inflater.inflate(R.layout.user_item, parent, false);
        final UserViewHolder viewHolder = new UserViewHolder(view);
        view.setTag(viewHolder);
        return view;
    }

    public void add(UserInfo gitHubRepo) {
        userList.add(gitHubRepo);
        notifyDataSetChanged();
    }

    private static class UserViewHolder {

        private TextView textUserName;
        private TextView textType;
        private ImageView imgUser;

        public UserViewHolder(View view) {
            textUserName = (TextView) view.findViewById(R.id.text_name);
            textType = (TextView) view.findViewById(R.id.text_type);
            imgUser = (ImageView) view.findViewById(R.id.imageview_poster);
        }

        public void setUserRelatedData(Context mcontext, UserInfo user) {
            textUserName.setText(user.login);
            textType.setText(user.type);

            //Picasso.with(mcontext).load(user.avatar_url).into(imgUser);
//            Picasso.with(mcontext).load(user.avatar_url).resize(60, 60).
//                    centerCrop().into(imgUser);
//            Picasso.with(mcontext).load(user.avatar_url).fit().centerCrop()
//                    .placeholder(R.drawable.userdefault)
//                    .error(R.drawable.userdefault)
//                    .into(imgUser);
//            Picasso.with(mcontext).load(user.avatar_url)
//                    .resize(60, 60)
//                    .into(imgUser, new Callback() {
//                        @Override
//                        public void onSuccess() {
//                            Bitmap imageBitmap = ((BitmapDrawable) imgUser.getDrawable()).getBitmap();
//                            RoundedBitmapDrawable imageDrawable = RoundedBitmapDrawableFactory.create(mcontext.getResources(), imageBitmap);
//                            imageDrawable.setCircular(true);
//                            imageDrawable.setCornerRadius(Math.max(imageBitmap.getWidth(), imageBitmap.getHeight()) / 2.0f);
//                            imgUser.setImageDrawable(imageDrawable);
//                        }
//                        @Override
//                        public void onError() {
//                            imgUser.setImageResource(R.drawable.userdefault);
//                        }
//                    });

            Picasso.with(mcontext)
                    .load(user.avatar_url)
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .into(imgUser, new Callback() {
                        @Override
                        public void onSuccess() {
                            Log.v("Picasso","fetch image success in first time.");
                            makeimageViewCircular(mcontext);
                        }

                        @Override
                        public void onError() {
                            //Try again online if cache failed
                            Log.v("Picasso","Could not fetch image in first time...");
                            Picasso.with(mcontext).load(user.avatar_url).networkPolicy(NetworkPolicy.NO_CACHE)
                                    .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE).error(R.drawable.userdefault)
                                    .into(imgUser, new Callback() {

                                        @Override
                                        public void onSuccess() {
                                            Log.v("Picasso","fetch image success in try again.");
                                            makeimageViewCircular(mcontext);
                                        }

                                        @Override
                                        public void onError() {
                                            Log.v("Picasso","Could not fetch image again...");
                                        }

                                    });
                        }
                    });
        }

        public void makeimageViewCircular(Context mcontext)
        {
            Bitmap imageBitmap = ((BitmapDrawable) this.imgUser.getDrawable()).getBitmap();
            RoundedBitmapDrawable imageDrawable = RoundedBitmapDrawableFactory.create(mcontext.getResources(), imageBitmap);
            imageDrawable.setCircular(true);
            imageDrawable.setCornerRadius(Math.max(imageBitmap.getWidth(), imageBitmap.getHeight()) / 2.0f);
            imgUser.setImageDrawable(imageDrawable);
        }
    }
}
