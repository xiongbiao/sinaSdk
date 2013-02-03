// Popover function
(function ($) {
  var popmouse = false;
  $.fn.popover = function () {
    $(this).click(
      function (e) {
        e.preventDefault();
        var trigger = $(this);
        var popover = $(trigger).parent().find(".popover");
        if ($(popover).is(':visible')) {
          // close the popover if it's already showing
          $().clearpop();
        } else {
          // wipe existing popovers
          $().clearpop();
          // mark trigger active
          $(trigger).addClass("active");
          // on trigger click
          $(popover).fadeIn('fast',
                            function () {
                              // focus the first input if popover has any
                              if ($(this).find('input').length) {
                                $('input:first').focus();
                              }
                            }).show().hover(function () {
                                              // on popover hover
                                              var popmouse = true;
                                            }, function () {
                                              // on popover hover out
                                              var popmouse = false;
                                            });
        }
      }).hover(function () {
                 // on trigger hover
                 var popmouse = true;
               }, function () {
                 // on trigger hover out
                 var popmouse = false;
               });
  };
  $.fn.clearpop = function () {
    if (!popmouse) {
      var popover = $(".popover");
      var trigger = $(popover).parent().find("a.active");
      $(trigger).removeClass("active");
      $(popover).fadeOut("fast");
    }
  };
  $.fn.addcommas = function () {
    var commatize = function (num) {
      num += '';
      if (isNaN(num)) {
        return $(this);
      }
      num_split = num.split('.');
      num_split_1 = num_split[0];
      num_split_2 = num_split.length > 1 ? '.' + num_split[1] : '';
      var rgx = /(\d+)(\d{3})/;
      while (rgx.test(num_split_1)) {
        num_split_1 = num_split_1.replace(rgx, '$1' + ',' + '$2');
      }
      return num_split_1 + num_split_2;
    };
    var raw_num = $(this).text();
    var nice_num = commatize(raw_num);
    return $(this).text(nice_num);
  };
  $.fn.clearform = function (ele) {
    $(ele).find(':input').each(function () {
      switch (this.type) {
        case 'password':
        case 'select-multiple':
        case 'select-one':
        case 'text':
        case 'textarea':
          $(this).val('');
          break;
        case 'checkbox':
        case 'radio':
          this.checked = false;
      }
    });
  };
})(jQuery);

