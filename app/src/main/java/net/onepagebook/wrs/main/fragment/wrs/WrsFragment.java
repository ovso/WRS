package net.onepagebook.wrs.main.fragment.wrs;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.UiThread;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import net.onepagebook.wrs.common.Log;
import net.onpagebook.wrs.R;

import java.security.MessageDigestSpi;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by jaeho_oh on 2015-12-21.
 */
public class WrsFragment extends Fragment implements WRSPresenter.View, View.OnClickListener {

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

    int i = 0;
    private void show(final String[] content) {

        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                showText(msg.obj.toString());
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                      getActivity().runOnUiThread(new Runnable() {
                          @Override
                          public void run() {
                            clearText();
                          }
                      });
                    }
                }, 200); // 문장 표시 시간
            }
        };

        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Message.obtain(handler, 0, content[i]).sendToTarget();

                if (i == content.length - 1) {

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            clearText();
                            i = 0;
                        }
                    });
                    timer.cancel();
                } else {
                    i++;
                }
            }
        }, 0, 1000); // 문장 간격 시간
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
    public void play(String content) {
        int contentLength = content.length();
        Log.d("길이 = " + contentLength);


        int sliceLength = 20;

        Log.d("나몫 = " + (contentLength/sliceLength));
        Log.d("나머지 = " + (contentLength%sliceLength));
        String[] strs = new String[contentLength%sliceLength==0?(contentLength/sliceLength):((contentLength/sliceLength)+1)];
        Log.d("size = " + strs.length);

        for (int i = 0; i < strs.length; i++) {

            int start = i*sliceLength;
            int end = start + sliceLength;
            int last = strs.length - 1;
            if(i == last) {
                end = start + (contentLength%sliceLength);
            }
            strs[i] = content.subSequence(start, end).toString();
        }
        show(strs);
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
    public void showTraningTime(String time) {
        ((TextView)mContentView.findViewById(R.id.tv_traning_time)).setText(time);
    }
}