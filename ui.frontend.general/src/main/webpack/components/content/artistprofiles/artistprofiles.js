(function () {
    "use strict";

    let selectors = {
        self: '[data-cmp-is="artistprofiles"]',
        starRatingAttr: 'data-star-rating'
    };

    function ArtistProfiles(config) {

        function init(config) {
            const element = config.element;
            element.removeAttribute("data-cmp-is");

            element.querySelectorAll('.artistprofilesitem__header').forEach(accordionItemHeader => {
                accordionItemHeader.addEventListener('click', function () {
                    const contentElement = this.nextElementSibling;
                    contentElement.classList.toggle('active');
                });
            });
            element.querySelectorAll('.artistprofilesitem__content__star-rating')
                .forEach(accordionItemRating => fillStarRating(accordionItemRating));
        }

        function fillStarRating(starRatingItem) {
            const rating = parseInt(starRatingItem.getAttribute(selectors.starRatingAttr), 10);
            const maxStars = 5;
            starRatingItem.innerHTML = '';

            for (let i = 0; i < rating; i++) {
                const filledStar = document.createElement('span');
                filledStar.className = 'filled';
                filledStar.textContent = '★';
                starRatingItem.appendChild(filledStar);
            }

            for (let i = rating; i < maxStars; i++) {
                const emptyStar = document.createElement('span');
                emptyStar.className = 'empty';
                emptyStar.textContent = '★';
                starRatingItem.appendChild(emptyStar);
            }
        }

        if (config && config.element) {
            init(config);
        }
    }

    function onDocumentReady() {
        const elements = document.querySelectorAll(selectors.self);
        for (let i = 0; i < elements.length; i++) {
            new ArtistProfiles({element: elements[i]});
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
                                new ArtistProfiles({element: element});
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
