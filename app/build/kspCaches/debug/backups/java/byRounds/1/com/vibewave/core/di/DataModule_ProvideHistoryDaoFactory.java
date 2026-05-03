package com.vibewave.core.di;

import com.vibewave.data.db.HistoryDao;
import com.vibewave.data.db.VibeWaveDatabase;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata
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
public final class DataModule_ProvideHistoryDaoFactory implements Factory<HistoryDao> {
  private final Provider<VibeWaveDatabase> dbProvider;

  public DataModule_ProvideHistoryDaoFactory(Provider<VibeWaveDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public HistoryDao get() {
    return provideHistoryDao(dbProvider.get());
  }

  public static DataModule_ProvideHistoryDaoFactory create(Provider<VibeWaveDatabase> dbProvider) {
    return new DataModule_ProvideHistoryDaoFactory(dbProvider);
  }

  public static HistoryDao provideHistoryDao(VibeWaveDatabase db) {
    return Preconditions.checkNotNullFromProvides(DataModule.INSTANCE.provideHistoryDao(db));
  }
}
