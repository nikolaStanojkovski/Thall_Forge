(function () {
    "use strict";

    let selectors = {
        self: '[data-cmp-is="genreexplorer"]',
        albumIdAttr: 'data-album-id',
        albumSongsContainerIdAttr: 'album-songs-container-id'
    };

    function GenreExplorer(config) {

        function init(config) {
            const element = config.element;
            element.removeAttribute("data-cmp-is");

            Array.from(element.querySelectorAll('.mdl-genre-explorer__album-container__album-card'))
                .forEach(albumCard => albumCard.addEventListener('click', () => toggleSongs(element, albumCard)));
        }

        function toggleSongs(element, albumCard) {
            if (!albumCard.hasAttribute('data-album-id')) {
                return;
            }
            const albumId = albumCard.attributes[selectors.albumIdAttr]['value'];
            if (!albumId) {
                return;
            }
            Array.from(element.querySelectorAll('.mdl-genre-explorer__song-list'))
                .forEach(songListContainer => {
                    let songListId = songListContainer.attributes[selectors.albumSongsContainerIdAttr]['value'];
                    if (songListId) {
                        if (songListId !== albumId) {
                            // Hide other albums' songs
                            if (!songListContainer.classList.contains('hidden')) {
                                songListContainer.classList.add('hidden');
                            }
                        } else {
                            // Toggle visibility of the selected album's songs
                            songListContainer.classList.toggle('hidden');
                        }
                    }
                });
        }

        if (config && config.element) {
            init(config);
        }
    }

    function onDocumentReady() {
        const elements = document.querySelectorAll(selectors.self);
        for (let i = 0; i < elements.length; i++) {
            new GenreExplorer({element: elements[i]});
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
                                new GenreExplorer({element: element});
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
