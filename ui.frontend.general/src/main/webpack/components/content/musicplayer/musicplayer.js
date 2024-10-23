(function () {
    "use strict";

    let selectors = {
        self: '[data-cmp-is="musicplayer"]',
        shuffleEndpointAttr: 'data-shuffle-endpoint',
    };

    function MusicPlayer(config) {

        function init(config) {
            const element = config.element;
            element.removeAttribute("data-cmp-is");

            document.addEventListener('DOMContentLoaded', function () {
                const audioPlayer = element.querySelector(".mdl-music-player__audio");
                const playPauseBtn = element.querySelector(".mdl-music-player__play-pause");
                const seekBar = element.querySelector(".mdl-music-player__seek-bar");
                const currentTime = element.querySelector(".mdl-music-player__current-time");
                const trackDuration = element.querySelector(".mdl-music-player__track-duration");
                const volumeBar = element.querySelector(".mdl-music-player__volume-control");
                const repeatBtn = element.querySelector(".mdl-music-player__repeat-button");
                const shuffleBtn = element.querySelector(".mdl-music-player__shuffle-button");

                let isPlaying = false;
                let isShuffle = false;
                let isRepeat = false;

                playPauseBtn.addEventListener('click', function () {
                    if (isPlaying) {
                        audioPlayer.pause();
                        playPauseBtn.textContent = 'Play';
                    } else {
                        audioPlayer.play();
                        playPauseBtn.textContent = 'Pause';
                    }
                    isPlaying = !isPlaying;
                });

                // Update seek bar max value when metadata is loaded
                audioPlayer.addEventListener('loadedmetadata', function () {
                    seekBar.max = audioPlayer.duration;
                    trackDuration.textContent = formatTime(audioPlayer.duration);
                });

                // Seek track when user changes the seek bar
                seekBar.addEventListener('input', function () {
                    audioPlayer.currentTime = seekBar.value;
                });

                // Update seek bar as the audio plays
                audioPlayer.addEventListener('timeupdate', () => updateSeekBar(seekBar, audioPlayer, currentTime));

                // Volume control
                volumeBar.addEventListener('input', function () {
                    audioPlayer.volume = volumeBar.value;
                });

                // Repeat functionality
                repeatBtn.addEventListener('click', function () {
                    isRepeat = !isRepeat;
                    repeatBtn.style.backgroundColor = isRepeat ? '#4caf50' : '#444';
                    audioPlayer.loop = isRepeat;
                });

                // Shuffle functionality (mock for now, backend logic should handle actual shuffle)
                shuffleBtn.addEventListener('click', function () {
                    isShuffle = !isShuffle;
                    shuffleBtn.style.backgroundColor = isShuffle ? '#4caf50' : '#444';

                    if (isShuffle) {
                        fetchRandomTrack(element, audioPlayer, seekBar, trackDuration);
                    }
                });
            });
        }

        function fetchRandomTrack(element, audioPlayer, seekBar, trackDuration) {
            if (element.hasAttribute(selectors.shuffleEndpointAttr)) {
                const shuffleEndpoint = element.attributes[selectors.shuffleEndpointAttr]['value'];
                fetch(shuffleEndpoint, {
                    method: 'GET',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                })
                    .then(response => response.json())
                    .then(data => {
                        if (data) {
                            updateTrackDetails(element, audioPlayer, seekBar, trackDuration, data);
                        }
                    })
                    .catch(error => {
                        console.error('Error fetching shuffled track:', error);
                    });
            }
        }

        function updateTrackDetails(element, audioPlayer, seekBar, trackDuration, trackData) {
            const titleElement = element.querySelector(".mdl-music-player__track-title");
            const artistElement = element.querySelector(".mdl-music-player__track-artist");
            const albumCoverElement = element.querySelector(".mdl-music-player__album-cover");
            const audioTrackElement = element.querySelector(".mdl-music-player__audio");

            titleElement.textContent = trackData['title'];
            artistElement.textContent = trackData['artist'];
            albumCoverElement.src = trackData['coverLink'];
            audioTrackElement.src = trackData['trackLink'];
            audioPlayer.load();

            audioPlayer.addEventListener('loadedmetadata', function () {
                seekBar.max = audioPlayer.duration;
                trackDuration.textContent = formatTime(audioPlayer.duration);
            });
        }

        function updateSeekBar(seekBar, audioPlayer, currentTime) {
            seekBar.value = audioPlayer.currentTime;
            currentTime.textContent = formatTime(audioPlayer.currentTime);
        }

        function formatTime(seconds) {
            const minutes = Math.floor(seconds / 60);
            const remainingSeconds = Math.floor(seconds % 60);
            return `${minutes}:${remainingSeconds < 10 ? '0' : ''}${remainingSeconds}`;
        }

        if (config && config.element) {
            init(config);
        }
    }

    function onDocumentReady() {
        const elements = document.querySelectorAll(selectors.self);
        for (let i = 0; i < elements.length; i++) {
            new MusicPlayer({element: elements[i]});
        }

        const MutationObserver = window.MutationObserver || window.WebKitMutationObserver || window.MozMutationObserver;
        const body = document.querySelector("body");
        const observer = new MutationObserver(function (mutations) {
            mutations.forEach(function (mutation) {
                // needed for IE
                const nodesArray = [].slice.call(mutation.addedNodes);
                if (nodesArray.length > 0) {
                    nodesArray.forEach(function (addedNode) {
                        if (addedNode.querySelectorAll) {
                            const elementsArray = [].slice.call(addedNode.querySelectorAll(selectors.self));
                            elementsArray.forEach(function (element) {
                                new MusicPlayer({element: element});
                            });
                        }
                    });
                }
            });
        });

        observer.observe(body, {
            subtree: true,
            childList: true,
            characterData: true
        });
    }

    if (document.readyState !== "loading") {
        onDocumentReady();
    } else {
        document.addEventListener("DOMContentLoaded", onDocumentReady);
    }
}());
