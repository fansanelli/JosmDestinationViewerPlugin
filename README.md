# JosmDestinationViewerPlugin

This project depends on: [OsmDestinationViewer](https://github.com/fansanelli/OsmDestinationViewer)

## Usage

Once the plugin is installed:
1. Select an object (node or way) from the map that contains a destination=* tag or related.
2. Go to the top menu bar and click on Tools → View destination.
3. A dialog will appear displaying a road sign-style rendering of the destination tag.

> ⚠️ You must select exactly one object with a valid destination tag for the plugin to work.

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
3. **Build and install the OsmDestinationViewer dependency locally:**

   ```sh
   cd external/OsmDestinationViewer
   mvn clean install
   cd ../..
   ```

4. Build the main project with:

   ```sh
   mvn clean package
   ```

The resulting jar with all dependencies will be in the `target` folder.

## Installing the plugin in JOSM

Copy the generated jar from `target/` to your JOSM plugins directory, for example:

```sh
cp target/josmdestinationviewer-0.0.1-SNAPSHOT-jar-with-dependencies.jar ~/.local/share/JOSM/plugins/
```

---

**Disclaimer:** JOSM and OpenStreetMap are trademarks of their respective owners. This project is not affiliated with or endorsed by the JOSM or OpenStreetMap projects.

For any issues or contributions, please refer to the repository or open an issue.

