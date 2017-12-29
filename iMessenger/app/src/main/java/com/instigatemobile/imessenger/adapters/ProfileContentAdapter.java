package com.instigatemobile.imessenger.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.instigatemobile.imessenger.R;
import com.instigatemobile.imessenger.models.User;

import java.util.ArrayList;
import java.util.List;

public class ProfileContentAdapter extends RecyclerView.Adapter<ProfileContentAdapter.ProfileViewHolder> {
    List<ProfileContent> profileContent;

    public ProfileContentAdapter(User profile) {
        initProfileContent(profile);
    }

    private void initProfileContent(User profile) {
        ArrayList<ProfileContent> contentList = new ArrayList<>();
//TODO:
//        contentList.add(new ProfileContent("Favorits", profile.getFavoritesCount(), R.mipmap.favorites));
//        contentList.add(new ProfileContent("ContactsFragment", profile.getFavoritesCount(), R.mipmap.contacts));
//        profileContent = contentList;
    }

    @Override
    public ProfileViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_content_item, parent, false);
        ProfileViewHolder pvh = new ProfileViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(ProfileViewHolder personViewHolder, int i) {
        personViewHolder.profileElement.setText(profileContent.get(i).getElementName());
        personViewHolder.count.setText(profileContent.get(i).getCount() + "");
        personViewHolder.elementImage.setImageResource(profileContent.get(i).getImagePath());
    }

    @Override
    public int getItemCount() {
        return this.profileContent.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class ProfileViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView profileElement;
        TextView count;
        ImageView elementImage;

        ProfileViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cv);
            profileElement = (TextView) itemView.findViewById(R.id.element);
            count = (TextView) itemView.findViewById(R.id.count);
            elementImage = (ImageView) itemView.findViewById(R.id.elementImage);
        }
    }

    class ProfileContent {
        private int count;
        private String elementName;
        private int imagePath;

        public ProfileContent(String elementName, int count, int imagePath) {
            this.elementName = elementName;
            this.count = count;
            this.imagePath = imagePath;
        }

        public int getCount() {
            return count;
        }

        public String getElementName() {
            return elementName;
        }

        public int getImagePath() {
            return imagePath;
        }
    }
}