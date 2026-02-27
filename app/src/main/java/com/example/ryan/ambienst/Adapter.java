package com.example.ryan.ambienst;


//import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class Adapter extends RecyclerView.Adapter<Adapter.CardViewHolder>{
    private ArrayList<Card> mCardList;

    public boolean mRemixOption;

    private OnItemClickListener Listener;

    public interface OnItemClickListener{
        void onItemClick(int position);
        void onButtonClick(int position);

    }

    public Adapter(ArrayList<Card> cardList){
        mCardList = cardList;
    }
    public Adapter(ArrayList<Card> cardList, boolean remixOption){

        mCardList = cardList;
        mRemixOption = remixOption;
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        int cardType;

        if(mRemixOption){
            cardType = R.layout.card;
            Log.d("mRemixOption ", "Yes");
        }
        else{
            Log.d("mRemixOption ", "No");
            cardType = R.layout.saved_card;
        }



        View v = LayoutInflater.from(parent.getContext()).inflate(cardType, parent, false);
        CardViewHolder cardViewHolder = new CardViewHolder(v, Listener, mRemixOption);
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
        public ImageView imageButton;

        public CardViewHolder(View view, final OnItemClickListener listener, boolean remixOption){
            super(view);

            imageView = itemView.findViewById(R.id.img);
            text = itemView.findViewById(R.id.textView);
            text2 = itemView.findViewById(R.id.textView2);

            if(remixOption){
                imageButton = itemView.findViewById(R.id.image_remix);


            }
            else{
                imageButton = itemView.findViewById(R.id.recommend_button);

            }

            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!=null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.onButtonClick(position);
                        }
                    }
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!=null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });


        }
    }
}