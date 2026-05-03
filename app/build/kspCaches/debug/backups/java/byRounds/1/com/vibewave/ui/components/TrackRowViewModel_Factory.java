package com.vibewave.ui.components;

import com.vibewave.data.repository.FavoritesRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
public final class TrackRowViewModel_Factory implements Factory<TrackRowViewModel> {
  private final Provider<FavoritesRepository> favoritesProvider;

  public TrackRowViewModel_Factory(Provider<FavoritesRepository> favoritesProvider) {
    this.favoritesProvider = favoritesProvider;
  }

  @Override
  public TrackRowViewModel get() {
    return newInstance(favoritesProvider.get());
  }

  public static TrackRowViewModel_Factory create(Provider<FavoritesRepository> favoritesProvider) {
    return new TrackRowViewModel_Factory(favoritesProvider);
  }

  public static TrackRowViewModel newInstance(FavoritesRepository favorites) {
    return new TrackRowViewModel(favorites);
  }
}
