package com.example.todoapplication.di.qualifier

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ProcessorRoomDB

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ProcessorSharedPref
