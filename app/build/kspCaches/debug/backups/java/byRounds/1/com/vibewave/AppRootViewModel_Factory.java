package com.vibewave;

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
public final class AppRootViewModel_Factory implements Factory<AppRootViewModel> {
  private final Provider<SettingsRepository> settingsRepoProvider;

  public AppRootViewModel_Factory(Provider<SettingsRepository> settingsRepoProvider) {
    this.settingsRepoProvider = settingsRepoProvider;
  }

  @Override
  public AppRootViewModel get() {
    return newInstance(settingsRepoProvider.get());
  }

  public static AppRootViewModel_Factory create(Provider<SettingsRepository> settingsRepoProvider) {
    return new AppRootViewModel_Factory(settingsRepoProvider);
  }

  public static AppRootViewModel newInstance(SettingsRepository settingsRepo) {
    return new AppRootViewModel(settingsRepo);
  }
}
