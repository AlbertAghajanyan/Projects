package com.instigatemobile.imessenger.adapters;

import android.content.Context;
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

public class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.PersonViewHolder> {

    ArrayList<User> personsArray;
    private Context mContext;

    public PersonAdapter(ArrayList<User> personsArray) {
        this.personsArray = personsArray;
    }

    public PersonAdapter(ArrayList<User> personsArray, Context mContext) {
        this.personsArray = personsArray;
        this.mContext = mContext;
    }

    public PersonAdapter() {
        initializePerson();
    }

    private void initializePerson() {
        personsArray = new ArrayList<>();
        personsArray.add(new User("Mary Antonyan", R.drawable.ic_tab_person));
        personsArray.add(new User("Vachagan Hovhannisyan", R.drawable.ic_tab_person));
        personsArray.add(new User("Vrej Unanyan", R.drawable.ic_tab_person));
        personsArray.add(new User("Albert Aghajanyan", R.drawable.ic_tab_person));

    }

    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.person_card, parent, false);
        PersonViewHolder personViewHolder = new PersonViewHolder(view);
        return personViewHolder;
    }

    @Override
    public void onBindViewHolder(PersonViewHolder holder, int position) {
        String arr[] = personsArray.get(position).name.split(" ");
        holder.personNameTextView.setText(arr[0]);
        holder.personSurnameTextView.setText(arr[1]);
        holder.personImageView.setImageResource(personsArray.get(position).resource);
    }

    @Override
    public int getItemCount() {
        return this.personsArray.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public class PersonViewHolder extends RecyclerView.ViewHolder {
        ImageView personImageView;
        TextView personNameTextView;
        TextView personSurnameTextView;
        CardView cardView;

        public PersonViewHolder(View itemView) {
            super(itemView);
            this.personNameTextView = (TextView) itemView.findViewById(R.id.person_name_text_view);
            this.personSurnameTextView = (TextView) itemView.findViewById(R.id.person_surname_text_view);
            this.personImageView = (ImageView) itemView.findViewById(R.id.person_image_view);
        }

        public PersonViewHolder(View itemView, ImageView personImageView, TextView personNameTextView, TextView personSurnameTextView, CardView cardView) {
            super(itemView);
            this.personImageView = personImageView;
            this.personNameTextView = personNameTextView;
            this.personSurnameTextView = personSurnameTextView;
            this.cardView = cardView;
        }
    }
}