package bloop.honk.Fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import bloop.honk.R;

/**
 * Created by Jun Hao Ng on 6/9/2017.
 */

public class NewsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        getActivity().setTitle("DON TESTING");//set the title on the toolbar

        return view;
    }
}
