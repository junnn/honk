package bloop.honk.View;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import bloop.honk.Model.FeedItem;
import bloop.honk.R;

/**
 * Created by Don on 2017/10/02.
 */

public class FeedsAdapter extends RecyclerView.Adapter<FeedsAdapter.MyViewHolder> {

    private List<FeedItem> data = Collections.emptyList();
    private LayoutInflater inflater;
    private Context context;

    public FeedsAdapter(Context context, List<FeedItem> data) {
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final FeedItem current = data.get(position);
        holder.title.setText(current.getType());
        /*if(current.getType().equalsIgnoreCase("heavy traffic"))
            holder.image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.favstar));*/
        String msg = current.getMessage().substring(current.getMessage().indexOf(' ') + 1);

        holder.desc.setText(msg);

        //String date_time = current.Message.substring(0, current.Message.indexOf(' '));
        String date_time = current.getDate_time();

        SimpleDateFormat spf = new SimpleDateFormat("(dd/MM)hh:mm");
        try {
            Date newDate = spf.parse(date_time);
            spf = new SimpleDateFormat("dd MMM hh:mm a");
            date_time = spf.format(newDate);
            holder.date.setText(date_time);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView desc;
        TextView date;
//        ImageView image;

        public MyViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.listText);
            desc = itemView.findViewById(R.id.descText);
            date = itemView.findViewById(R.id.dateText);
//            image = itemView.findViewById(R.id.imageView2);
        }
    }
}
