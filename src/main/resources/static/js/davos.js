/*global $, jQuery, base, Materialize */
var filter = (function ($) {

    'use strict';

    var initialise, removeChip, addFilter;

    initialise = function () {

        $('#add_new_filter').on('click', addFilter);

        $('#new_filter').on('keypress', function (e) {

            if (e.keyCode == 13) {
                addFilter();
            }
        });

        $('#filters').on('click', '.chip i.material-icons', function () {
            removeChip($(this).parents('.chip'));
        });
    };

    addFilter = function () {

        var filter = $('#new_filter').val();

        if ($.trim(filter.length) > 0) {

            $('#filter_chips').append('<div class="chip"><input type="hidden" value="" class="filter_id" /><div class="letter">'+filter.substring(0,1)+'</div><span class="filter_value">' +
                filter + '</span><i class="material-icons">close</i></div>&nbsp;');

            $('#new_filter').val('');
        }
    };

    removeChip = function (chip) {
        chip.remove();
    };

    return {
        init: initialise
    };

}(jQuery));

var action = (function ($) {

    'use strict';

    var initialise, addMove, addPushbullet, addApi, removeAction;

    initialise = function () {

        $('#select_download_action').on('change', function () {

            var action = $(this).find('option:selected').val();
            $(this).val('choose');

            if (action === 'pushbullet') {
                addPushbullet();
            }

            if (action === 'move') {
                addMove();
            }

            if (action === 'api') {
                addApi();
            }
        });

        $('#download_actions').on('click', '.remove_action', function () {
            removeAction($(this));
        });
    };

    addMove = function () {
        var newAction = $('<div data-action-type="move" class="mdl-cell mdl-cell--6-col mdl-cell--8-col-tablet mdl-cell--4-col-phone action action_move">'+
          '<div class="demo-card-wide mdl-card mdl-shadow--2dp action-card"><div class="mdl-card__title"><h2 class="mdl-card__title-text">Move Downloaded File</h2>'+
          '<i class="material-icons">folder_open</i></div><div class="mdl-card__actions mdl-card--border"><div class="full mdl-textfield mdl-js-textfield mdl-textfield--floating-label">'+
          '<span>To</span><input class="mdl-textfield__input f1"></div></div><div class="mdl-card__menu"><i class="material-icons remove_action">close</i></div></div></div>');

        $('#download_actions').append(newAction).children(':last').hide().fadeIn(500);
    };

    addPushbullet = function () {

        var newAction = $('<div data-action-type="pushbullet" class="mdl-cell mdl-cell--6-col mdl-cell--8-col-tablet mdl-cell--4-col-phone action action_pushbullet">'+
          '<div class="demo-card-wide mdl-card mdl-shadow--2dp action-card"><div class="mdl-card__title"><h2 class="mdl-card__title-text">Pushbullet Notification</h2>'+
          '</div><div class="mdl-card__actions mdl-card--border"><div class="full mdl-textfield mdl-js-textfield mdl-textfield--floating-label"><span>API Access Token</span>'+
          '<input class="mdl-textfield__input f1"></div></div><div class="mdl-card__menu"><i class="material-icons remove_action">close</i></div></div></div>');

        $('#download_actions').append(newAction).children(':last').hide().fadeIn(500);
    };

    addApi = function () {

        var newAction = $('<div data-action-type="api" class="mdl-cell mdl-cell--6-col mdl-cell--8-col-tablet mdl-cell--4-col-phone action action_api">'+
        '<div class="demo-card-wide mdl-card mdl-shadow--2dp action-card"><div class="mdl-card__title"><h2 class="mdl-card__title-text">Generic HTTP API Call</h2>'+
        '<i class="material-icons">http</i></div><div class="mdl-card__actions mdl-card--border"><div class="full mdl-textfield mdl-js-textfield mdl-textfield--floating-label"><span>URL</span>'+
        '<input class="mdl-textfield__input f1"></div><div class="full mdl-textfield mdl-js-textfield mdl-textfield--floating-label"><span>HTTP Method</span>'+
        '<input class="mdl-textfield__input f2"></div><div class="full mdl-textfield mdl-js-textfield mdl-textfield--floating-label"><span>Content Type</span>'+
        '<input class="mdl-textfield__input f3"></div><div class="full mdl-textfield mdl-js-textfield mdl-textfield--floating-label"><span>Body</span>'+
        '<input class="mdl-textfield__input f4" placeholder="Use $filename to reference the file"></div></div><div class="mdl-card__menu"><i class="material-icons remove_action">close</i></div></div></div>');

        $('#download_actions').append(newAction).children(':last').hide().fadeIn(500);
    };

    removeAction = function (closeButton) {
        closeButton.parents('.action').fadeOut(500).remove();
    };

    return {
        init: initialise
    };

}(jQuery));

