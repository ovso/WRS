package net.onepagebook.wrs.main.fragment.wrs;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import net.onepagebook.wrs.common.Log;
import net.onepagebook.wrs.main.MainActivity;
import net.onpagebook.wrs.R;

public class WrsFragment extends Fragment implements WRSPresenter.View, View.OnClickListener{

    public final static WrsFragment newInstance() {
        Log.d("");
        WrsFragment fragment = new WrsFragment();
        return fragment;
    }
    private View mContentView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        return mContentView = inflater.from(getActivity()).inflate(R.layout.fragment_wrs, container, false);
    }
    private WRSPresenter mPresenter;
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mPresenter = new WRSPresenterImpl(this);
        mPresenter.onActivityCreate(getActivity());

    }

    @Override
    public void onInit() {
        mContentView.findViewById(R.id.btn_play_pause).setOnClickListener(this);
        mContentView.findViewById(R.id.btn_stop).setOnClickListener(this);
        mContentView.findViewById(R.id.btn_skip_previous).setOnClickListener(this);
        mContentView.findViewById(R.id.btn_skip_next).setOnClickListener(this);
        mContentView.findViewById(R.id.btn_current_repeat).setOnClickListener(this);
        mContentView.findViewById(R.id.btn_current_hold).setOnClickListener(this);
        mContentView.findViewById(R.id.btn_loop).setOnClickListener(this);
        mContentView.findViewById(R.id.btn_reset).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        mPresenter.onClick(v);
    }
    @Override
    public void showText(final String text) {
        ((TextView) mContentView.findViewById(R.id.tv_show)).setText(text);
    }

    @Override
    public void clearText() {
        ((TextView) mContentView.findViewById(R.id.tv_show)).setText("");
    }

    @Override
    public void showToast(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showPlayButton() {
        ((ImageButton)mContentView.findViewById(R.id.btn_play_pause)).setImageResource(R.drawable.ic_av_play_arrow);
    }

    @Override
    public void showPauseButton() {
        ((ImageButton)mContentView.findViewById(R.id.btn_play_pause)).setImageResource(R.drawable.ic_av_pause);
    }

    @Override
    public void showLoopOn() {
        ((ImageButton)mContentView.findViewById(R.id.btn_loop)).setImageResource(R.drawable.ic_av_repeat_on);
    }

    @Override
    public void showLoopOff() {
        ((ImageButton)mContentView.findViewById(R.id.btn_loop)).setImageResource(R.drawable.ic_av_repeat);
    }

    @Override
    public void showResetDialog(final Handler handler) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.dialog_loop_msg);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Message.obtain(handler,0).sendToTarget();
            }
        });
        builder.setNegativeButton(R.string.cancel, null);
        builder.show();
    }

    @Override
    public void showTraningTimer(String time) {
        ((TextView)mContentView.findViewById(R.id.tv_traning_timer)).setText(time);
    }

    @Override
    public void showTraningTimeFix(String time) {
        ((TextView)mContentView.findViewById(R.id.tv_traning_time_fix)).setText(time);
    }

    @Override
    public void changePage(WRSScheduleManager.PAGE_STATE state) {
        if(state == WRSScheduleManager.PAGE_STATE.SET) {
            ((MainActivity)getActivity()).mViewPager.setCurrentItem(1);
        }
    }
}