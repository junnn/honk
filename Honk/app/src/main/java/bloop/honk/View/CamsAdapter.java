package bloop.honk.View;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import bloop.honk.Model.CamItem;
import bloop.honk.R;

public class CamsAdapter extends RecyclerView.Adapter<CamsAdapter.MyViewHolder> {
    Context context;
    Geocoder geocoder;
    List<CamItem> data = Collections.emptyList();
    private LayoutInflater inflater;
    private CamsAdapter.ItemClickListener ClickListener;

    public CamsAdapter(Context context, List<CamItem> data) {
        geocoder = new Geocoder(context, Locale.getDefault());
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.camera_item, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final CamItem current = data.get(position);

        Picasso.with(context).load(current.getImageLink()).into(holder.cam_image);

        double lat = current.getLatitude();
        double lng = current.getLongitude();
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            //Log.i("PostActivity", current.getCameraID() + ": " + addresses.get(0));
            String str = addresses.get(0).getAddressLine(0) + " " + addresses.get(0).getThoroughfare();
            holder.title.setText(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title;
        ImageView cam_image;

        public MyViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.listText);
            cam_image = (ImageView) itemView.findViewById(R.id.cam_image);

            title.setOnClickListener(this);
            cam_image.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (ClickListener != null) ClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    public CamItem getItem(int id) {
        return data.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(CamsAdapter.ItemClickListener itemClickListener) {
        this.ClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
