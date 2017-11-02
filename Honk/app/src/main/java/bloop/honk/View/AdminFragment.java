package bloop.honk.View;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import bloop.honk.R;

/**
 * Created by Jun Hao Ng on 28/10/2017.
 */

public class AdminFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin, container, false);
        Toast.makeText(getContext(), "You're an admin.", Toast.LENGTH_SHORT).show();
        return view;
    }
}
