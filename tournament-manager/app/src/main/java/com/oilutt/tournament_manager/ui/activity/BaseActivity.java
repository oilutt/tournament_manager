package com.oilutt.tournament_manager.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.oilutt.tournament_manager.R;
import com.oilutt.tournament_manager.ui.dialog.DialogProgress;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.enums.SnackbarType;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by oilut on 21/08/2017.
 */

public class BaseActivity extends MvpAppCompatActivity {

    protected Toolbar toolbar;
    protected ImageView rightIcon;
    private DialogProgress dialogProgress;
    protected boolean runningBackground = false;

    @Override
    protected void onResume() {
        super.onResume();
        runningBackground = false;
    }

    @Override
    protected void onStop() {
        super.onStop();
        runningBackground = true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDialogProgress();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public void openActivity(Class<?> openActivity) {
        Intent intent = new Intent();
        intent.setClass(this, openActivity);
        startActivity(intent);
    }

    public void openTeamList(int quantidade) {
        Intent intent = new Intent();
        intent.setClass(this, TeamListActivity.class);
        intent.putExtra("quantidade", quantidade);
        startActivity(intent);
    }

    public void openActivityWithoutHist(Class<?> openActivity) {
        Intent intent = new Intent();
        intent.setClass(this, openActivity);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void initDialogProgress() {
        dialogProgress = new DialogProgress(this);
    }

    protected void setupToolbar(boolean isBack) {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            if (isBack) {
                toolbar.setNavigationIcon(R.drawable.ic_back);
                toolbar.setNavigationOnClickListener(getToolbarOnBackClick());
            }
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    public void setUpToolbarText(int title, boolean isBack) {
        setUpToolbarText(getString(title), isBack);
    }

    public void setUpToolbarText(String title, boolean isBack) {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            assert getSupportActionBar() != null;
            if (!TextUtils.isEmpty(title)) {
                getSupportActionBar().setDisplayShowTitleEnabled(true);
                getSupportActionBar().setTitle(title);
            } else {
                getSupportActionBar().setDisplayShowTitleEnabled(false);
            }
            if (isBack) {
                toolbar.setNavigationIcon(R.drawable.ic_back);
                toolbar.setNavigationOnClickListener(v -> onBackPressed());
            }
        }
    }

    public void changeTitleToolbar(String text) {
        if (toolbar != null) getSupportActionBar().setTitle(text);
    }

    public void showBackToolback() {
        if (toolbar == null) return;
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    public void hideBackToolbar() {
        if (toolbar == null) return;
        toolbar.setNavigationIcon(null);
    }

    private void hideRightIcon() {
        if (rightIcon != null) {
            rightIcon.setVisibility(View.GONE);
        }
    }

    public void hidenKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        //check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void showProgressDialog(String mensagem) {
        showProgressDialog(true, mensagem);
    }

    public void showProgressDialog(boolean isShow, String mensagem) {
        if (dialogProgress != null) {
            if (isShow) {
                dialogProgress.show();
                if (!TextUtils.isEmpty(mensagem)) {
                    dialogProgress.setTitle(mensagem);
                } else {
                    dialogProgress.setTitle("Aguarde...");
                }
            } else {
                if (dialogProgress.isShowing()) {
                    dialogProgress.dismiss();
                }
            }
        }
    }

    public void showSnack(String msg) {
        if (!TextUtils.isEmpty(msg)) {
            msg = msg.replace("\\n", " ");
            SnackbarManager.show(Snackbar.with(getApplicationContext())
                    .type(SnackbarType.MULTI_LINE)
                    .text(msg)
                    .duration(Snackbar.SnackbarDuration.LENGTH_SHORT), this);
        }
    }

    public void dismissProgressDialog() {
        showProgressDialog(false, null);
    }

    protected void setupToolbarWithRightLogoutIcon(boolean isBack) {
        setupToolbar(isBack);
        setupRightIcon();
    }

    private void setupRightIcon() {
        if (toolbar != null) {
            rightIcon = (ImageView) findViewById(R.id.ic_toolbar_logout);
            rightIcon.setVisibility(View.VISIBLE);
            rightIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }

    public View.OnClickListener getToolbarOnBackClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        };
    }
}
