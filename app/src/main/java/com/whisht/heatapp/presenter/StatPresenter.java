package com.whisht.heatapp.presenter;

import com.whisht.heatapp.constant.NetConstant;
import com.whisht.heatapp.model.IndexModel;
import com.whisht.heatapp.model.StatModel;
import com.whisht.heatapp.presenter.base.BasePresenter;
import com.whisht.heatapp.view.base.IBaseView;

public class StatPresenter extends BasePresenter {

    private String TAG = getClass().getSimpleName();
    private StatModel statModel;
    public StatPresenter(IBaseView baseView) {
        super(baseView);
        statModel = new StatModel();
    }

    public void statDay(int pageIndex, int pageSize, String startDate, String stopDate) {
        statModel.statDay(pageIndex, pageSize, startDate, stopDate).subscribe((result)->{
            if(result.getResultCode() == NetConstant.ERR_CODE_SUCCESS) {
                mBaseView.success(NetConstant.APP_MSG_STAT_DAY, result);
            } else {
                mBaseView.fail(NetConstant.APP_MSG_STAT_DAY, result.getResultCode(), result.getResultMsg());
            }
        }, (e)->{
            mBaseView.fail(NetConstant.APP_MSG_STAT_DAY, NetConstant.CODE_SYS_EXCEPTION, NetConstant.MSG_TIP_SYSTEM_ERROR);
        });
    }


    public void statDayList(int pageIndex, int pageSize, String unitCode, String startDate, String stopDate) {
        statModel.statDayList(pageIndex, pageSize, unitCode, startDate, stopDate).subscribe((result)->{
            if(result.getResultCode() == NetConstant.ERR_CODE_SUCCESS) {
                mBaseView.success(NetConstant.APP_MSG_STAT_DAY_UNIT, result);
            } else {
                mBaseView.fail(NetConstant.APP_MSG_STAT_DAY_UNIT, result.getResultCode(), result.getResultMsg());
            }
        }, (e)->{
            mBaseView.fail(NetConstant.APP_MSG_STAT_DAY_UNIT, NetConstant.CODE_SYS_EXCEPTION, NetConstant.MSG_TIP_SYSTEM_ERROR);
        });
    }

    public void statMonth(int pageIndex, int pageSize, String startDate, String stopDate) {
        statModel.statMonth(pageIndex, pageSize, startDate, stopDate).subscribe((result)->{
            if(result.getResultCode() == NetConstant.ERR_CODE_SUCCESS) {
                mBaseView.success(NetConstant.APP_MSG_STAT_MONTH, result);
            } else {
                mBaseView.fail(NetConstant.APP_MSG_STAT_MONTH, result.getResultCode(), result.getResultMsg());
            }
        }, (e)->{
            mBaseView.fail(NetConstant.APP_MSG_STAT_MONTH, NetConstant.CODE_SYS_EXCEPTION, NetConstant.MSG_TIP_SYSTEM_ERROR);
        });
    }


    public void statMonthList(int pageIndex, int pageSize, String unitCode, String startDate, String stopDate) {
        statModel.statMonthList(pageIndex, pageSize, unitCode, startDate, stopDate).subscribe((result)->{
            if(result.getResultCode() == NetConstant.ERR_CODE_SUCCESS) {
                mBaseView.success(NetConstant.APP_MSG_STAT_MONTH_UNIT, result);
            } else {
                mBaseView.fail(NetConstant.APP_MSG_STAT_MONTH_UNIT, result.getResultCode(), result.getResultMsg());
            }
        }, (e)->{
            mBaseView.fail(NetConstant.APP_MSG_STAT_MONTH_UNIT, NetConstant.CODE_SYS_EXCEPTION, NetConstant.MSG_TIP_SYSTEM_ERROR);
        });
    }

    public void statYear(int pageIndex, int pageSize, String startDate, String stopDate) {
        statModel.statYear(pageIndex, pageSize, startDate, stopDate).subscribe((result)->{
            if(result.getResultCode() == NetConstant.ERR_CODE_SUCCESS) {
                mBaseView.success(NetConstant.APP_MSG_STAT_YEAR, result);
            } else {
                mBaseView.fail(NetConstant.APP_MSG_STAT_YEAR, result.getResultCode(), result.getResultMsg());
            }
        }, (e)->{
            mBaseView.fail(NetConstant.APP_MSG_STAT_YEAR, NetConstant.CODE_SYS_EXCEPTION, NetConstant.MSG_TIP_SYSTEM_ERROR);
        });
    }


    public void statYearList(int pageIndex, int pageSize, String unitCode, String startDate, String stopDate) {
        statModel.statYearList(pageIndex, pageSize, unitCode, startDate, stopDate).subscribe((result)->{
            if(result.getResultCode() == NetConstant.ERR_CODE_SUCCESS) {
                mBaseView.success(NetConstant.APP_MSG_STAT_YEAR_UNIT, result);
            } else {
                mBaseView.fail(NetConstant.APP_MSG_STAT_YEAR_UNIT, result.getResultCode(), result.getResultMsg());
            }
        }, (e)->{
            mBaseView.fail(NetConstant.APP_MSG_STAT_YEAR_UNIT, NetConstant.CODE_SYS_EXCEPTION, NetConstant.MSG_TIP_SYSTEM_ERROR);
        });
    }

}
