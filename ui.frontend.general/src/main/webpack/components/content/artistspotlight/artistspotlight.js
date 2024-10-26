(function () {
    "use strict";

    let selectors = {
        self: '[data-cmp-is="artistspotlight"]'
    };

    function ArtistSpotlight(config) {

        function init(config) {
            const element = config.element;
            element.removeAttribute("data-cmp-is");

            document.querySelectorAll('.artist-spotlight__artist-albums__albums-list__album-item').forEach(album => {
                album.addEventListener('click', () => {
                    const songsList = album.querySelector('.artist-spotlight__artist-albums__albums-list__songs-list');
                    songsList.classList.toggle('hidden');
                });
            });
        }

        if (config && config.element) {
            init(config);
        }
    }

    function onDocumentReady() {
        const elements = document.querySelectorAll(selectors.self);
        for (let i = 0; i < elements.length; i++) {
            new ArtistSpotlight({element: elements[i]});
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
                                new ArtistSpotlight({element: element});
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
