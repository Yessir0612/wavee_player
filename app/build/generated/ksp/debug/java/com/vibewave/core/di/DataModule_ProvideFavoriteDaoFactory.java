package com.vibewave.core.di;

import com.vibewave.data.db.FavoriteDao;
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
public final class DataModule_ProvideFavoriteDaoFactory implements Factory<FavoriteDao> {
  private final Provider<VibeWaveDatabase> dbProvider;

  public DataModule_ProvideFavoriteDaoFactory(Provider<VibeWaveDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public FavoriteDao get() {
    return provideFavoriteDao(dbProvider.get());
  }

  public static DataModule_ProvideFavoriteDaoFactory create(Provider<VibeWaveDatabase> dbProvider) {
    return new DataModule_ProvideFavoriteDaoFactory(dbProvider);
  }

  public static FavoriteDao provideFavoriteDao(VibeWaveDatabase db) {
    return Preconditions.checkNotNullFromProvides(DataModule.INSTANCE.provideFavoriteDao(db));
  }
}
