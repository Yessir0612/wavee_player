package com.vibewave;

import android.app.Activity;
import android.app.Service;
import android.view.View;
import androidx.datastore.core.DataStore;
import androidx.datastore.preferences.core.Preferences;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.firebase.auth.FirebaseAuth;
import com.vibewave.core.di.AuthModule_ProvideFirebaseAuthFactory;
import com.vibewave.core.di.DataModule_ProvideDatabaseFactory;
import com.vibewave.core.di.DataModule_ProvideFavoriteDaoFactory;
import com.vibewave.core.di.DataModule_ProvideHistoryDaoFactory;
import com.vibewave.core.di.DataModule_ProvideSettingsDataStoreFactory;
import com.vibewave.core.di.NetworkModule_ProvideHttpClientFactory;
import com.vibewave.core.di.NetworkModule_ProvideJsonFactory;
import com.vibewave.data.api.DeezerApi;
import com.vibewave.data.db.FavoriteDao;
import com.vibewave.data.db.HistoryDao;
import com.vibewave.data.db.VibeWaveDatabase;
import com.vibewave.data.repository.AuthRepository;
import com.vibewave.data.repository.FavoritesRepository;
import com.vibewave.data.repository.HistoryRepository;
import com.vibewave.data.repository.LocalMediaRepository;
import com.vibewave.data.repository.MusicRepository;
import com.vibewave.data.repository.SettingsRepository;
import com.vibewave.player.PlaybackService;
import com.vibewave.player.PlayerController;
import com.vibewave.ui.components.TrackRowViewModel;
import com.vibewave.ui.components.TrackRowViewModel_HiltModules;
import com.vibewave.ui.screens.auth.AuthViewModel;
import com.vibewave.ui.screens.auth.AuthViewModel_HiltModules;
import com.vibewave.ui.screens.home.HomeViewModel;
import com.vibewave.ui.screens.home.HomeViewModel_HiltModules;
import com.vibewave.ui.screens.library.LibraryViewModel;
import com.vibewave.ui.screens.library.LibraryViewModel_HiltModules;
import com.vibewave.ui.screens.player.PlayerViewModel;
import com.vibewave.ui.screens.player.PlayerViewModel_HiltModules;
import com.vibewave.ui.screens.profile.ProfileViewModel;
import com.vibewave.ui.screens.profile.ProfileViewModel_HiltModules;
import com.vibewave.ui.screens.search.SearchViewModel;
import com.vibewave.ui.screens.search.SearchViewModel_HiltModules;
import dagger.hilt.android.ActivityRetainedLifecycle;
import dagger.hilt.android.ViewModelLifecycle;
import dagger.hilt.android.internal.builders.ActivityComponentBuilder;
import dagger.hilt.android.internal.builders.ActivityRetainedComponentBuilder;
import dagger.hilt.android.internal.builders.FragmentComponentBuilder;
import dagger.hilt.android.internal.builders.ServiceComponentBuilder;
import dagger.hilt.android.internal.builders.ViewComponentBuilder;
import dagger.hilt.android.internal.builders.ViewModelComponentBuilder;
import dagger.hilt.android.internal.builders.ViewWithFragmentComponentBuilder;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories_InternalFactoryFactory_Factory;
import dagger.hilt.android.internal.managers.ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory;
import dagger.hilt.android.internal.managers.SavedStateHandleHolder;
import dagger.hilt.android.internal.modules.ApplicationContextModule;
import dagger.hilt.android.internal.modules.ApplicationContextModule_ProvideContextFactory;
import dagger.internal.DaggerGenerated;
import dagger.internal.DoubleCheck;
import dagger.internal.IdentifierNameString;
import dagger.internal.KeepFieldType;
import dagger.internal.LazyClassKeyMap;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import io.ktor.client.HttpClient;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;
import kotlinx.serialization.json.Json;

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
public final class DaggerVibeWaveApp_HiltComponents_SingletonC {
  private DaggerVibeWaveApp_HiltComponents_SingletonC() {
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {
    private ApplicationContextModule applicationContextModule;

    private Builder() {
    }

    public Builder applicationContextModule(ApplicationContextModule applicationContextModule) {
      this.applicationContextModule = Preconditions.checkNotNull(applicationContextModule);
      return this;
    }

    public VibeWaveApp_HiltComponents.SingletonC build() {
      Preconditions.checkBuilderRequirement(applicationContextModule, ApplicationContextModule.class);
      return new SingletonCImpl(applicationContextModule);
    }
  }

  private static final class ActivityRetainedCBuilder implements VibeWaveApp_HiltComponents.ActivityRetainedC.Builder {
    private final SingletonCImpl singletonCImpl;

    private SavedStateHandleHolder savedStateHandleHolder;

    private ActivityRetainedCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ActivityRetainedCBuilder savedStateHandleHolder(
        SavedStateHandleHolder savedStateHandleHolder) {
      this.savedStateHandleHolder = Preconditions.checkNotNull(savedStateHandleHolder);
      return this;
    }

    @Override
    public VibeWaveApp_HiltComponents.ActivityRetainedC build() {
      Preconditions.checkBuilderRequirement(savedStateHandleHolder, SavedStateHandleHolder.class);
      return new ActivityRetainedCImpl(singletonCImpl, savedStateHandleHolder);
    }
  }

  private static final class ActivityCBuilder implements VibeWaveApp_HiltComponents.ActivityC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private Activity activity;

    private ActivityCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ActivityCBuilder activity(Activity activity) {
      this.activity = Preconditions.checkNotNull(activity);
      return this;
    }

    @Override
    public VibeWaveApp_HiltComponents.ActivityC build() {
      Preconditions.checkBuilderRequirement(activity, Activity.class);
      return new ActivityCImpl(singletonCImpl, activityRetainedCImpl, activity);
    }
  }

