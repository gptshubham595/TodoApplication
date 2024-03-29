package com.todo.core.di.qualifier

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ProcessorRoomDB


@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ProcessorRealmDB

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ProcessorSharedPref
