package net.onpagebook.wrs.main.fragment.setting;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.onepagebook.wrs.common.Log;
import net.onpagebook.wrs.R;

/**
 * Created by jaeho_oh on 2015-12-21.
 */
public class SetFragment extends Fragment {

    public final static SetFragment newInstance() {
        Log.d("");
        SetFragment fragment = new SetFragment();
        return fragment;
    }
    private View mContentView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        return mContentView = inflater.from(getActivity()).inflate(R.layout.fragment_set, container, false);
    }
}
