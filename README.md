# ThallForge - Progressive Metal Streaming & Discovery Platform

### **Overview**
**ThallForge** is a Progressive Metal streaming platform dedicated to discovering, streaming, and promoting underground and well-known progressive metal bands, including subgenres like **djent, thall**, and **alt-metal**. It offers curated music experiences, band recommendations, and access to exclusive content.

## Modules

The main parts of the project template are:

- **[all]** - A single content package that embeds all of the compiled modules (bundles and content packages) including any vendor dependencies
- **[core]** - Java bundle containing core functionality like OSGi services, listeners, or schedulers, as well as component-related Java code such as servlets or request filters.
- **[ui.apps]** - Contains the `/apps` (and `/etc`) parts of the project, including JS & CSS clientlibs, components, and templates
- **[ui.config]** - Contains runmode-specific OSGi configurations for the project
- **[ui.content]** - Contains sample content using the components from `ui.apps`
- **[ui.frontend]** - An optional dedicated front-end build mechanism (General Webpack project)

## How to Build

To build all the modules, run the following command in the project root directory with Maven 3:

```bash
mvn clean install
```

To build all the modules and deploy the `all` package to a local instance of AEM, run in the project root directory the following command:

```bash
mvn clean install -PautoInstallSinglePackage
```

Or to deploy it to a publish instance, run

```bash
mvn clean install -PautoInstallSinglePackagePublish
```

Or alternatively

```bash
mvn clean install -PautoInstallSinglePackage -Daem.port=4503
```

Or to deploy only the bundle to the author, run

```bash
mvn clean install -PautoInstallBundle
```

Or to deploy only a single content package, run in the sub-module directory (i.e `ui.apps`)

```bash
mvn clean install -PautoInstallPackage
```

## Requirements

### Core Functionalities

- **Music Streaming:** Stream high-quality progressive metal tracks.
- **Player Component:** Basic functionality including play, pause, skip, volume control, and shuffle.
- **Playlist Creation:** Users can create custom playlists and share them.
- **Content Discovery:** Genre-based navigation and trending playlists to help users discover music.

#### Custom Components
1. **Music Player**
2. **Album Tracklist**
3. **Genre Explorer**
4. **Artist Spotlight**
5. **Latest Releases**
6. **Album Manager**
7. **Thall Carousel** (Albums, Artists, Songs)

#### Core Components (Adobe Imported)
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

### Additional Functionalities

1. **Structure Components**
2. **Content Components**
3. **Content Fragments**
4. **Custom Templates**
5. **Experience Fragments**
6. **Endpoints/Servlets**
7. **Schedulers/Data Importing** (Spotify API)
8. **Translation Labels**
9. **Filter Implementation**
10. **Custom Clientlibs/Validators**
11. **Custom Users**

## **3. Schedulers & Services**

- **Trending Tracks Calculation (Scheduler):**
    - **Functionality:** A scheduler that runs every 24 hours to calculate the most played songs.
    - **Algorithm:** Uses play counts, likes, and shares as input to calculate trends.
    - **OSGi Service:** Implement a custom OSGi service to handle trend calculation and data persistence.

- **Expired Content Cleanup (Scheduler):**
    - **Functionality:** Cleans up expired or outdated content (e.g., live streams, old band events).
    - **Execution Time:** Runs weekly to keep the content library up-to-date.

## **4. Endpoints & APIs**

- **Band API:**
    - **Endpoint:** `/api/bands/{bandId}`
    - **Functionality:** Retrieves band information, discography, and upcoming events.
    - **Format:** JSON response with band data.
    - **Caching:** Implement caching to optimize response times.

- **Playlist API:**
    - **Endpoint:** `/api/playlists/{userId}`
    - **Functionality:** Fetches all playlists created by the user.
    - **Methods:** `GET` (retrieve playlists), `POST` (create playlist), `DELETE` (remove playlist).

## **5. Testing & Quality Assurance**

- **Unit Testing (AEM Mocks):** Use **AEM Mocks** to test Sling Models, components, and services locally without needing a full AEM instance.
- **End-to-End Testing:** Use tools like **Selenium** or **Cypress** to run end-to-end tests on the platform’s UI.

## **6. Deployment**

Deploy on **AEM Cloud Service** using **Maven** and **Cloud Manager**.
