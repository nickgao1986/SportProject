package com.nick.apps.pregnancy11.mypedometer.fragment.view;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.nick.apps.pregnancy11.mypedometer.fragment.model.PhoneModel;
import com.imooc.sport.R;


public class PedoUsernamePopup extends BasePopupWindow<PhoneModel> {

    private TextView mTvStandard;
    private TextView mTvTarget;
    private OnStepPopClickListener mPopClickListener;

    public PedoUsernamePopup(Activity activity) {
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
        return R.layout.pedo_user_layout;
    }

    @Override
    protected void initView(@NonNull View view) {

    }

    @Override
    public void onClick(View v) {

    }

    public interface OnStepPopClickListener {
        void onStepPopClick(View view, int showMode);
    }

    public void setOnStepPopClickListener(OnStepPopClickListener listener) {
        this.mPopClickListener = listener;
    }

}
