package com.jordanmadrigal.lendsum;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;


    public ChatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView  = inflater.inflate(R.layout.chat_item_list, container, false);

        mRecyclerView = rootView.findViewById(R.id.chatItemRecyclerList);

        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);

        List<Message> messages = new ArrayList<>();

        messages.add(new Message("Jordan Madrigal", "This is a message", "7/11/90"));
        messages.add(new Message("Lesly Ramirez", "This is a message", "7/11/90"));
        messages.add(new Message("Vickie Madrigal", "This is a message", "7/11/90"));
        messages.add(new Message("Jordan Madrigal", "This is a message", "7/11/90"));
        messages.add(new Message("Jordan Madrigal", "This is a message", "7/11/90"));
        messages.add(new Message("Jordan Madrigal", "This is a message", "7/11/90"));
        messages.add(new Message("Jordan Madrigal", "This is a message", "7/11/90"));
        messages.add(new Message("Jordan Madrigal", "This is a message", "7/11/90"));
        messages.add(new Message("Jordan Madrigal", "This is a message", "7/11/90"));

        ChatMessagesAdapter adapter = new ChatMessagesAdapter(messages);
        mRecyclerView.setAdapter(adapter);

        return rootView;
    }

}
