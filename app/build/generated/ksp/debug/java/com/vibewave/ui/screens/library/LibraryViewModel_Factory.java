package com.vibewave.ui.screens.library;

import com.vibewave.data.repository.LocalMediaRepository;
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
public final class LibraryViewModel_Factory implements Factory<LibraryViewModel> {
  private final Provider<LocalMediaRepository> localMediaRepoProvider;

  public LibraryViewModel_Factory(Provider<LocalMediaRepository> localMediaRepoProvider) {
    this.localMediaRepoProvider = localMediaRepoProvider;
  }

  @Override
  public LibraryViewModel get() {
    return newInstance(localMediaRepoProvider.get());
  }

  public static LibraryViewModel_Factory create(
      Provider<LocalMediaRepository> localMediaRepoProvider) {
    return new LibraryViewModel_Factory(localMediaRepoProvider);
  }

  public static LibraryViewModel newInstance(LocalMediaRepository localMediaRepo) {
    return new LibraryViewModel(localMediaRepo);
  }
}
