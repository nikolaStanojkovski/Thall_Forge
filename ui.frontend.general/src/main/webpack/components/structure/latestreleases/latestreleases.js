(function () {
    "use strict";

    let selectors = {
        self: '[data-cmp-is="latestreleases"]'
    };

    function LatestReleases(config) {

        function init(config) {
            const element = config.element;
            element.removeAttribute("data-cmp-is");

            const track = document.querySelector('.mdl-latest-releases__carousel-track-container__carousel-track');
            const items = Array.from(track.children);
            const prevButton = document.querySelector('.mdl-latest-releases__carousel-control.prev');
            const nextButton = document.querySelector('.mdl-latest-releases__carousel-control.next');
            const dotsNav = document.querySelector('.mdl-latest-releases__carousel-dots');
            const dots = Array.from(dotsNav.children);

            const itemWidth = items[0].getBoundingClientRect().width;
            let currentIndex = 0;

            const setItemPosition = (item, index) => {
                item.style.left = `${index * (itemWidth + 16)}px`;  // Include gap of 1em (16px)
            };
            items.forEach(setItemPosition);

            const updateCarousel = (currentIndex) => {
                track.style.transform = `translateX(-${currentIndex * (itemWidth + 16)}px)`;
                dots.forEach(dot => dot.classList.remove('active'));
                dots[currentIndex].classList.add('active');
                prevButton.style.visibility = currentIndex === 0 ? 'hidden' : 'visible';
                nextButton.style.visibility = currentIndex === items.length - 1 ? 'hidden' : 'visible';
            };

            nextButton.addEventListener('click', () => {
                if (currentIndex < items.length - 1) {
                    currentIndex++;
                    updateCarousel(currentIndex);
                }
            });

            prevButton.addEventListener('click', () => {
                if (currentIndex > 0) {
                    currentIndex--;
                    updateCarousel(currentIndex);
                }
            });

            dotsNav.addEventListener('click', (e) => {
                const targetDot = e.target.closest('span');
                if (!targetDot) return;

                currentIndex = dots.findIndex(dot => dot === targetDot);
                updateCarousel(currentIndex);
            });

            updateCarousel(currentIndex);
        }

        if (config && config.element) {
            init(config);
        }
    }

    function onDocumentReady() {
        const elements = document.querySelectorAll(selectors.self);
        for (let i = 0; i < elements.length; i++) {
            new LatestReleases({element: elements[i]});
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
                                new LatestReleases({element: element});
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
