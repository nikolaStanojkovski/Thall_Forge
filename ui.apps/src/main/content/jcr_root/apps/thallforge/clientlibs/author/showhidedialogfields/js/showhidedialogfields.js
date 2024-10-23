(function(document, $) {
  "use strict";

  // when dialog gets injected
  $(document).on("foundation-contentloaded", function(e) {
    // if there is already an inital value make sure the according target element becomes visible
    showHide($("[data-cq-dialog-dropdown-showhide], [data-cq-dialog-checkbox-showhide]", e.target));
  });

  $(document).on("selected", "[data-cq-dialog-dropdown-showhide]", function(e) {
    showHide($(this));
  });

  $(document).on("change", "[data-cq-dialog-checkbox-showhide]", function(e) {
    showHide($(this));
  });

  function showHide(el){

    el.each(function(i, element) {

      var target, value;
      var type = getFieldType(element);

      switch (type) {
        case "select":
          var widget = $(element).data("select");
          if (widget) {
            // get the selected value
            value =  widget.getValue();
          }
          break;
        case "checkbox":
          // get the selected value
          value =  $(element).prop('checked');
      }

      // get the selector to find the target elements. its stored as data-.. attribute
      target = $(element).data("cq-dialog-showhide-target");

      if (target) {
        hideUnselectedElements(target, value);
        showTarget(target, value);
      }
    });
  }

  //Get type of field
  function getFieldType(element){
    //Check if field is a checkbox
    var type = $(element).prop("type");
    if(type==="checkbox"){
      return "checkbox";
    }
    //Check if field is a dropdown
    var select = $(element).hasClass("coral-Select");
    if(select){
      return "select";
    }

    //Check if field is a CoralUI3 checkbox
    if(element && element.tagName==="CORAL-CHECKBOX"){
      return "checkbox";
    }
  }

  // make sure all unselected target elements are hidden.
  function hideUnselectedElements(target){
    $(target).not(".hide").each(function() {
      $(this).addClass('hide'); //If target is a container, hides the container
      $(this).closest('.coral-Form-fieldwrapper').addClass('hide'); // Hides the target field wrapper. Thus, hiding label, quicktip etc.
    });
  }

  // unhide the target element that contains the selected value as data-showhidetargetvalue attribute
  function showTarget(target, value){
    $(target).filter("[data-showhidetargetvalue*='" + value + "'],:has([data-showhidetargetvalue*='" + value + "'])").each(function() {
      $(this).removeClass('hide');  //If target is a container, unhides the container
      $(this).closest('.coral-Form-fieldwrapper').removeClass('hide'); // Unhides the target field wrapper. Thus, displaying label, quicktip etc.
    });
  }

})(document,Granite.$);