var schedule = (function ($) {

    'use strict';

    var initialise, save, onSuccess, onError, cleanId, startSchedule, stopSchedule, startResponse, stopResponse, cleanActionFunction;

    initialise = function () {

        $('.start-stop').on('click', '.schedule-start', function() { startSchedule($(this).attr('data-schedule-id')) });
        $('.start-stop').on('click', '.schedule-stop', function() { stopSchedule($(this).attr('data-schedule-id')) });

        $('#save_schedule').on('click', function () {

            var postData = {

                id: cleanId($('#schedule_id').val()),
                name: $('#schedule_name').val(),
                startAutomatically: $('#start_automatically').prop('checked'),
                interval: parseInt($('#interval option:checked').attr('value'), 10),
                connectionType: $('input[name="protocol_type"]:checked').val(),
                hostName: $('#server_name').val(),
                port: parseInt($('#server_port').val(), 10),
                username: $('#username').val(),
                password: $('#password').val(),
                remoteFilePath: $('#remote_path').val(),
                localFilePath: $('#local_path').val(),
                transferType: $('input[name="scan_type"]:checked').val(),
                lastRun: parseInt($('#schedule_lastrun').val(), 10),
                filters: [],
                actions: []
            };

            $('#filter_chips .chip').each(function () {

                var filter = {

                    id: cleanId($(this).find('.filter_id').val()),
                    value: $(this).find('.filter_value').text()
                };

                postData.filters.push(filter);
            });

            $('#download_actions .action').each(function () {

                var action = {

                    id: cleanId($(this).attr('data-action-id')),
                    actionType: $(this).attr('data-action-type'),
                    f1: cleanActionFunction($(this).find('.f1')),
                    f2: cleanActionFunction($(this).find('.f2')),
                    f3: cleanActionFunction($(this).find('.f3')),
                    f4: cleanActionFunction($(this).find('.f4'))
                };

                postData.actions.push(action);
            });

            $.ajax({

                method: "POST",
                url: "/api/v1/schedule",
                dataType: "json",
                contentType: 'application/json',
                data: JSON.stringify(postData)

            }).done(onSuccess);

        });
    };

    cleanActionFunction = function (action) {

        if (action) {
            return action.val();
        }

        return null;
    };

    cleanId = function (id) {

        if ($.trim(id).length === 0) {
            return null;
        }

        return parseInt(id, 10);
    };

    startSchedule = function (id) {

        $.ajax({
            method: "GET",
            url: "/api/v1/schedule/" + id + "/start"
        }).done(startResponse).fail(startResponse);
    };

    stopSchedule = function (id) {

        $.ajax({
            method: "GET",
            url: "/api/v1/schedule/" + id + "/stop"
        }).done(stopResponse).fail(stopResponse);
    };

    startResponse = function (msg) {
      var snackbarContainer = document.querySelector('#toast-message');
      snackbarContainer.MaterialSnackbar.showSnackbar({ message: msg.message, timeout: 3000 });

      if (msg.message === 'Schedule started') {
          $('#start-stop-' + msg.id).find('.schedule-start').remove();
          $('#start-stop-' + msg.id).append('<button class="mdl-button mdl-js-button mdl-button--raised schedule-stop" data-schedule-id="'+msg.id+'">Stop</button>');
      }
    };

    stopResponse = function (msg) {
      var snackbarContainer = document.querySelector('#toast-message');
      snackbarContainer.MaterialSnackbar.showSnackbar({ message: msg.message, timeout: 3000 });

      if (msg.message === 'Schedule stopped') {
          $('#start-stop-' + msg.id).find('.schedule-stop').remove();
          $('#start-stop-' + msg.id).append('<button class="mdl-button mdl-js-button mdl-button--raised schedule-start" data-schedule-id="'+msg.id+'">Start</button>');
      }
    };

    onSuccess = function (msg) {

        if (msg.id === cleanId($('#schedule_id').val())) {
            var snackbarContainer = document.querySelector('#toast-message');
            snackbarContainer.MaterialSnackbar.showSnackbar({ message: 'Schedule Saved', timeout: 3000 });
        } else {
            window.location.replace('/scheduling/' + msg.id);
        }
    };

    onError = function (msg) {
        Materialize.toast('Unable to save schedule config', 3000, 'rounded');
    };

    return {
        init: initialise
    };

}(jQuery));

jQuery(document).ready(filter.init);
jQuery(document).ready(action.init);
jQuery(document).ready(schedule.init);
