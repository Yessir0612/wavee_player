package com.vibewave.data.api;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import io.ktor.client.HttpClient;
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
public final class DeezerApi_Factory implements Factory<DeezerApi> {
  private final Provider<HttpClient> clientProvider;

  public DeezerApi_Factory(Provider<HttpClient> clientProvider) {
    this.clientProvider = clientProvider;
  }

  @Override
  public DeezerApi get() {
    return newInstance(clientProvider.get());
  }

  public static DeezerApi_Factory create(Provider<HttpClient> clientProvider) {
    return new DeezerApi_Factory(clientProvider);
  }

  public static DeezerApi newInstance(HttpClient client) {
    return new DeezerApi(client);
  }
}
