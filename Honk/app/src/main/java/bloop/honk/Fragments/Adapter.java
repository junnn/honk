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

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {
    private LayoutInflater inflater;
    List<XmlParser.Entry> data = Collections.emptyList();

    public Adapter(Context context, List<XmlParser.Entry> data){
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

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView desc;
        TextView date;
        public MyViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.listText);
            desc = itemView.findViewById(R.id.descText);
            date = itemView.findViewById(R.id.dateText);
        }
    }
}
