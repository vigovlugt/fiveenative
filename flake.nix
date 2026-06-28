{
  inputs = {
    nixpkgs.url = "github:NixOS/nixpkgs/nixos-unstable";
    flake-utils.url = "github:numtide/flake-utils";
  };

  outputs =
    { nixpkgs, flake-utils, ... }:
    flake-utils.lib.eachDefaultSystem (
      system:
      let
        pkgs = import nixpkgs {
          inherit system;
          config = {
            allowUnfree = true;
            android_sdk.accept_license = true;
          };
        };

        androidSdk =
          (pkgs.androidenv.composeAndroidPackages {
            platformVersions = [ "36" ];
            buildToolsVersions = [ "36.0.0" ];
            includeEmulator = true;
            includeSystemImages = true;
            systemImageTypes = [ "google_apis_playstore" ];
            abiVersions = [ "x86_64" ];
          }).androidsdk;

        androidHome = "${androidSdk}/libexec/android-sdk";

        # Gradle's daemon JVM criteria pins JDK 21. Provide a Nix-patched
        # OpenJDK 21 so Gradle does not auto-download a JDK that cannot run
        # on NixOS (missing libz.so.1, etc.).
        jdk21 = pkgs.jdk21;
      in
      {
        devShells.default = pkgs.mkShell {
          packages = [
            pkgs.jetbrains.jdk
            jdk21
            androidSdk
            pkgs.android-studio
          ];
          JAVA_HOME = pkgs.jetbrains.jdk;
          ANDROID_HOME = androidHome;
          ANDROID_SDK_ROOT = androidHome;
          GRADLE_OPTS = "-Dorg.gradle.project.android.aapt2FromMavenOverride=${androidHome}/build-tools/36.0.0/aapt2 -Dorg.gradle.java.installations.paths=${jdk21.home} -Dorg.gradle.java.installations.auto-download=false";
        };
      }
    );
}
