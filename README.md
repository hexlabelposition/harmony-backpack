# Harmony Backpack

Harmony Backpack is a lightweight backpack mod for Minecraft built with the
[Fabric](https://fabricmc.net/) toolchain.

## Features

- One portable backpack with 27 storage slots (3 rows of 9).
- Backpack contents are stored directly in the item and persist when it is moved.
- Open a backpack by using it or by pressing `B` (configurable in Minecraft's key bindings).
- The keyboard shortcut opens the selected backpack first, then the first backpack in the player
  inventory, and finally the off-hand backpack.
- The open backpack is locked in place until its screen is closed.
- Backpacks cannot be stored inside other backpacks.
- English and Russian translations.

The backpack is currently available in the **Tools & Utilities** creative inventory tab. A crafting
recipe has not been added yet.

## Requirements

- Minecraft `1.21.11`
- Fabric Loader `0.19.3` or newer
- Fabric API
- Java 21 or newer

## Installation

1. Install [Fabric Loader](https://fabricmc.net/use/installer/) for Minecraft 1.21.11.
2. Download and install the matching version of
   [Fabric API](https://modrinth.com/mod/fabric-api).
3. Put the Harmony Backpack `.jar` file into the Minecraft `mods` directory.

The mod must be installed on both the client and the server.

## Development

Clone the repository and import it as a Gradle project in an IDE with a Java 21 JDK configured.
The included Gradle wrapper is the recommended way to run tasks:

```shell
./gradlew runClient
./gradlew runServer
./gradlew build
```

On Windows, use `gradlew.bat` instead of `./gradlew`. Built artifacts are written to `build/libs/`.

Project versions are configured in [`gradle.properties`](gradle.properties). General Fabric setup
guidance is available in the
[Fabric documentation](https://docs.fabricmc.net/develop/getting-started/setting-up-a-development-environment).

## License

Harmony Backpack is licensed under the [MIT License](LICENSE).

## Acknowledgements

This project was originally created from the
[Fabric example mod template](https://github.com/FabricMC/fabric-example-mod), released under CC0.
Thanks to the Fabric project and its contributors for providing the template, documentation, and
development toolchain.
