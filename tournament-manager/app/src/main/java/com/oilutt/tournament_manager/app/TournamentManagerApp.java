package com.oilutt.tournament_manager.app;

import android.app.Application;

import com.oilutt.tournament_manager.R;
import com.oilutt.tournament_manager.utils.PreferencesManager;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by oilut on 21/08/2017.
 */

public class TournamentManagerApp extends Application {

    private static TournamentManagerApp instance;
    public static PreferencesManager preferencesManager;
    public static DatabaseReference database;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        init();
    }

    public static TournamentManagerApp getInstance() {
        return instance;
    }

    private void init() {
        initPreferences();
        initFirebase();
        configRealm();
        configFonts();
    }

    private void initFirebase() {
        database = FirebaseDatabase.getInstance().getReference();
        database.goOnline();
    }

    private void initPreferences() {
        PreferencesManager.initializeInstance(this);
        preferencesManager = PreferencesManager.getInstance();
    }

    private void configRealm() {
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);
    }

    private void configFonts() {
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath(getString(R.string.monstserrat_regular))
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }
}
