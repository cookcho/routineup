package com.routineup

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class RoutineUpApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // 앱 초기화 코드
    }
}
