package bloop.honk.NewsComponents;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.util.Collections;
import java.util.List;

import bloop.honk.NewsDetails;
import bloop.honk.R;

/**
 * Created by Don on 2017/09/23.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.MyViewHolder> {
    Context context;
    List<Entry> data = Collections.emptyList();
    private LayoutInflater inflater;
    private ItemClickListener mClickListener;

    public NewsAdapter(Context context, List<Entry> data){
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.context=context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.row, parent,false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Entry current = data.get(position);
        holder.title.setText(current.title);
        holder.desc.setText(current.summary);
        holder.date.setText(current.pubDate);
        //holder.type.setText(current.news_type);

        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(context, NewsDetails.class);
                String url = "http://"+current.getLink();
                //Log.i("WEBVIEW",url);
                intent.putExtra("Link", url);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView title;
        TextView desc;
        TextView date;
        //TextView type;

        public MyViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.listText);
            desc = itemView.findViewById(R.id.descText);
            date = itemView.findViewById(R.id.dateText);
            //type = itemView.findViewById(R.id.news_type);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }

    }

    public String getLink(int id){
        return data.get(id).getLink();
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
