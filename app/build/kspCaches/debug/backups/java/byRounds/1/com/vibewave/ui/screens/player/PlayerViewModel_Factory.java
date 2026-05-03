package com.vibewave.ui.screens.player;

import com.vibewave.data.repository.FavoritesRepository;
import com.vibewave.player.PlayerController;
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
public final class PlayerViewModel_Factory implements Factory<PlayerViewModel> {
  private final Provider<PlayerController> playerProvider;

  private final Provider<FavoritesRepository> favoritesProvider;

  public PlayerViewModel_Factory(Provider<PlayerController> playerProvider,
      Provider<FavoritesRepository> favoritesProvider) {
    this.playerProvider = playerProvider;
    this.favoritesProvider = favoritesProvider;
  }

  @Override
  public PlayerViewModel get() {
    return newInstance(playerProvider.get(), favoritesProvider.get());
  }

  public static PlayerViewModel_Factory create(Provider<PlayerController> playerProvider,
      Provider<FavoritesRepository> favoritesProvider) {
    return new PlayerViewModel_Factory(playerProvider, favoritesProvider);
  }

  public static PlayerViewModel newInstance(PlayerController player,
      FavoritesRepository favorites) {
    return new PlayerViewModel(player, favorites);
  }
}
