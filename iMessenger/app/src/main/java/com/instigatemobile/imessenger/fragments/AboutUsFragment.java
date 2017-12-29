package com.instigatemobile.imessenger.fragments;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.instigatemobile.imessenger.R;
import com.instigatemobile.imessenger.adapters.PersonAdapter;

public class AboutUsFragment extends Fragment implements View.OnClickListener {
    private Button phoneNumber;
    private View view;

    public AboutUsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_about_us, container, false);
        initViews();
        setListeners();
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycleViewPerson);
        recyclerView.setHasFixedSize(true);
        PersonAdapter adapter = new PersonAdapter();
        recyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        return view;
    }

    private void initViews() {
        phoneNumber = (Button) view.findViewById(R.id.phoneNumber);
    }

    private void setListeners() {
        phoneNumber.setOnClickListener(this);
    }

    private void callNumber() {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:+37460445500"));
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, 10);
            return;
        }

        try {
            startActivity(callIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getActivity().getApplicationContext(), "Your activity is not founded...", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View view) {
        if (view == phoneNumber) {
            callNumber();
        }
    }
}