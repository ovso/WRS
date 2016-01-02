package net.onepagebook.wrs.main;

import android.content.Context;

/**
 * Created by ovso on 2016. 1. 2..
 */
public class MainPresenterImpl implements MainPresenter {
    MainPresenter.View mView;
    MainModel mModel;
    public MainPresenterImpl(MainPresenter.View view) {
        mView = view;
        mModel = new MainModel();
    }

    @Override
    public void onCreate() {
        mView.onInit();
    }

    @Override
    public void onPageSelected(Context context, int position) {
        if(position > 0) {
            mModel.stopWRS(context);
        }
    }
}
