# JosmDestinationViewerPlugin

This project depends on: [OsmDestinationViewer](https://github.com/fansanelli/OsmDestinationViewer)

## Cloning the project with submodules

To clone this repository and automatically fetch the required submodule, run:

```sh
git clone --recurse-submodules https://github.com/fansanelli/JosmDestinationViewerPlugin.git
```

If you have already cloned the repository without submodules, initialize and update them with:

```sh
git submodule update --init --recursive
```

## Building

1. Download the file `josm-latest.jar` and place it in the `lib` folder.
2. Make sure you have [Maven](https://maven.apache.org/) installed.
3. Build the project with:

```sh
mvn clean package
```

The resulting jar with all dependencies will be in the `target` folder.

## Installing the plugin in JOSM

Copy the generated jar from `target/` to your JOSM plugins directory, for example:

```sh
cp target/josmdestinationviewer-0.0.1-SNAPSHOT.jar ~/.local/share/JOSM/plugins/
```

---

For any issues or contributions, please refer to the repository or open an issue.

