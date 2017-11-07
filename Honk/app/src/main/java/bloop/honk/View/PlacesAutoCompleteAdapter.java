package bloop.honk.View;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import bloop.honk.Model.MapPlace;
import bloop.honk.R;

public class PlacesAutoCompleteAdapter extends RecyclerView.Adapter<PlacesAutoCompleteAdapter.PredictionHolder> {
    private ArrayList<MapPlace> mResultList;
    private Context mContext;
    private int layout;

    public PlacesAutoCompleteAdapter(Context context, int resource) {
        mContext = context;
        layout = resource;
    }

    @Override
    public PredictionHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View convertView = layoutInflater.inflate(layout, viewGroup, false);
        PredictionHolder mPredictionHolder = new PredictionHolder(convertView);
        return mPredictionHolder;
    }

    @Override
    public void onBindViewHolder(PredictionHolder mPredictionHolder, final int i) {
        mPredictionHolder.mPrediction.setText(mResultList.get(i).toString());
    }

    @Override
    public int getItemCount() {
        if (mResultList != null)
            return mResultList.size();
        else
            return 0;
    }

    public void clearList() {
        int size = getItemCount();
        if (size > 0) {
            mResultList.clear();
            notifyItemRangeRemoved(0, size);
        }
    }

    public class PredictionHolder extends RecyclerView.ViewHolder {
        private TextView mPrediction;
        private RelativeLayout mRow;

        public PredictionHolder(View itemView) {
            super(itemView);
            mPrediction = (TextView) itemView.findViewById(R.id.address);
            mRow = (RelativeLayout) itemView.findViewById(R.id.predictedRow);
        }

    }

    public void setmResultList(ArrayList<MapPlace> mResultList) {
        this.mResultList = mResultList;
    }

    public MapPlace getItem(int position) {
        return mResultList.get(position);
    }
}