$(document).ready(function () {
  window.setTimeout(function () {
    $('#notification').fadeOut('slow');
  }, 4000);
  if ($("#id_mode").val() == "development") {
    $(".production_form_item").hide();
  }
  $("#id_mode").change(function (e) {
    var val = $(this).val();
    if (val == "development") {
      $(".production_form_item").hide();
    } else {
      $(".production_form_item").show();
    }
  });
  if ($("#id_push_service_status").val() == "disabled") {
    $(".push_form_item").hide();
  }
  $("#id_push_service_status").change(function (e) {
    var val = $(this).val();
    if (val == "disabled") {
      $(".push_form_item").hide();
      $(".push_form_item").children("input").val("");
    } else {
      $(".push_form_item").show();
    }
  });
  $("#id_inapp_enabled").each(function () {
    if (!$(this).is(":checked")) {
      $(".iap_form_item").hide();
    } else {
      $("#inapp_spacer").addClass("active_spacing");
    }
  });
  $("#id_inapp_enabled").click(function (e) {
    if ($(this).is(":checked")) {
      $(".iap_form_item").show();
      $("#inapp_spacer").addClass("active_spacing");
    } else {
      $(".iap_form_item").hide();
      $("#inapp_spacer").removeClass("active_spacing");
      $(".iap_form_item").children("input").val("");
    }
  });
  $("#id_push_enabled").each(function () {
    if (!$(this).is(":checked")) {
      $(".push_form_item").hide();
    } else {
      $("#inapp_spacer").addClass("active_spacing");
    }
  });
  $("#id_push_enabled").click(function (e) {
    if ($(this).is(":checked")) {
      $(".push_form_item").show();
      $('#id_debug_mode').attr('checked', ($('#id_mode option:selected').attr('value') === "development" ? true : false));
      $("#inapp_spacer").addClass("active_spacing");
    } else {
      $(".push_form_item").hide();
      $("#inapp_spacer").removeClass("active_spacing");
      $(".push_form_item").children("input").val("");
    }
  });
  $('#id_mode').change(function () {
    if ($('#id_push_enabled').attr('checked'))
      $('#id_debug_mode').attr('checked', ($('#id_mode option:selected').attr('value') === "development" ? true : false));
  });
  $("a.test_feedback_hook").click(function (e) {
    e.preventDefault();
    var url = $(this).attr('href');
    $("img#spinner").show();
    $.getJSON(url, function (data) {
      $("img#spinner").hide();
      $("span#test_feedback_hook_result_label").show();
      $("#test_feedback_hook_result").text(data.status);
    });
  });
  $("#id_auto_renewable").click(function () {
    if ($(this).is(":checked")) {
      $(".legacy_options").hide();
      $(".ar_options").show();
      $(".legacy_options").children("input").val("");
    } else {
      $(".legacy_options").show();
      $(".ar_options").hide();
      $(".ar_options").children("input").val("");
    }
  });
  $("#id_auto_renewable").each(function () {
    if ($(this).is(":checked")) {
      $(".legacy_options").hide();
      $(".ar_options").show();
    }
  });

  $("#navigation a").click(function (e) {
    $("#navigation a").each(function () {
      $(this).removeClass("current");
    });
    $(this).addClass("current");
  });
  $("div#app_dropdown a").click(function (e) {
    $("#navigation a").each(function () {
      $(this).removeClass("current");
    });
    $("#apps_button > a").addClass("current");
  });

  var handleProductTypeChanges = function (productType) {
    switch (productType) {
      case "0": // Legacy
        $(".legacy_options").show();
        $(".ar_options").hide();
        $(".ar_options").children("input").val("");
        break;
      case "1": // autorenewables
        $(".legacy_options").hide();
        $(".ar_options").show();
        $(".legacy_options").children("input").val("");
        break;
      case "2": // free
        $(".legacy_options").hide();
        $(".ar_options").hide();
        $(".ar_options").children("input").val("");
        $(".legacy_options").children("input").val("");
        break;
    }
  };

  $("#id_product_type").change(function () {
    handleProductTypeChanges($(this).val());
  });
  handleProductTypeChanges($("#id_product_type").val());

  $("#id_newsstand").click(function () {
    if ($(this).is(":checked")) {
      $(".ns_options").show();
    } else {
      $(".ns_options").hide();
    }
  });
  $("#id_newsstand").each(function () {
    if ($(this).is(":checked")) {
      $(".ns_options").show();
    }
  });
  $("#change_cc").click(function (e) {
    e.preventDefault();
    $("#cc_info").hide();
    $("#cc_form").removeClass("hidden");
  });
  $(".announcement_mark_read a").click(function (e) {
    e.preventDefault();
    var toHide = $(this).closest("div.announcement");
    $.getJSON($(this).attr("href"), function (data) {
      if (data.ok) {
        toHide.hide();
      }
    });
  });
  // popover menus
  $(".nav > li > a, .appselect_button").popover();
  $(".inside, .footer").bind("mouseup", function () {
    $().clearpop();
  });
  $(document).keydown(function (e) {
    // ESCAPE key pressed
    if (e.keyCode == 27) {
      $().clearpop();
    }
  });
  // inline edit switches display between parent of trigger, and a sibling of parent with a .hidden class
  $('body').on('click', 'a.inline_edit', function (ev) {
    ev.preventDefault()

    var self = $(this)
      , parent = $(this).parent()
      , siblings = parent.siblings('.hidden')

    siblings.find('.cancel').click(click_cancel)
    siblings.removeClass('hidden')
    parent.addClass('hidden')
    function click_cancel(ev) {
      ev.preventDefault()
      siblings.addClass('hidden')
      parent.removeClass('hidden')
      siblings.unbind('click', click_cancel)
    }
  });
  
  
});
