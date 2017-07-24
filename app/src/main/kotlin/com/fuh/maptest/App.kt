package com.fuh.maptest

import android.app.Application
import com.facebook.stetho.Stetho
import com.fuh.mapstest.BuildConfig
import com.uphyca.stetho_realm.RealmInspectorModulesProvider
import io.realm.Realm
import io.realm.RealmConfiguration
import timber.log.Timber
import java.util.regex.Pattern

/**
 * Created by lll on 20.07.2017.
 */
class App : Application() {
    override fun onCreate() {
        super.onCreate()

        initRealm()
        if (BuildConfig.DEBUG) {
            initTimber()
            initStetho()
        }
    }

    private fun initTimber() {
        Timber.plant(Timber.DebugTree())
    }

    private fun initRealm() {
        Realm.init(this)

        val config = RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build()

        Realm.setDefaultConfiguration(config)
    }

    private fun initStetho() {
        val provider = RealmInspectorModulesProvider.builder(this)
//                .withFolder(cacheDir)
//                .withEncryptionKey("encrypted.realm", key)
                .withMetaTables()
                .withDescendingOrder()
                .withLimit(1000)
                .databaseNamePattern(Pattern.compile(".+\\.realm"))
                .build()

        val initializer = Stetho.newInitializerBuilder(this)
                .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                .enableWebKitInspector(provider)
                .build()

        Stetho.initialize(initializer)
    }
}