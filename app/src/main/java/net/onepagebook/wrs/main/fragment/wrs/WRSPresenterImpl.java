package net.onepagebook.wrs.main.fragment.wrs;

public class WRSPresenterImpl implements WRSPresenter {
    private WRSPresenter.View mView;
    private WRSModel mModel;
    public WRSPresenterImpl(WRSPresenter.View view) {
        mView = view;
        mModel = new WRSModel();
    }

    @Override
    public void onActivityCreate() {
        mView.onInit();
    }

    @Override
    public void onClickPlay(android.view.View view) {
        mView.play(mModel.getWRSText(view.getContext()));
    }
}