  private static final class FragmentCBuilder implements VibeWaveApp_HiltComponents.FragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private Fragment fragment;

    private FragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public FragmentCBuilder fragment(Fragment fragment) {
      this.fragment = Preconditions.checkNotNull(fragment);
      return this;
    }

    @Override
    public VibeWaveApp_HiltComponents.FragmentC build() {
      Preconditions.checkBuilderRequirement(fragment, Fragment.class);
      return new FragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragment);
    }
  }

  private static final class ViewWithFragmentCBuilder implements VibeWaveApp_HiltComponents.ViewWithFragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private View view;

    private ViewWithFragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;
    }

    @Override
    public ViewWithFragmentCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public VibeWaveApp_HiltComponents.ViewWithFragmentC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewWithFragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl, view);
    }
  }

  private static final class ViewCBuilder implements VibeWaveApp_HiltComponents.ViewC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private View view;

    private ViewCBuilder(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public ViewCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public VibeWaveApp_HiltComponents.ViewC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, view);
    }
  }

  private static final class ViewModelCBuilder implements VibeWaveApp_HiltComponents.ViewModelC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private SavedStateHandle savedStateHandle;

    private ViewModelLifecycle viewModelLifecycle;

    private ViewModelCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ViewModelCBuilder savedStateHandle(SavedStateHandle handle) {
      this.savedStateHandle = Preconditions.checkNotNull(handle);
      return this;
    }

    @Override
    public ViewModelCBuilder viewModelLifecycle(ViewModelLifecycle viewModelLifecycle) {
      this.viewModelLifecycle = Preconditions.checkNotNull(viewModelLifecycle);
      return this;
    }

    @Override
    public VibeWaveApp_HiltComponents.ViewModelC build() {
      Preconditions.checkBuilderRequirement(savedStateHandle, SavedStateHandle.class);
      Preconditions.checkBuilderRequirement(viewModelLifecycle, ViewModelLifecycle.class);
      return new ViewModelCImpl(singletonCImpl, activityRetainedCImpl, savedStateHandle, viewModelLifecycle);
    }
  }

  private static final class ServiceCBuilder implements VibeWaveApp_HiltComponents.ServiceC.Builder {
    private final SingletonCImpl singletonCImpl;

    private Service service;

    private ServiceCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ServiceCBuilder service(Service service) {
      this.service = Preconditions.checkNotNull(service);
      return this;
    }

    @Override
    public VibeWaveApp_HiltComponents.ServiceC build() {
      Preconditions.checkBuilderRequirement(service, Service.class);
      return new ServiceCImpl(singletonCImpl, service);
    }
  }

  private static final class ViewWithFragmentCImpl extends VibeWaveApp_HiltComponents.ViewWithFragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private final ViewWithFragmentCImpl viewWithFragmentCImpl = this;

    private ViewWithFragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;


    }
  }

  private static final class FragmentCImpl extends VibeWaveApp_HiltComponents.FragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl = this;

    private FragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        Fragment fragmentParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return activityCImpl.getHiltInternalFactoryFactory();
    }

    @Override
    public ViewWithFragmentComponentBuilder viewWithFragmentComponentBuilder() {
      return new ViewWithFragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl);
    }
  }

  private static final class ViewCImpl extends VibeWaveApp_HiltComponents.ViewC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final ViewCImpl viewCImpl = this;

    private ViewCImpl(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }
  }

  private static final class ActivityCImpl extends VibeWaveApp_HiltComponents.ActivityC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl = this;

    private ActivityCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, Activity activityParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;


    }

    @Override
    public void injectMainActivity(MainActivity mainActivity) {
      injectMainActivity2(mainActivity);
    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return DefaultViewModelFactories_InternalFactoryFactory_Factory.newInstance(getViewModelKeys(), new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl));
    }

    @Override
    public Map<Class<?>, Boolean> getViewModelKeys() {
      return LazyClassKeyMap.<Boolean>of(ImmutableMap.<String, Boolean>builderWithExpectedSize(8).put(LazyClassKeyProvider.com_vibewave_AppRootViewModel, AppRootViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_vibewave_ui_screens_auth_AuthViewModel, AuthViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_vibewave_ui_screens_home_HomeViewModel, HomeViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_vibewave_ui_screens_library_LibraryViewModel, LibraryViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_vibewave_ui_screens_player_PlayerViewModel, PlayerViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_vibewave_ui_screens_profile_ProfileViewModel, ProfileViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_vibewave_ui_screens_search_SearchViewModel, SearchViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_vibewave_ui_components_TrackRowViewModel, TrackRowViewModel_HiltModules.KeyModule.provide()).build());
    }

    @Override
    public ViewModelComponentBuilder getViewModelComponentBuilder() {
      return new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public FragmentComponentBuilder fragmentComponentBuilder() {
      return new FragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }

    @Override
    public ViewComponentBuilder viewComponentBuilder() {
      return new ViewCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }

    @CanIgnoreReturnValue
    private MainActivity injectMainActivity2(MainActivity instance) {
      MainActivity_MembersInjector.injectPlayer(instance, singletonCImpl.playerControllerProvider.get());
      return instance;
    }

    @IdentifierNameString
    private static final class LazyClassKeyProvider {
      static String com_vibewave_ui_screens_player_PlayerViewModel = "com.vibewave.ui.screens.player.PlayerViewModel";

      static String com_vibewave_ui_screens_profile_ProfileViewModel = "com.vibewave.ui.screens.profile.ProfileViewModel";

      static String com_vibewave_ui_screens_library_LibraryViewModel = "com.vibewave.ui.screens.library.LibraryViewModel";

      static String com_vibewave_ui_screens_auth_AuthViewModel = "com.vibewave.ui.screens.auth.AuthViewModel";

      static String com_vibewave_ui_screens_home_HomeViewModel = "com.vibewave.ui.screens.home.HomeViewModel";

      static String com_vibewave_AppRootViewModel = "com.vibewave.AppRootViewModel";

      static String com_vibewave_ui_screens_search_SearchViewModel = "com.vibewave.ui.screens.search.SearchViewModel";

      static String com_vibewave_ui_components_TrackRowViewModel = "com.vibewave.ui.components.TrackRowViewModel";

      @KeepFieldType
      PlayerViewModel com_vibewave_ui_screens_player_PlayerViewModel2;

      @KeepFieldType
      ProfileViewModel com_vibewave_ui_screens_profile_ProfileViewModel2;

      @KeepFieldType
      LibraryViewModel com_vibewave_ui_screens_library_LibraryViewModel2;

      @KeepFieldType
      AuthViewModel com_vibewave_ui_screens_auth_AuthViewModel2;

      @KeepFieldType
      HomeViewModel com_vibewave_ui_screens_home_HomeViewModel2;

      @KeepFieldType
      AppRootViewModel com_vibewave_AppRootViewModel2;

      @KeepFieldType
      SearchViewModel com_vibewave_ui_screens_search_SearchViewModel2;

      @KeepFieldType
      TrackRowViewModel com_vibewave_ui_components_TrackRowViewModel2;
    }
  }

  private static final class ViewModelCImpl extends VibeWaveApp_HiltComponents.ViewModelC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ViewModelCImpl viewModelCImpl = this;

    private Provider<AppRootViewModel> appRootViewModelProvider;

    private Provider<AuthViewModel> authViewModelProvider;

    private Provider<HomeViewModel> homeViewModelProvider;

    private Provider<LibraryViewModel> libraryViewModelProvider;

    private Provider<PlayerViewModel> playerViewModelProvider;

    private Provider<ProfileViewModel> profileViewModelProvider;

    private Provider<SearchViewModel> searchViewModelProvider;

    private Provider<TrackRowViewModel> trackRowViewModelProvider;

    private ViewModelCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, SavedStateHandle savedStateHandleParam,
        ViewModelLifecycle viewModelLifecycleParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;

      initialize(savedStateHandleParam, viewModelLifecycleParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandle savedStateHandleParam,
        final ViewModelLifecycle viewModelLifecycleParam) {
      this.appRootViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 0);
      this.authViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 1);
      this.homeViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 2);
      this.libraryViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 3);
      this.playerViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 4);
      this.profileViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 5);
      this.searchViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 6);
      this.trackRowViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 7);
    }

    @Override
    public Map<Class<?>, javax.inject.Provider<ViewModel>> getHiltViewModelMap() {
      return LazyClassKeyMap.<javax.inject.Provider<ViewModel>>of(ImmutableMap.<String, javax.inject.Provider<ViewModel>>builderWithExpectedSize(8).put(LazyClassKeyProvider.com_vibewave_AppRootViewModel, ((Provider) appRootViewModelProvider)).put(LazyClassKeyProvider.com_vibewave_ui_screens_auth_AuthViewModel, ((Provider) authViewModelProvider)).put(LazyClassKeyProvider.com_vibewave_ui_screens_home_HomeViewModel, ((Provider) homeViewModelProvider)).put(LazyClassKeyProvider.com_vibewave_ui_screens_library_LibraryViewModel, ((Provider) libraryViewModelProvider)).put(LazyClassKeyProvider.com_vibewave_ui_screens_player_PlayerViewModel, ((Provider) playerViewModelProvider)).put(LazyClassKeyProvider.com_vibewave_ui_screens_profile_ProfileViewModel, ((Provider) profileViewModelProvider)).put(LazyClassKeyProvider.com_vibewave_ui_screens_search_SearchViewModel, ((Provider) searchViewModelProvider)).put(LazyClassKeyProvider.com_vibewave_ui_components_TrackRowViewModel, ((Provider) trackRowViewModelProvider)).build());
    }

    @Override
    public Map<Class<?>, Object> getHiltViewModelAssistedMap() {
      return ImmutableMap.<Class<?>, Object>of();
    }

    @IdentifierNameString
    private static final class LazyClassKeyProvider {
      static String com_vibewave_ui_screens_search_SearchViewModel = "com.vibewave.ui.screens.search.SearchViewModel";

      static String com_vibewave_ui_screens_home_HomeViewModel = "com.vibewave.ui.screens.home.HomeViewModel";

      static String com_vibewave_ui_screens_player_PlayerViewModel = "com.vibewave.ui.screens.player.PlayerViewModel";

      static String com_vibewave_ui_components_TrackRowViewModel = "com.vibewave.ui.components.TrackRowViewModel";

      static String com_vibewave_ui_screens_auth_AuthViewModel = "com.vibewave.ui.screens.auth.AuthViewModel";

      static String com_vibewave_AppRootViewModel = "com.vibewave.AppRootViewModel";

      static String com_vibewave_ui_screens_library_LibraryViewModel = "com.vibewave.ui.screens.library.LibraryViewModel";

      static String com_vibewave_ui_screens_profile_ProfileViewModel = "com.vibewave.ui.screens.profile.ProfileViewModel";

      @KeepFieldType
      SearchViewModel com_vibewave_ui_screens_search_SearchViewModel2;

      @KeepFieldType
      HomeViewModel com_vibewave_ui_screens_home_HomeViewModel2;

      @KeepFieldType
      PlayerViewModel com_vibewave_ui_screens_player_PlayerViewModel2;

      @KeepFieldType
      TrackRowViewModel com_vibewave_ui_components_TrackRowViewModel2;

      @KeepFieldType
      AuthViewModel com_vibewave_ui_screens_auth_AuthViewModel2;

      @KeepFieldType
      AppRootViewModel com_vibewave_AppRootViewModel2;

      @KeepFieldType
      LibraryViewModel com_vibewave_ui_screens_library_LibraryViewModel2;

      @KeepFieldType
      ProfileViewModel com_vibewave_ui_screens_profile_ProfileViewModel2;
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final ViewModelCImpl viewModelCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          ViewModelCImpl viewModelCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.viewModelCImpl = viewModelCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // com.vibewave.AppRootViewModel 
          return (T) new AppRootViewModel(singletonCImpl.settingsRepositoryProvider.get());

          case 1: // com.vibewave.ui.screens.auth.AuthViewModel 
          return (T) new AuthViewModel(singletonCImpl.authRepositoryProvider.get());

          case 2: // com.vibewave.ui.screens.home.HomeViewModel 
          return (T) new HomeViewModel(singletonCImpl.musicRepositoryProvider.get(), singletonCImpl.historyRepositoryProvider.get());

          case 3: // com.vibewave.ui.screens.library.LibraryViewModel 
          return (T) new LibraryViewModel(singletonCImpl.localMediaRepositoryProvider.get());

          case 4: // com.vibewave.ui.screens.player.PlayerViewModel 
          return (T) new PlayerViewModel(singletonCImpl.playerControllerProvider.get(), singletonCImpl.favoritesRepositoryProvider.get());

          case 5: // com.vibewave.ui.screens.profile.ProfileViewModel 
          return (T) new ProfileViewModel(singletonCImpl.settingsRepositoryProvider.get(), singletonCImpl.historyRepositoryProvider.get(), singletonCImpl.favoritesRepositoryProvider.get(), singletonCImpl.authRepositoryProvider.get());

          case 6: // com.vibewave.ui.screens.search.SearchViewModel 
          return (T) new SearchViewModel(singletonCImpl.musicRepositoryProvider.get());

          case 7: // com.vibewave.ui.components.TrackRowViewModel 
          return (T) new TrackRowViewModel(singletonCImpl.favoritesRepositoryProvider.get());

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ActivityRetainedCImpl extends VibeWaveApp_HiltComponents.ActivityRetainedC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl = this;

    private Provider<ActivityRetainedLifecycle> provideActivityRetainedLifecycleProvider;

    private ActivityRetainedCImpl(SingletonCImpl singletonCImpl,
        SavedStateHandleHolder savedStateHandleHolderParam) {
      this.singletonCImpl = singletonCImpl;

      initialize(savedStateHandleHolderParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandleHolder savedStateHandleHolderParam) {
      this.provideActivityRetainedLifecycleProvider = DoubleCheck.provider(new SwitchingProvider<ActivityRetainedLifecycle>(singletonCImpl, activityRetainedCImpl, 0));
    }

    @Override
    public ActivityComponentBuilder activityComponentBuilder() {
      return new ActivityCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public ActivityRetainedLifecycle getActivityRetainedLifecycle() {
      return provideActivityRetainedLifecycleProvider.get();
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // dagger.hilt.android.ActivityRetainedLifecycle 
          return (T) ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory.provideActivityRetainedLifecycle();

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ServiceCImpl extends VibeWaveApp_HiltComponents.ServiceC {
    private final SingletonCImpl singletonCImpl;

    private final ServiceCImpl serviceCImpl = this;

    private ServiceCImpl(SingletonCImpl singletonCImpl, Service serviceParam) {
      this.singletonCImpl = singletonCImpl;


    }

    @Override
    public void injectPlaybackService(PlaybackService playbackService) {
    }
  }

  private static final class SingletonCImpl extends VibeWaveApp_HiltComponents.SingletonC {
    private final ApplicationContextModule applicationContextModule;

    private final SingletonCImpl singletonCImpl = this;

    private Provider<VibeWaveDatabase> provideDatabaseProvider;

    private Provider<HistoryRepository> historyRepositoryProvider;

    private Provider<PlayerController> playerControllerProvider;

    private Provider<DataStore<Preferences>> provideSettingsDataStoreProvider;

    private Provider<SettingsRepository> settingsRepositoryProvider;

    private Provider<FirebaseAuth> provideFirebaseAuthProvider;

    private Provider<AuthRepository> authRepositoryProvider;

    private Provider<Json> provideJsonProvider;

    private Provider<HttpClient> provideHttpClientProvider;

    private Provider<DeezerApi> deezerApiProvider;

    private Provider<MusicRepository> musicRepositoryProvider;

    private Provider<LocalMediaRepository> localMediaRepositoryProvider;

    private Provider<FavoritesRepository> favoritesRepositoryProvider;

    private SingletonCImpl(ApplicationContextModule applicationContextModuleParam) {
      this.applicationContextModule = applicationContextModuleParam;
      initialize(applicationContextModuleParam);

    }

    private HistoryDao historyDao() {
      return DataModule_ProvideHistoryDaoFactory.provideHistoryDao(provideDatabaseProvider.get());
    }

    private FavoriteDao favoriteDao() {
      return DataModule_ProvideFavoriteDaoFactory.provideFavoriteDao(provideDatabaseProvider.get());
    }

    @SuppressWarnings("unchecked")
    private void initialize(final ApplicationContextModule applicationContextModuleParam) {
      this.provideDatabaseProvider = DoubleCheck.provider(new SwitchingProvider<VibeWaveDatabase>(singletonCImpl, 2));
      this.historyRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<HistoryRepository>(singletonCImpl, 1));
      this.playerControllerProvider = DoubleCheck.provider(new SwitchingProvider<PlayerController>(singletonCImpl, 0));
      this.provideSettingsDataStoreProvider = DoubleCheck.provider(new SwitchingProvider<DataStore<Preferences>>(singletonCImpl, 4));
      this.settingsRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<SettingsRepository>(singletonCImpl, 3));
      this.provideFirebaseAuthProvider = DoubleCheck.provider(new SwitchingProvider<FirebaseAuth>(singletonCImpl, 6));
      this.authRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<AuthRepository>(singletonCImpl, 5));
      this.provideJsonProvider = DoubleCheck.provider(new SwitchingProvider<Json>(singletonCImpl, 10));
      this.provideHttpClientProvider = DoubleCheck.provider(new SwitchingProvider<HttpClient>(singletonCImpl, 9));
      this.deezerApiProvider = DoubleCheck.provider(new SwitchingProvider<DeezerApi>(singletonCImpl, 8));
      this.musicRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<MusicRepository>(singletonCImpl, 7));
      this.localMediaRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<LocalMediaRepository>(singletonCImpl, 11));
      this.favoritesRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<FavoritesRepository>(singletonCImpl, 12));
    }

    @Override
    public void injectVibeWaveApp(VibeWaveApp vibeWaveApp) {
    }

    @Override
    public Set<Boolean> getDisableFragmentGetContextFix() {
      return ImmutableSet.<Boolean>of();
    }

    @Override
    public ActivityRetainedComponentBuilder retainedComponentBuilder() {
      return new ActivityRetainedCBuilder(singletonCImpl);
    }

    @Override
    public ServiceComponentBuilder serviceComponentBuilder() {
      return new ServiceCBuilder(singletonCImpl);
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // com.vibewave.player.PlayerController 
          return (T) new PlayerController(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule), singletonCImpl.historyRepositoryProvider.get());

          case 1: // com.vibewave.data.repository.HistoryRepository 
          return (T) new HistoryRepository(singletonCImpl.historyDao());

          case 2: // com.vibewave.data.db.VibeWaveDatabase 
          return (T) DataModule_ProvideDatabaseFactory.provideDatabase(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 3: // com.vibewave.data.repository.SettingsRepository 
          return (T) new SettingsRepository(singletonCImpl.provideSettingsDataStoreProvider.get());

          case 4: // androidx.datastore.core.DataStore<androidx.datastore.preferences.core.Preferences> 
          return (T) DataModule_ProvideSettingsDataStoreFactory.provideSettingsDataStore(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 5: // com.vibewave.data.repository.AuthRepository 
          return (T) new AuthRepository(singletonCImpl.provideFirebaseAuthProvider.get());

          case 6: // com.google.firebase.auth.FirebaseAuth 
          return (T) AuthModule_ProvideFirebaseAuthFactory.provideFirebaseAuth();

          case 7: // com.vibewave.data.repository.MusicRepository 
          return (T) new MusicRepository(singletonCImpl.deezerApiProvider.get());

          case 8: // com.vibewave.data.api.DeezerApi 
          return (T) new DeezerApi(singletonCImpl.provideHttpClientProvider.get());

          case 9: // io.ktor.client.HttpClient 
          return (T) NetworkModule_ProvideHttpClientFactory.provideHttpClient(singletonCImpl.provideJsonProvider.get());

          case 10: // kotlinx.serialization.json.Json 
          return (T) NetworkModule_ProvideJsonFactory.provideJson();

          case 11: // com.vibewave.data.repository.LocalMediaRepository 
          return (T) new LocalMediaRepository(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 12: // com.vibewave.data.repository.FavoritesRepository 
          return (T) new FavoritesRepository(singletonCImpl.favoriteDao());

          default: throw new AssertionError(id);
        }
      }
    }
  }
}
