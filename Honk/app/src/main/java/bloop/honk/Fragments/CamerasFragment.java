package bloop.honk.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import bloop.honk.R;

/**
 * Created by Jun Hao Ng on 6/9/2017.
 */

public class CamerasFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cameras, container, false);
        getActivity().setTitle("Cameras");//set the title on the toolbar

        return view;
    }
}
