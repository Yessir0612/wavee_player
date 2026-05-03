package com.vibewave.data.repository;

import com.vibewave.data.db.FavoriteDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
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
public final class FavoritesRepository_Factory implements Factory<FavoritesRepository> {
  private final Provider<FavoriteDao> daoProvider;

  public FavoritesRepository_Factory(Provider<FavoriteDao> daoProvider) {
    this.daoProvider = daoProvider;
  }

  @Override
  public FavoritesRepository get() {
    return newInstance(daoProvider.get());
  }

  public static FavoritesRepository_Factory create(Provider<FavoriteDao> daoProvider) {
    return new FavoritesRepository_Factory(daoProvider);
  }

  public static FavoritesRepository newInstance(FavoriteDao dao) {
    return new FavoritesRepository(dao);
  }
}
