package com.vibewave;

import com.vibewave.player.PlayerController;
import dagger.MembersInjector;
import dagger.internal.DaggerGenerated;
import dagger.internal.InjectedFieldSignature;
import dagger.internal.QualifierMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class MainActivity_MembersInjector implements MembersInjector<MainActivity> {
  private final Provider<PlayerController> playerProvider;

  public MainActivity_MembersInjector(Provider<PlayerController> playerProvider) {
    this.playerProvider = playerProvider;
  }

  public static MembersInjector<MainActivity> create(Provider<PlayerController> playerProvider) {
    return new MainActivity_MembersInjector(playerProvider);
  }

  @Override
  public void injectMembers(MainActivity instance) {
    injectPlayer(instance, playerProvider.get());
  }

  @InjectedFieldSignature("com.vibewave.MainActivity.player")
  public static void injectPlayer(MainActivity instance, PlayerController player) {
    instance.player = player;
  }
}
