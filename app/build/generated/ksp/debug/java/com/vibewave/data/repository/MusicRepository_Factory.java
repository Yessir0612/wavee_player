package com.vibewave.data.repository;

import com.vibewave.data.api.DeezerApi;
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
public final class MusicRepository_Factory implements Factory<MusicRepository> {
  private final Provider<DeezerApi> apiProvider;

  public MusicRepository_Factory(Provider<DeezerApi> apiProvider) {
    this.apiProvider = apiProvider;
  }

  @Override
  public MusicRepository get() {
    return newInstance(apiProvider.get());
  }

  public static MusicRepository_Factory create(Provider<DeezerApi> apiProvider) {
    return new MusicRepository_Factory(apiProvider);
  }

  public static MusicRepository newInstance(DeezerApi api) {
    return new MusicRepository(api);
  }
}
