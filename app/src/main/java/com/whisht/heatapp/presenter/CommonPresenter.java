package com.whisht.heatapp.presenter;

import com.whisht.heatapp.constant.NetConstant;
import com.whisht.heatapp.model.CommonModel;
import com.whisht.heatapp.presenter.base.BasePresenter;
import com.whisht.heatapp.view.base.IBaseView;

public class CommonPresenter extends BasePresenter {

    private String TAG = getClass().getSimpleName();
    private CommonModel commonModel;
    public CommonPresenter(IBaseView baseView) {
        super(baseView);
        commonModel = new CommonModel();
    }

    public void queryIndexStatus() {
        commonModel.queryIndexStatus().subscribe((resultMap)->{
            if (resultMap.getResultCode() == NetConstant.ERR_CODE_SUCCESS) {
                mBaseView.success(NetConstant.APP_MSG_QUERY_INDEX_STATUS, resultMap);
            } else {
                mBaseView.fail(NetConstant.APP_MSG_QUERY_INDEX_STATUS, resultMap.getResultCode(), resultMap.getResultMsg());
            }
        }, (e)->{
            mBaseView.fail(NetConstant.APP_MSG_QUERY_INDEX_STATUS, NetConstant.CODE_SYS_EXCEPTION, NetConstant.MSG_TIP_SYSTEM_ERROR);
        });
    }

}
