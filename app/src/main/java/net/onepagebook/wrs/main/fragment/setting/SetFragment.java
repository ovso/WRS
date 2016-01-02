package net.onepagebook.wrs.main.fragment.setting;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import net.onepagebook.wrs.common.Log;
import net.onpagebook.wrs.R;

/**
 * Created by jaeho_oh on 2015-12-21.
 */
public class SetFragment extends Fragment implements SetPresenter.View, View.OnClickListener, SeekBar.OnSeekBarChangeListener {

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
        mPresenter.onActivityCreate(getActivity());
    }

    @Override
    public void onInit() {
        mContentView.findViewById(R.id.button).setOnClickListener(this);
        ((SeekBar)mContentView.findViewById(R.id.seek_text_show_time)).setOnSeekBarChangeListener(this);
        ((SeekBar)mContentView.findViewById(R.id.seek_text_show_interval)).setOnSeekBarChangeListener(this);
        ((SeekBar)mContentView.findViewById(R.id.seek_text_size)).setOnSeekBarChangeListener(this);
        ((SeekBar)mContentView.findViewById(R.id.seek_text_once_length)).setOnSeekBarChangeListener(this);
    }

    @Override
    public void navigateToActivity(Intent intent, int requestCode) {
        startActivityForResult(intent, requestCode);

    }

    @Override
    public void setTextShowTime(String text) {
        ((TextView)mContentView.findViewById(R.id.tv_text_show_time)).setText(text);
    }

    @Override
    public void setTextShowInterval(String text) {
        ((TextView)mContentView.findViewById(R.id.tv_text_show_interval)).setText(text);
    }

    @Override
    public void setTextSize(String text) {
        ((TextView)mContentView.findViewById(R.id.tv_text_size)).setText(text);
    }

    @Override
    public void setTextOnceLength(String text) {
        ((TextView)mContentView.findViewById(R.id.tv_text_once_length)).setText(text);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        mPresenter.onActivityResult(getActivity(), requestCode, resultCode, data);

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.button) {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
                if (getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 111);
                } else {
                    mPresenter.onClick(v);
                }
            } else {
                mPresenter.onClick(v);
            }
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        mPresenter.onProgressChanged(seekBar, progress);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        mPresenter.onStartTrackingTouch(seekBar);
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mPresenter.onStopTrackingTouch(seekBar);
    }
}
