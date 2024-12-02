package com.example.filmapp.di

import com.example.filmapp.common.Constants
import com.example.filmapp.data.remote.KinopoiskApi
import com.example.filmapp.data.repository.FilmRepositoryImpl
import com.example.filmapp.domain.repository.FilmRepository
import com.example.filmapp.ui.firebase.GoogleFirebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideKinopoiskApi(): KinopoiskApi{
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(KinopoiskApi::class.java)
    }

    @Provides
    @Singleton
    fun provideFilmRepository(api: KinopoiskApi): FilmRepository{
        return FilmRepositoryImpl(api)
    }
    @Provides
    @Singleton
    fun provideGoogleFirebaseObj(): GoogleFirebase{
        return GoogleFirebase()
    }


}