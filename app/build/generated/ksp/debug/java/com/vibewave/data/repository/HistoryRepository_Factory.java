package com.vibewave.data.repository;

import com.vibewave.data.db.HistoryDao;
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
public final class HistoryRepository_Factory implements Factory<HistoryRepository> {
  private final Provider<HistoryDao> daoProvider;

  public HistoryRepository_Factory(Provider<HistoryDao> daoProvider) {
    this.daoProvider = daoProvider;
  }

  @Override
  public HistoryRepository get() {
    return newInstance(daoProvider.get());
  }

  public static HistoryRepository_Factory create(Provider<HistoryDao> daoProvider) {
    return new HistoryRepository_Factory(daoProvider);
  }

  public static HistoryRepository newInstance(HistoryDao dao) {
    return new HistoryRepository(dao);
  }
}
