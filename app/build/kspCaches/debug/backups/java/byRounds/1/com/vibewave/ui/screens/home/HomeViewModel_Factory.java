package com.vibewave.ui.screens.home;

import com.vibewave.data.repository.HistoryRepository;
import com.vibewave.data.repository.MusicRepository;
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
public final class HomeViewModel_Factory implements Factory<HomeViewModel> {
  private final Provider<MusicRepository> musicRepoProvider;

  private final Provider<HistoryRepository> historyRepoProvider;

  public HomeViewModel_Factory(Provider<MusicRepository> musicRepoProvider,
      Provider<HistoryRepository> historyRepoProvider) {
    this.musicRepoProvider = musicRepoProvider;
    this.historyRepoProvider = historyRepoProvider;
  }

  @Override
  public HomeViewModel get() {
    return newInstance(musicRepoProvider.get(), historyRepoProvider.get());
  }

  public static HomeViewModel_Factory create(Provider<MusicRepository> musicRepoProvider,
      Provider<HistoryRepository> historyRepoProvider) {
    return new HomeViewModel_Factory(musicRepoProvider, historyRepoProvider);
  }

  public static HomeViewModel newInstance(MusicRepository musicRepo,
      HistoryRepository historyRepo) {
    return new HomeViewModel(musicRepo, historyRepo);
  }
}
