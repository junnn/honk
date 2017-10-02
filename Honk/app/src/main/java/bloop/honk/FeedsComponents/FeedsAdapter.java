package bloop.honk.FeedsComponents;

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
 * Created by Don on 2017/10/02.
 */

public class FeedsAdapter extends RecyclerView.Adapter<FeedsAdapter.MyViewHolder>{

    List<FeedItem> data = Collections.emptyList();
    private LayoutInflater inflater;

    public FeedsAdapter(Context context, List<FeedItem> data){
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
        final FeedItem current = data.get(position);
        holder.title.setText(current.type);

        String msg = current.message.substring(current.message.indexOf(' ')+1);

        holder.desc.setText(msg);

        String date_time = current.message.substring(0,current.message.indexOf(' '));

        holder.date.setText(date_time);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
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
