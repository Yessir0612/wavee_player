package com.vibewave.ui.screens.profile;

import com.vibewave.data.repository.AuthRepository;
import com.vibewave.data.repository.FavoritesRepository;
import com.vibewave.data.repository.HistoryRepository;
import com.vibewave.data.repository.SettingsRepository;
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
public final class ProfileViewModel_Factory implements Factory<ProfileViewModel> {
  private final Provider<SettingsRepository> settingsRepoProvider;

  private final Provider<HistoryRepository> historyRepoProvider;

  private final Provider<FavoritesRepository> favoritesRepoProvider;

  private final Provider<AuthRepository> authRepoProvider;

  public ProfileViewModel_Factory(Provider<SettingsRepository> settingsRepoProvider,
      Provider<HistoryRepository> historyRepoProvider,
      Provider<FavoritesRepository> favoritesRepoProvider,
      Provider<AuthRepository> authRepoProvider) {
    this.settingsRepoProvider = settingsRepoProvider;
    this.historyRepoProvider = historyRepoProvider;
    this.favoritesRepoProvider = favoritesRepoProvider;
    this.authRepoProvider = authRepoProvider;
  }

  @Override
  public ProfileViewModel get() {
    return newInstance(settingsRepoProvider.get(), historyRepoProvider.get(), favoritesRepoProvider.get(), authRepoProvider.get());
  }

  public static ProfileViewModel_Factory create(Provider<SettingsRepository> settingsRepoProvider,
      Provider<HistoryRepository> historyRepoProvider,
      Provider<FavoritesRepository> favoritesRepoProvider,
      Provider<AuthRepository> authRepoProvider) {
    return new ProfileViewModel_Factory(settingsRepoProvider, historyRepoProvider, favoritesRepoProvider, authRepoProvider);
  }

  public static ProfileViewModel newInstance(SettingsRepository settingsRepo,
      HistoryRepository historyRepo, FavoritesRepository favoritesRepo, AuthRepository authRepo) {
    return new ProfileViewModel(settingsRepo, historyRepo, favoritesRepo, authRepo);
  }
}
