package com.kyawhut.imindemoapp.di

import android.content.Context
import com.kyawhut.imindemoapp.utils.printer.Printer
import com.kyawhut.imindemoapp.utils.printer.PrinterImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

/**
 * @author kyawhtut
 * @date 2/14/22
 */

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun providePrinter(@ApplicationContext context: Context): Printer {
        return PrinterImpl(context)
    }
}
