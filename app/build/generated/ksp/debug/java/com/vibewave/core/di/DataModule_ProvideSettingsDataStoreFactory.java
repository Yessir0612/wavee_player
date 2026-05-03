package com.vibewave.core.di;

import android.content.Context;
import androidx.datastore.core.DataStore;
import androidx.datastore.preferences.core.Preferences;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast",
    "deprecation"
})
public final class DataModule_ProvideSettingsDataStoreFactory implements Factory<DataStore<Preferences>> {
  private final Provider<Context> ctxProvider;

  public DataModule_ProvideSettingsDataStoreFactory(Provider<Context> ctxProvider) {
    this.ctxProvider = ctxProvider;
  }

  @Override
  public DataStore<Preferences> get() {
    return provideSettingsDataStore(ctxProvider.get());
  }

  public static DataModule_ProvideSettingsDataStoreFactory create(Provider<Context> ctxProvider) {
    return new DataModule_ProvideSettingsDataStoreFactory(ctxProvider);
  }

  public static DataStore<Preferences> provideSettingsDataStore(Context ctx) {
    return Preconditions.checkNotNullFromProvides(DataModule.INSTANCE.provideSettingsDataStore(ctx));
  }
}
