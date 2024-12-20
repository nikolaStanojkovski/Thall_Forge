(function () {
    "use strict";

    let selectors = {
        self: '[data-cmp-is="topfansbanner"]'
    };

    function TopFansBanner(config) {

        function init(config) {
            const element = config.element;
            element.removeAttribute("data-cmp-is");

            const closeButton = element.querySelector('.mdl-top-fans-banner__close-btn');
            closeButton.addEventListener('click', () => {
                element.style.transition = "opacity 0.3s";
                element.style.opacity = "0";
                setTimeout(() => {
                    element.style.display = "none";
                }, 300);
            });
        }

        if (config && config.element) {
            init(config);
        }
    }

    function onDocumentReady() {
        const elements = document.querySelectorAll(selectors.self);
        for (let i = 0; i < elements.length; i++) {
            new TopFansBanner({element: elements[i]});
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
                                new TopFansBanner({element: element});
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
