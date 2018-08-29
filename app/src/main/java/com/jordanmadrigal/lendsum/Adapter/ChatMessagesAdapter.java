package com.jordanmadrigal.lendsum.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jordanmadrigal.lendsum.Model.Message;
import com.jordanmadrigal.lendsum.R;

import java.util.List;

public class ChatMessagesAdapter extends RecyclerView.Adapter<ChatMessagesAdapter.ChatMessagesViewHolder> {

    private List<Message> mMessageDataSet;

    public ChatMessagesAdapter(List<Message> messageList) {
        mMessageDataSet = messageList;
    }

    public class ChatMessagesViewHolder extends RecyclerView.ViewHolder {

        private TextView nameText, msgText, dateText;

        public ChatMessagesViewHolder(View itemView) {
            super(itemView);

            nameText = itemView.findViewById(R.id.dummyChatNameTextView);
            msgText = itemView.findViewById(R.id.dummyChatMsgTextView);
            dateText = itemView.findViewById(R.id.dummyChatDateText);

        }
    }

    @NonNull
    @Override
    public ChatMessagesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_messsage_view_items, null);

        return new ChatMessagesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatMessagesViewHolder holder, int position) {

        final Message msgData = mMessageDataSet.get(position);

        holder.nameText.setText(msgData.getName());
        holder.msgText.setText(msgData.getMessage());
        holder.dateText.setText(msgData.getDate());

        }

    @Override
    public int getItemCount() {
        return mMessageDataSet.size();
    }

}
