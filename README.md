# ThallForge - Progressive Metal Streaming & Discovery Platform

### **Overview:**
**ThallForge** is a Progressive Metal streaming platform dedicated to discovering, streaming, and promoting underground and well-known progressive metal bands, including subgenres like **djent, thall**, and **alt-metal**. It provides personalized playlists, band recommendations, and access to exclusive content such as live sessions, behind-the-scenes footage, and music tutorials.

### **Requirements:**

#### **1. Core Functionalities:**

**1.1 User Registration & Authentication:**
- **User Sign-Up & Login** (OAuth with social logins like Google, Facebook, or custom credentials).
- **User Profiles**: Users can create profiles, follow bands, create playlists, and interact with other users.
- **Authentication Service**: Custom OSGi service for handling user authentication via JWT (JSON Web Token).

**1.2 Music Streaming:**
- **Stream Songs**: High-quality progressive metal tracks streamed directly from the platform.
- **Player Component**: Develop a player with basic functionality (play, pause, skip, volume control, shuffle).
- **Playlist Creation**: Users can create custom playlists, share with friends, or set them to public.
- **Song Recommendation**: Based on user’s listening history, similar bands, or subgenres will be recommended.

**1.3 Content Discovery:**
- **Band Pages**: Create profile pages for bands where users can view their discography, bio, upcoming shows, etc.
- **Genre/Tag-Based Navigation**: Users can explore content based on progressive metal subgenres or moods (thall, djent, alt-metal, etc.).
- **Trending Tracks & Playlists**: Show trending music and playlists across the platform based on user engagement.

**1.4 Exclusive Content:**
- **Live Sessions**: Users can view live concert streams or jam sessions from progressive metal bands.
- **Tutorials**: Access exclusive content like behind-the-scenes footage and guitar or production tutorials from bands.

#### **2. Components to Implement:**

**2.1 Music Player Component (Complex):**
- **Functionality**: Plays music, with options for play, pause, volume control, skipping, and seeking.
- **Progress Bar**: Visually indicate song progress, time elapsed, and time remaining.
- **Adaptive Streaming**: Implement bitrate switching based on user bandwidth.

**2.2 Band Profile Component:**
- **Content**: Display information about the band, discography, biography, and upcoming events.
- **Dynamic Rendering**: Use Sling Models to fetch data dynamically from the back-end database.

**2.3 Playlist Component:**
- **User Playlists**: Allow users to create, edit, delete, and share their custom playlists.
- **Public/Private Toggle**: Users can choose to make playlists public or private.

**2.4 Genre Explorer Component:**
- **Filter Options**: Users can filter and explore music based on subgenres like djent, thall, progressive, and alt-metal.
- **AJAX Search**: Dynamic searching of tracks, albums, and bands via genre tags.

#### **3. Schedulers & Services:**

**3.1 Trending Tracks Calculation (Scheduler):**
- **Functionality**: A scheduler that runs every 24 hours to calculate the most played songs across the platform.
- **Algorithm**: Uses play counts, likes, and shares as input to calculate trends.
- **OSGi Service**: Implement a custom OSGi service to handle trend calculation and data persistence.

**3.2 Personalized Playlist Generator (Scheduler):**
- **Functionality**: Generates a daily personalized playlist for each user based on listening habits.
- **Execution Time**: Runs every night at 2 AM.
- **Service Integration**: Uses a recommendation engine, possibly with machine learning, to generate playlists.

**3.3 Expired Content Cleanup (Scheduler):**
- **Functionality**: Cleans up expired or outdated content (e.g., live streams, old band events).
- **Execution Time**: Runs weekly to ensure the content library stays up-to-date.

#### **4. Endpoints & APIs:**

**4.1 Band API:**
- **Endpoint**: `/api/bands/{bandId}`
- **Functionality**: Retrieves band information, discography, and upcoming events.
- **Format**: JSON response containing band data.
- **Caching**: Implement caching to optimize response times for frequently requested bands.

**4.2 Playlist API:**
- **Endpoint**: `/api/playlists/{userId}`
- **Functionality**: Fetches all playlists created by the user.
- **Methods**: GET (retrieve playlists), POST (create playlist), DELETE (remove playlist).

**4.3 Recommendation API:**
- **Endpoint**: `/api/recommendations/{userId}`
- **Functionality**: Returns personalized recommendations for users based on their listening history.
- **Format**: JSON response with track and band suggestions.

#### **5. Advanced Features:**

**5.1 Integration with External Music Platforms (Optional):**
- **Functionality**: Use external APIs (e.g., Spotify, Last.fm) to allow users to import their playlists and follow their favorite bands.
- **APIs**: Build integration modules that use OAuth and third-party APIs to fetch user data from external platforms.

**5.2 Social Sharing (Optional):**
- **Functionality**: Users can share tracks, playlists, and band profiles on social platforms like Facebook or Twitter.
- **Integration**: Integrate with social APIs to facilitate seamless sharing.

**5.3 Live Streaming (Optional):**
- **Functionality**: Live-stream exclusive concerts or sessions directly from the platform.
- **Technical Stack**: Use WebRTC or a similar streaming technology to broadcast and stream content.

#### **6. Testing & Quality Assurance:**

**6.1 Unit Testing (AEM Mocks):**
- Use **AEM Mocks** to test Sling Models, components, and services locally without needing a full AEM instance.

**6.2 End-to-End Testing:**
- Use tools like **Selenium** or **Cypress** to run end-to-end tests on the platform’s UI, including the player and content explorer components.

#### **7. Deployment:**

- Deploy on **AEM Cloud Service** or an **AEM on-premise instance** depending on the project needs.
- Automate deployments using **Maven**, **Jenkins**, or **Cloud Manager**.


## Modules

The main parts of the template are:

* **[all]** - A single content package that embeds all of the compiled modules (bundles and content packages) including any vendor dependencies
* **[core]** - Java bundle containing all core functionality like OSGi services, listeners or schedulers, as well as component-related Java code such as servlets or request filters.
* **[ui.apps]** - Contains the /apps (and /etc) parts of the project, ie JS&CSS clientlibs, components, and templates
* **[ui.config]** - Contains runmode specific OSGi configs for the project
* **[ui.content]** - Contains sample content using the components from the ui.apps
* **[ui.frontend]** - An optional dedicated front-end build mechanism (General Webpack project)

## How to build

To build all the modules run in the project root directory the following command with Maven 3:

    mvn clean install

To build all the modules and deploy the `all` package to a local instance of AEM, run in the project root directory the following command:

    mvn clean install -PautoInstallSinglePackage

Or to deploy it to a publish instance, run

    mvn clean install -PautoInstallSinglePackagePublish

Or alternatively

    mvn clean install -PautoInstallSinglePackage -Daem.port=4503

Or to deploy only the bundle to the author, run

    mvn clean install -PautoInstallBundle

Or to deploy only a single content package, run in the sub-module directory (i.e `ui.apps`)

    mvn clean install -PautoInstallPackage