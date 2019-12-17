package com.cammace.aurora

import android.app.Application
import timber.log.Timber
import timber.log.Timber.DebugTree


class AuroraApplication : Application() {

  override fun onCreate() {
    super.onCreate()

    // Initialize Timber if in debug mode
    if (BuildConfig.DEBUG) {
      Timber.plant(DebugTree())
    }
  }
}