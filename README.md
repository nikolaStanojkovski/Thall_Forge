# ThallForge - Streaming & Discovery Platform

### **Overview**
**ThallForge** is a streaming platform dedicated to discovering, streaming, and promoting music. It offers curated music experiences, band recommendations, and access to exclusive content.

## Modules

The main parts of the project template are:

- **[all]** - A single content package that embeds all of the compiled modules (bundles and content packages) including any vendor dependencies.
- **[core]** - Java bundle containing core functionality like OSGi services, listeners, or schedulers, as well as component-related Java code such as servlets or request filters.
- **[ui.apps]** - Contains the `/apps` (and `/etc`) parts of the project, including JS & CSS clientlibs, components, and templates.
- **[ui.config]** - Contains runmode-specific OSGi configurations for the project.
- **[ui.content]** - Contains sample content using the components from `ui.apps`.
- **[ui.frontend]** - An optional dedicated front-end build mechanism (General Webpack project).

## How to Build

To build all the modules, run the following command in the project root directory with Maven 3:

```bash
mvn clean install
```

To build all the modules and deploy the `all` package to a local instance of AEM, run in the project root directory the following command:

```bash
mvn clean install -PautoInstallSinglePackage
```

Or to deploy it to a publish instance, run:

```bash
mvn clean install -PautoInstallSinglePackagePublish
```

Alternatively:

```bash
mvn clean install -PautoInstallSinglePackage -Daem.port=4503
```

To deploy only the bundle to the author, run:

```bash
mvn clean install -PautoInstallBundle
```

To deploy only a single content package, run in the sub-module directory (i.e `ui.apps`):

```bash
mvn clean install -PautoInstallPackage
```

## Requirements

### Core Functionalities

- **Music Streaming:** Stream high-quality music tracks.
- **Player Component:** Basic functionality including play, pause, skip, volume control, and shuffle.
- **Content Discovery:** Genre-based navigation and trending albums to help users discover music.

### Custom Components
1. **Music Player** - Configurable audio playback with repeat and shuffle features.
2. **Album Tracklist** - Displays tracks for a specified album with download links.
3. **Genre Explorer** - Displays filtered albums based on selected genres.
4. **Artist Spotlight** - Shows albums and songs for a specified artist.
5. **Latest Releases** - Carousel of the latest album releases based on author-defined limits.
6. **Artist Profiles** - Accordion items with nested components for artist information.
7. **Footer** - Configurable links and logo for the page footer.
8. **Header** - Configurable links and logo for the page header.
9. **Top-Fans Banner** - Displays a message to the top user based on interaction counts.
10. **XF Header** - Imports header from page properties.
11. **XF Footer** - Imports footer from page properties.

### Core Components (Adobe Imported)
1. **Text**
2. **Teaser**
3. **Image**
4. **Breadcrumb**
5. **List**
6. **Experience Fragment**
7. **Language Navigation**
8. **Title**
9. **Download**
10. **Sharing**

## Feature Summary

1. **Structure Components**
2. **Content Components**
3. **Content Fragments**
4. **Custom Templates**
5. **Experience Fragments**
6. **Endpoints/Servlets**
7. **Schedulers**
8. **Filter Implementation**
9. **Custom Clientlibs/Validators**
10. **Custom Users**

### **Schedulers & Services**

- **Artist Data Source Update (Scheduler):**
  - **Functionality:** Updates the artist data source JSON file every 12 hours.
  - **Source:** Data is updated from a configured JSON file.

### **Endpoints & APIs**

- **Music-Player Shuffle API:**
  - **Endpoint:** `${resource-path}._jcr_content.shuffle.json`
  - **Functionality:** Fetches details about a randomly picked tracked from the selected album path.
  - **Format:** JSON response with an object containing album data.

## **Deployment**

Deploy on **AEM Cloud Service** using **Maven** and **Cloud Manager**.
