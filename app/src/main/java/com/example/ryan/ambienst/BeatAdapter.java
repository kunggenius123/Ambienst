package com.example.ryan.ambienst;


//import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackView;

public class BeatAdapter extends RecyclerView.Adapter<BeatAdapter.CardViewHolder>{
    private ArrayList<Card> mCardList;

    public static boolean mSaveOption;
    private OnItemClickListener Listener;

    public interface OnItemClickListener{
        void onItemClick(int position);
        void onSaveClick(int position);

    }

    public BeatAdapter(ArrayList<Card> cardList){
        mCardList = cardList;
    }

    public BeatAdapter(ArrayList<Card> cardList, boolean saveOption){

        mCardList = cardList;
        mSaveOption = saveOption;
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.beat_card, parent, false);
        CardViewHolder cardViewHolder = new CardViewHolder(v, Listener, mSaveOption);
        return cardViewHolder;
    }

    @Override
    public void onBindViewHolder(CardViewHolder holder, int position) {
        Card currentCard = mCardList.get(position);
        holder.imageView.setImageResource(currentCard.getImgRes());
        holder.text.setText(currentCard.getLine1());
        holder.text2.setText(currentCard.getLine2());
    }

    @Override
    public int getItemCount() {
        return mCardList.size();
    }

    public void setOnItemClickListener(OnItemClickListener mListener){
        Listener = mListener;
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder{
        public ImageView imageView;
        public TextView text;
        public TextView text2;
        public ImageButton saveButton;



        public CardViewHolder(View view, final OnItemClickListener listener, boolean saveOption){
            super(view);
            imageView = itemView.findViewById(R.id.img);
            text = itemView.findViewById(R.id.textView);
            text2 = itemView.findViewById(R.id.textView2);
            saveButton = itemView.findViewById(R.id.save_button);
            if(BeatAdapter.mSaveOption){

                saveButton.setOnClickListener(v -> {
                    if(listener!=null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.onSaveClick(position);
                        }
                    }
                });
            }
            else{
                saveButton.setVisibility(View.GONE);
            }

            itemView.setOnClickListener(v -> {
                if(listener!=null){
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION){
                        listener.onItemClick(position);
                    }
                }
            });



        }
    }
}