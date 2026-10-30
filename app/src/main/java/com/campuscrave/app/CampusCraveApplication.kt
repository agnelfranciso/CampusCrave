package com.campuscrave.app

import android.app.Application
import com.campuscrave.app.data.repository.CampusCraveRepository

class CampusCraveApplication : Application() {
    val repository: CampusCraveRepository by lazy {
        CampusCraveRepository(this)
    }
}

