package net.onepagebook.wrs.main.fragment.wrs;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.onepagebook.wrs.common.Log;
import net.onpagebook.wrs.R;

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
        mPresenter.onActivityCreate();

    }

    int i = 0;
    private void show(final String[] content) {
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                //super.handleMessage(msg);
                TextView tv = (TextView) mContentView.findViewById(R.id.tv_show);
                tv.setText("");
                tv.setText(msg.obj.toString());
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                      getActivity().runOnUiThread(new Runnable() {
                          @Override
                          public void run() {
                              TextView tv = (TextView) mContentView.findViewById(R.id.tv_show);
                              tv.setText("");

                          }
                      });
                    }
                }, 100);
            }
        };

        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Message.obtain(handler, 0, content[i]).sendToTarget();
                if(i == content.length -1) {
                    timer.cancel();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            TextView tv = (TextView) mContentView.findViewById(R.id.tv_show);
                            tv.setText("");
                            i = 0;
                        }
                    });
                } else {
                    i++;
                }
            }
        }, 0, 500);

    }

    @Override
    public void onInit() {
        mContentView.findViewById(R.id.btn_play).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_play) {
            mPresenter.onClickPlay(v);
        }
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
            if(i == strs.length-1) {
                end = start + (contentLength%sliceLength);
            }
            strs[i] = content.subSequence(start, end).toString();
        }

        show(strs);
    }
}
