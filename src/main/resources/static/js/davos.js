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

    var initialise;

    initialise = function () {


    };

    return {
        init: initialise
    };

}(jQuery));

var schedule = (function ($) {

    'use strict';

    var initialise, save, onSuccess, onError, cleanId, startSchedule, stopSchedule, startResponse, stopResponse;

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

            $.ajax({

                method: "POST",
                url: "/api/v1/schedule",
                dataType: "json",
                contentType: 'application/json',
                data: JSON.stringify(postData)

            }).done(onSuccess);

        });
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
