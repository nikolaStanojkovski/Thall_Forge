(function ($document, $) {
  'use strict';

  // when dialog gets injected
  $document.on('foundation-contentloaded', function () {
    // if there is already an initial value make sure the according target
    // element becomes visible
    $('[data-acs-cq-dialog-dropdown-checkbox-showhide]').each(function () {
      // handle Coral3 base drop-down/checkbox
      Coral.commons.ready($(this), function(element) {
        // Make sure the showhide is called at the end of the event-loop, selects in IE11 otherwise are not yet initialized
        setTimeout(function() {
          showHide(element);
        }, 0);
      });
    });

    showCorrectComboTargetElements();

  });

  $document.on('change', '[data-acs-cq-dialog-dropdown-checkbox-showhide]', function () {
    showHide($(this));
  });

  $document.on('change', '[data-acs-cq-dialog-combo-checkbox-showhide]', function () {
    showCorrectComboTargetElements();
  });

  function showHide(el) {
    // get the selector to find the target elements. it is stored as
    // data-attribute 'acsCqDialogDropdownCheckboxShowhideTarget'
    var target = el.data('acsCqDialogDropdownCheckboxShowhideTarget');
    var checkboxValue = '';
    var dropdownValue = '';

    // check if the changed element is the drop-down or the check-box
    // and get the values accordingly
    if ($(el).is('coral-select')) {
      dropdownValue = getDropdownValue(el);
      checkboxValue = getCheckboxValue(el.closest('coral-panel-content').find('coral-checkbox'));
    } else if ($(el).is('coral-checkbox')) {
      dropdownValue = getDropdownValue(el.closest('coral-panel-content').find('coral-select'));
      checkboxValue = getCheckboxValue(el);
    }

    if (typeof (el.data('acsCqDialogDropdownCheckboxShowhideMultifield')) !== 'undefined') {
      showCorrectTargetElementsBeneath(target, dropdownValue, checkboxValue, el.closest('coral-multifield-item'));
    } else {
      showCorrectTargetElementsBeneath(target, dropdownValue, checkboxValue, $(document));
    }
  }

  function getDropdownValue(dropdownElement) {
    return dropdownElement.val();
  }

  function getCheckboxValue(checkBoxElement) {
    // is check-box checked?
    var checked = checkBoxElement.prop('checked');
    // get the selected value
    // if check-box is not checked, we set the value to empty string
    return checked ? checkBoxElement.val() : '';
  }

  function showCorrectTargetElementsBeneath(target, dropdownValue, checkboxValue, $root) {
    // unhide target elements based on the target values
    $root.find(target).each(function () {
      toggleVisibilityElement($(this), !shouldBeVisible($(this), dropdownValue, checkboxValue));
    });
  }

  function showCorrectComboTargetElements() {
    $('[data-acs-combocheckboxshowhideclasses]').each(function () {
      var classes = $(this).data('acsCombocheckboxshowhideclasses').split(' ');
      var values = $(this).data('acsCombocheckboxshowhidevalues').split(' ');
      var operator = $(this).data('acsCombocheckboxshowhideoperator');
      var boolean;
      if (operator === 'AND') {
        boolean = true;
        classes.forEach(function (classvalue, index) {
          var temp = $('[data-acs-cq-dialog-combo-checkbox-showhide-target="' + classvalue + '"]');
          var checked = temp.prop('checked');
          boolean = (boolean && (checked === ('true' === values[index])));
        });
      } else {
        boolean = false;
        classes.forEach(function (classvalue, index) {
          var temp = $('[data-acs-cq-dialog-combo-checkbox-showhide-target="' + classvalue + '"]');
          var checked = temp.prop('checked');
          boolean = (boolean || (checked === ('true' === values[index])));
        });
      }
      toggleVisibilityElement($(this), !boolean);

    });
  }

  /**
   * Checks if the element should be visible based on selected dropdown value
   * and checkbox value
   */
  function shouldBeVisible($elem, dropdownValue, checkboxValue) {
    if ($elem.is('[data-acs-dropdownshowhidetargetvalue]') && $elem.is('[data-acs-checkboxshowhidetargetvalue]')) {
      return ($elem.attr('data-acs-dropdownshowhidetargetvalue').split(' ').indexOf(dropdownValue) >= 0) && $elem.attr('data-acs-checkboxshowhidetargetvalue') === checkboxValue;
    } else if ($elem.is('[data-acs-dropdownshowhidetargetvalue]')) {
      return $elem.attr('data-acs-dropdownshowhidetargetvalue').split(' ').indexOf(dropdownValue) >= 0;
    } else if ($elem.is('[data-acs-checkboxshowhidetargetvalue]')) {
      return $elem.attr('data-acs-checkboxshowhidetargetvalue') === checkboxValue;
    } else if ($elem.is('[data-acs-dropdownshowhidetargetnotvalue]')) {
      return $elem.attr('data-acs-dropdownshowhidetargetnotvalue') !== dropdownValue;
    }
    return false;
  }

  /**
   * Hides/unhides the element
   */
  function toggleVisibilityElement($elem, hide) {
    var $fieldWrapper = $elem.closest('.coral-Form-fieldwrapper');
    var tabPanel = $elem.parent().parent('coral-panel[role="region"],coral-panel[role="tabpanel"]');
    var tabLabelId = $(tabPanel).attr('aria-labelledby');
    var disable = $elem.attr('data-acs-disablewhenhidden');
    var required = $elem.attr('data-acs-optionalwhenhidden');

    if (hide) {
      // If target is a container, hides the container
      $elem.addClass('hide');
      if (!$elem.is('coral-checkbox') || ($elem.siblings('.coral-Form-fieldinfo').length > 0) || typeof ($elem.data('cqMsmLockable')) !== 'undefined') {
        // Hides the target field wrapper. Thus, hiding label, quicktip etc.
        $fieldWrapper.addClass('hide');
      }

      // hide the tab
      $('#' + tabLabelId).addClass('hide');

      //disable all input fields within the element if necessary
      if (disable === 'true') {
        $elem.find('.coral-Form-field').attr('disabled', '');
      }

      //disable validation when a tab is hidden
      if (required === 'true') {
        $elem.find('.coral-Form-field').removeAttr('required');
        $elem.find('input.coral-Form-field').attr('aria-required', 'false');
      }
    } else {
      // Unhide target container/field wrapper/tab
      $elem.removeClass('hide');
      if (!$elem.is('coral-checkbox') || ($elem.siblings('.coral-Form-fieldinfo').length > 0) || typeof ($elem.data('cqMsmLockable')) !== 'undefined') {
        $fieldWrapper.removeClass('hide');
      }
      $('#' + tabLabelId).removeClass('hide');

      //enable all input fields within the element if necessary
      if (disable === 'true') {
        $elem.find('.coral-Form-field').removeAttr('disabled');
      }

      //enable all inputfields when the tab is shown again
      if (required === 'true') {
        $elem.find('.coral-Form-field').attr('required');
        var inputField = $elem.find('input.coral-Form-field');
        $(inputField).attr('aria-required', 'true');
        $(inputField).attr('aria-invalid', 'true');
      }
    }
  }

})($(document), Granite.$);