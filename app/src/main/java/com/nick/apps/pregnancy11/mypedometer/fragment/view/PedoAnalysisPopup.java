package com.nick.apps.pregnancy11.mypedometer.fragment.view;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.nick.apps.pregnancy11.mypedometer.fragment.model.PhoneModel;
import com.nick.apps.pregnancy11.mypedometer.fragment.util.StepConstants;
import com.imooc.sport.R;




public class PedoAnalysisPopup extends BasePopupWindow<PhoneModel> {

    private TextView mTvTarget;
    private TextView mTvSetWeight;
    private OnStepPopClickListener mPopClickListener;

    public PedoAnalysisPopup(Activity activity) {
        super(activity);
    }

    @Override
    protected void init(Activity activity) {
        super.init(activity);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setOutsideTouchable(true);
    }

    @Override
    protected void bindData(Activity activity, @Nullable PhoneModel data) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.pedo_analysis_popup_layout;
    }

    @Override
    protected void initView(@NonNull View view) {
        mTvTarget = (TextView) view.findViewById(R.id.tv_set_target);
        mTvSetWeight = (TextView) view.findViewById(R.id.tv_set_weight);
        mTvSetWeight.setOnClickListener(this);
        mTvTarget.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (R.id.tv_set_target == v.getId()) {
            if (mPopClickListener != null) {
                mPopClickListener.onStepPopClick(v, StepConstants.MODE_SET_TARGET);
            }
            dismiss();
        } else if (R.id.tv_set_weight == v.getId()) {
            if (mPopClickListener != null) {
                mPopClickListener.onStepPopClick(v, StepConstants.MODE_SET_WEIGHT);
            }
            dismiss();
        }
    }

    public interface OnStepPopClickListener {
        void onStepPopClick(View view, int showMode);
    }

    public void setOnStepPopClickListener(OnStepPopClickListener listener) {
        this.mPopClickListener = listener;
    }

}
