package bloop.honk.Fragments;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.util.Collections;
import java.util.List;

import bloop.honk.R;

/**
 * Created by Don on 2017/09/23.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.MyViewHolder> {
    List<XmlParser.Entry> data = Collections.emptyList();
    private LayoutInflater inflater;
    private ItemClickListener mClickListener;

    public NewsAdapter(Context context, List<XmlParser.Entry> data){
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.row, parent,false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        XmlParser.Entry current = data.get(position);
        holder.title.setText(current.title);
        holder.desc.setText(current.summary);
        holder.date.setText(current.pubDate);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView title;
        TextView desc;
        TextView date;
        public MyViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.listText);
            desc = itemView.findViewById(R.id.descText);
            date = itemView.findViewById(R.id.dateText);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }

    }

    //public XMLParser.entry getItem(int id) {
    //    return data.get(id);
    //}

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
