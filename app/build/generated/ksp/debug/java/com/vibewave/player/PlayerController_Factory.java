package com.vibewave.player;

import android.content.Context;
import com.vibewave.data.repository.HistoryRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
public final class PlayerController_Factory implements Factory<PlayerController> {
  private final Provider<Context> contextProvider;

  private final Provider<HistoryRepository> historyRepoProvider;

  public PlayerController_Factory(Provider<Context> contextProvider,
      Provider<HistoryRepository> historyRepoProvider) {
    this.contextProvider = contextProvider;
    this.historyRepoProvider = historyRepoProvider;
  }

  @Override
  public PlayerController get() {
    return newInstance(contextProvider.get(), historyRepoProvider.get());
  }

  public static PlayerController_Factory create(Provider<Context> contextProvider,
      Provider<HistoryRepository> historyRepoProvider) {
    return new PlayerController_Factory(contextProvider, historyRepoProvider);
  }

  public static PlayerController newInstance(Context context, HistoryRepository historyRepo) {
    return new PlayerController(context, historyRepo);
  }
}
