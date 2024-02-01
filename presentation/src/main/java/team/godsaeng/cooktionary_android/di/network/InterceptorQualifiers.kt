package team.godsaeng.cooktionary_android.di.network

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class ErrorInterceptor

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class AccessTokenInterceptor
