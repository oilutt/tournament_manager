package com.oilutt.tournament_manager.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.oilutt.tournament_manager.R;
import com.oilutt.tournament_manager.presentation.MainActivity.MainActivityCallback;
import com.oilutt.tournament_manager.presentation.MainActivity.MainActivityPresenter;
import com.oilutt.tournament_manager.utils.FontsOverride;
import com.oilutt.tournament_manager.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements MainActivityCallback,
        NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.adView)
    AdView mAdView;

    View header;

    @InjectPresenter
    MainActivityPresenter presenter;

    @ProvidePresenter
    MainActivityPresenter createPresenter() {
        return new MainActivityPresenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        setUpToolbarText(R.string.app_name, false);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        navigationView.setNavigationItemSelectedListener(this);
        header = navigationView.getHeaderView(0);
        FontsOverride.setDefaultFont(this, "MONOSPACE", getString(R.string.monstserrat_regular));
        toggle.syncState();
        presenter.clickMeusCamps();
        setHeaderClick();
        getBundle();
    }

    private void getBundle() {
        if (getIntent().hasExtra("invite") && !getIntent().getStringExtra("invite").equals("")) {
            presenter.getInvite(getIntent().getStringExtra("invite"));
        }
    }

    private void setHeaderClick() {
        header.setOnClickListener(v -> {
            presenter.clickHeader();
            drawerLayout.closeDrawer(GravityCompat.START);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @OnClick(R.id.fab)
    public void onClickFab() {
        openActivity(AddCampActivity.class);
    }

    @Override
    public void setFoto(String foto) {
        ImageView fotoView = (ImageView) header.findViewById(R.id.foto);
        if (foto.startsWith("https:/"))
            Glide.with(this).load(foto).placeholder(R.drawable.ic_person_black_24dp).into(fotoView);
        else
            Glide.with(this).load(Base64.decode(foto, Base64.DEFAULT)).placeholder(R.drawable.ic_person_black_24dp).into(fotoView);
    }

    @Override
    public void setFotoPath(String path) {
        ImageView fotoView = (ImageView) header.findViewById(R.id.foto);
        Glide.with(this).load(path).placeholder(R.drawable.ic_person_black_24dp).into(fotoView);
    }

    @Override
    public void setNome(String nome) {
        TextView nomeTxt = (TextView) header.findViewById(R.id.name);
        nomeTxt.setText(nome);
    }

    @Override
    public void setEmail(String email) {
        TextView emailTxt = (TextView) header.findViewById(R.id.email);
        emailTxt.setText(email);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.meus_camps) {
            presenter.clickMeusCamps();
        } else if (id == R.id.invite_camp) {
            presenter.clickInviteCamp();
        } else if (id == R.id.busca_camp) {
            presenter.clickBuscaCamp();
        } else if (id == R.id.sorteio_fifa) {
            presenter.sorteio();
        } else if (id == R.id.logout) {
            presenter.logout();
        }

        hidenKeyBoard();

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void replaceFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_frame, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void openLogin() {
        openActivity(LoginActivity.class);
        finish();
    }

    @Override
    public void openSorteio() {
        openActivity(SorteioFIFAActivity.class);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        presenter.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void launchCrop(Uri uri) {
        Utils.launchCrop(uri, this);
    }

    @Override
    public void openDetails(String invite) {
        Intent intent = new Intent(this, CampeonatoDetailsActivity.class);
        intent.putExtra("invite", invite);
        startActivity(intent);
    }
}
