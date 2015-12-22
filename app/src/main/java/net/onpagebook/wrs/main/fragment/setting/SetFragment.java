package net.onpagebook.wrs.main.fragment.setting;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import net.onepagebook.wrs.common.Log;
import net.onpagebook.wrs.R;

/**
 * Created by jaeho_oh on 2015-12-21.
 */
public class SetFragment extends Fragment implements SetPresenter.View, SeekBar.OnSeekBarChangeListener, View.OnClickListener {

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
    private SetPresenter mPresenter;
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPresenter = new SetPresenterImpl(this);
        mPresenter.onActivityCreate();
    }

    @Override
    public void onInit() {
        SeekBar seekBar = (SeekBar) mContentView.findViewById(R.id.seekBar);
        Button button = (Button) mContentView.findViewById(R.id.button);
        button.setOnClickListener(this);
        seekBar.setOnSeekBarChangeListener(this);
    }

    @Override
    public void navigateToActivity(Intent intent, int requestCode) {
        startActivityForResult(intent, requestCode);

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        mPresenter.onActivityResult(getActivity(), requestCode, resultCode, data);

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        Log.d("progress="+progress);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.button) {
            mPresenter.onClick(v);
        }
    }
}
