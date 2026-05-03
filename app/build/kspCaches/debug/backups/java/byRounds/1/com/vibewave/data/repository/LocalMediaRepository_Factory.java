package com.vibewave.data.repository;

import android.content.Context;
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
public final class LocalMediaRepository_Factory implements Factory<LocalMediaRepository> {
  private final Provider<Context> contextProvider;

  public LocalMediaRepository_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public LocalMediaRepository get() {
    return newInstance(contextProvider.get());
  }

  public static LocalMediaRepository_Factory create(Provider<Context> contextProvider) {
    return new LocalMediaRepository_Factory(contextProvider);
  }

  public static LocalMediaRepository newInstance(Context context) {
    return new LocalMediaRepository(context);
  }
}
