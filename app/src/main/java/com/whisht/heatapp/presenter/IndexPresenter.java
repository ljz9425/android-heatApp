package com.whisht.heatapp.presenter;

import com.whisht.heatapp.model.IndexModel;
import com.whisht.heatapp.presenter.base.BasePresenter;
import com.whisht.heatapp.view.base.IBaseView;

public class IndexPresenter extends BasePresenter {

    private String TAG = getClass().getSimpleName();
    private IndexModel indexModel;
    public IndexPresenter(IBaseView baseView) {
        super(baseView);
        indexModel = new IndexModel();
    }

}
