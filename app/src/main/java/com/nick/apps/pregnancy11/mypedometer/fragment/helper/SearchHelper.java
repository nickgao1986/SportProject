package com.nick.apps.pregnancy11.mypedometer.fragment.helper;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.imooc.sport.R;
import com.nick.apps.pregnancy11.mypedometer.fragment.util.PedoUtils;
import com.nick.apps.pregnancy11.mypedometer.fragment.util.Util;

import java.util.ArrayList;
import java.util.List;



public class SearchHelper<T> {

    private static final String TAG = SearchHelper.class.getSimpleName();

    private Context mContext;
    private String mSearchKey;
    public EditText mSearchView;
    public Button mSearchButton;
    public ImageView mCleanButton;
    public boolean mShowSearchButton = true;
    public List<T> mDataSource = new ArrayList<>();
    public SearchCallBack mSearchCallBack;

    private SearchHelper(){
    }

    public static SearchHelper getInstance(){
        return new SearchHelper();
    }

    public void init(Context context, View searchBar, Button searchBtn, List<T> dataSource, SearchCallBack searchCallBack) {
        mSearchView = (EditText)searchBar.findViewById(R.id.chat_team_search);
        mCleanButton = (ImageView)searchBar.findViewById(R.id.chat_clean_btn);
        init(context, mSearchView, mCleanButton, searchBtn, dataSource, searchCallBack);
    }

    public void init(Context context, EditText searchView, ImageView cleanBtn, Button searchBtn, List<T> dataSource, SearchCallBack searchCallBack) {
        mContext = context;
        mSearchView = searchView;
        mCleanButton = cleanBtn;
        mSearchButton = searchBtn;
        mShowSearchButton = (mSearchButton != null);
        mDataSource = dataSource;
        mSearchCallBack = searchCallBack;
        if (null == mSearchCallBack) {
            return;
        }
        mCleanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSearchView.setText("");
            }
        });

        mSearchView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mSearchCallBack.onSearchViewClick();
                }
            }
        });

        mSearchView.addTextChangedListener(new SearchKeywordTextWatch(mSearchView));
        mSearchView.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        mSearchView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (EditorInfo.IME_ACTION_SEARCH == actionId) {
                    String keyword = mSearchView.getText().toString();
                    if (!TextUtils.isEmpty(keyword.trim())) {
                        searchResult(keyword);
                        mSearchView.clearFocus();
                        PedoUtils.hideInputMethod(mContext, mSearchView);
                    } else {
                        mSearchCallBack.resetData();
                        mSearchView.requestFocus();
                    }
                    return true;
                }
                return false;
            }
        });
        if (mShowSearchButton) {
            mSearchButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null == mSearchCallBack) {
                        return;
                    }
                    if ("取消".equals(mSearchButton.getText())) {
                        mSearchCallBack.cancelSearch();
                    } else {
                        if (TextUtils.isEmpty(mSearchKey.trim())) {
                            return;
                        }
                        searchResult(mSearchKey.trim());
                        mSearchView.clearFocus();
                        PedoUtils.hideInputMethod(mContext, mSearchView);
                    }
                }
            });
        }
    }

    /**
     * 本地搜索结果
     */
    private ArrayList<T> mSearchResults = new ArrayList<>();
    public void searchResult(String key) {
        if (null == mSearchCallBack || Util.isEmpty(mDataSource)) {
            return;
        }
        mSearchResults.clear();
        mSearchCallBack.clearData();
        for (T data : mDataSource) {
            if (mSearchCallBack.isSearchResult(data, key)) {
                mSearchResults.add(data);
            }
        }
        mSearchCallBack.setSearchResult(key, mSearchResults);
    }

    class SearchKeywordTextWatch implements TextWatcher {
        private EditText et;

        SearchKeywordTextWatch(EditText et) {
            this.et = et;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (null == mSearchCallBack) {
                return;
            }
            mSearchKey = s.toString();
            if (!TextUtils.isEmpty(mSearchKey)) {
                mCleanButton.setVisibility(View.VISIBLE);
                searchResult(mSearchKey);
                if(mShowSearchButton) {
                    mSearchButton.setText(R.string.chat_search);
                }
            } else {
                mCleanButton.setVisibility(View.GONE);
                mSearchCallBack.resetData();
                if(mShowSearchButton) {
                    mSearchButton.setText(R.string.chat_cancel);
                }
            }
        }
    }

    public void dispatchTouchEventActionDown(MotionEvent ev, Activity activity) {
        View v = activity.getCurrentFocus();
        if (isShouldHideInput(v, ev)) {
            mSearchView.clearFocus();
            PedoUtils.hideInputMethod(mContext, mSearchView);
        }
    }

    private boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            return event.getY() < top || event.getY() > bottom;
        }
        return false;
    }

    public interface SearchCallBack<T>{
        void clearData();
        void resetData();
        boolean isSearchResult(T data, String key);
        void setSearchResult(String key, ArrayList<T> result);
        void cancelSearch();
        String getInputTips();
        void onSearchViewClick();
    }
}
