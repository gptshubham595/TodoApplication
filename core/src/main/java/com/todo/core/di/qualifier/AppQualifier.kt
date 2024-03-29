package com.todo.core.di.qualifier

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class RoomDatabaseQualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class RealmDBQualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class SharedPrefDatabaseQualifier
