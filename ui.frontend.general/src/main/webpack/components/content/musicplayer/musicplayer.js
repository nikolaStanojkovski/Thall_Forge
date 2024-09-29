(function() {
    "use strict";

    let selectors = {
        self:      '[data-cmp-is="musicplayer"]'
    };

    function HelloWorld(config) {

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
                });
            });
        }

        if (config && config.element) {
            init(config);
        }
    }

    function formatTime(seconds) {
        const minutes = Math.floor(seconds / 60);
        const remainingSeconds = Math.floor(seconds % 60);
        return `${minutes}:${remainingSeconds < 10 ? '0' : ''}${remainingSeconds}`;
    }

    function updateSeekBar(seekBar, audioPlayer, currentTime) {
        seekBar.value = audioPlayer.currentTime;
        currentTime.textContent = formatTime(audioPlayer.currentTime);
    }

    function onDocumentReady() {
        const elements = document.querySelectorAll(selectors.self);
        for (let i = 0; i < elements.length; i++) {
            new HelloWorld({ element: elements[i] });
        }

        const MutationObserver = window.MutationObserver || window.WebKitMutationObserver || window.MozMutationObserver;
        const body             = document.querySelector("body");
        const observer         = new MutationObserver(function(mutations) {
            mutations.forEach(function(mutation) {
                // needed for IE
                const nodesArray = [].slice.call(mutation.addedNodes);
                if (nodesArray.length > 0) {
                    nodesArray.forEach(function(addedNode) {
                        if (addedNode.querySelectorAll) {
                            const elementsArray = [].slice.call(addedNode.querySelectorAll(selectors.self));
                            elementsArray.forEach(function(element) {
                                new HelloWorld({ element: element });
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
