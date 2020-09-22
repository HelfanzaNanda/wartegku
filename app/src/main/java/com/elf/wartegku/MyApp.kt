package com.elf.wartegku

import android.app.Application
import com.elf.wartegku.repositories.*
import com.elf.wartegku.ui.category.CategoryViewModel
import com.elf.wartegku.ui.category.store.StoreViewModel
import com.elf.wartegku.ui.detail_store.FoodViewModel
import com.elf.wartegku.ui.checkout.CheckoutViewModel
import com.elf.wartegku.ui.login.LoginViewModel
import com.elf.wartegku.ui.main.home.HomeViewModel
import com.elf.wartegku.ui.register.RegisterViewModel
import com.elf.wartegku.ui.result_search.ResultSearchViewModel
import com.elf.wartegku.webservices.ApiClient
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.dsl.module

class MyApp : Application(){
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@MyApp)
            modules(listOf(repositoryModules, viewModelModules, retrofitModule))
        }
    }
}

val retrofitModule = module {
    single { ApiClient.instance() }
}

val repositoryModules = module {
    factory { CategoryRepository(get()) }
    factory { StoreRepository(get()) }
    factory { FoodRepository(get()) }
    factory { OrderRepository(get()) }
    factory { UserRepository(get()) }
}

val viewModelModules = module {
    viewModel { CategoryViewModel(get()) }
    viewModel { HomeViewModel(get(), get()) }
    viewModel { ResultSearchViewModel(get()) }
    viewModel { StoreViewModel(get()) }
    viewModel { FoodViewModel(get()) }
    viewModel { CheckoutViewModel(get()) }
    viewModel { LoginViewModel(get()) }
    viewModel { RegisterViewModel(get()) }
}