/*global $, jQuery, base, Materialize */
var filter = (function ($) {

    'use strict';

    var initialise;

    initialise = function () {

        $('#add_new_filter').on('click', function () {

            var filter = $('#new_filter').val();

            if ($.trim(filter.length) > 0) {

                $('#filter_chips').append('<div class="chip"><input type="hidden" value="" class="filter_id" /><span class="filter_value">' +
                    filter + '</span><i class="material-icons">close</i></div>&nbsp;');

                $('#new_filter').val('');
            }

        });
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

        var newAction = $('<div class="valign-wrapper row action action_move"><div class="input-field col s4 action_type">move</div><div class="input-field col s6"><input type="text" placeholder="Move To.." class="f1" /></div><div class="col s2"><i class="material-icons remove_action">close</i></div></div>');
        $('#download_actions').append(newAction);
    };

    addPushbullet = function () {

        var newAction = $('<div class="valign-wrapper row action action_pushbullet"><div class="input-field col s4 action_type">pushbullet</div><div class="input-field col s6"><input type="text" placeholder="API Key" class="f1" /></div><div class="col s2"><i class="material-icons remove_action">close</i></div></div>');
        $('#download_actions').append(newAction);
    };

    addApi = function () {


        var newAction = $('<div class="valign-wrapper row action action_api"><div class="input-field col s4 action_type">api</div><div class="input-field col s6"><input type="text" placeholder="URL" class="f1" /><br />' +
            '<input type="text" placeholder="Method (e.g. POST, GET)" class="f2" /><br /><input type="text" placeholder="Content Type (e.g. application/json)" class="f3" /><br /><input type="text"' +
            ' placeholder="Message body" class="f4" /><br /></div><div class="col s2"><i class="material-icons remove_action">close</i></div></div>');

        $('#download_actions').append(newAction);
    };

    removeAction = function (closeButton) {
        closeButton.parents('.action').remove();
    };

    return {
        init: initialise
    };

}(jQuery));

var schedule = (function ($) {

    'use strict';

    var initialise, save, onSuccess, onError, cleanId, startSchedule, stopSchedule, startResponse, stopResponse, cleanActionFunction;

    initialise = function () {

        $('#start_schedule').on('click', startSchedule);
        $('#stop_schedule').on('click', stopSchedule);

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
                    actionType: $(this).find('.action_type').text(),
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

    startSchedule = function () {

        $.ajax({
            method: "GET",
            url: "/api/v1/schedule/" + cleanId($('#schedule_id').val()) + "/start"
        }).done(startResponse).fail(startResponse);
    };

    stopSchedule = function () {

        $.ajax({
            method: "GET",
            url: "/api/v1/schedule/" + cleanId($('#schedule_id').val()) + "/stop"
        }).done(stopResponse).fail(stopResponse);
    };

    startResponse = function (msg) {
        Materialize.toast(msg.message, 3000, 'rounded');
    };

    stopResponse = function (msg) {
        Materialize.toast(msg.message, 3000, 'rounded');
    };

    onSuccess = function (msg) {

        if (msg.id === cleanId($('#schedule_id').val())) {
            Materialize.toast('Schedule Saved', 3000, 'rounded');
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
