/*global $, jQuery, base, Materialize */
var settings = (function($) {

    'use strict';

    var initialise, makeNotify, validate;

    makeNotify = function(notificationType, messageText, icon) {

        $.notify({
            icon: 'glyphicon ' + icon,
            message: messageText
        }, {
            // settings
            type: notificationType,
            placement: {
                from: "top",
                align: "right"
            },
            delay: 3000
        });
    };

    validate = function() {

        var validationPassed = true;

        $('input[type="text"].validate, input[type="number"].validate').each(function() {

            if ($.trim($(this).val()).length === 0) {
                $(this).parents('.form-group').addClass('has-error');
                validationPassed = false;
            } else {
                $(this).parents('.form-group').removeClass('has-error');
            }
        });

        return validationPassed;
    };

    initialise = function() {

        $('#logLevel').on('change', function() {

            var logLevel = $(this).find('option:selected').val();

            makeNotify('info', 'Changing logging level to ' + logLevel, 'info');

            $.ajax({
                method: 'POST',
                url: '/api/v2/settings/log?level=' + logLevel
            }).done(function(msg) {
                makeNotify('success', 'Settings saved!' + theme, 'ok');
            }).fail(function(msg) {
                makeNotify('danger', 'There was an error: ' + msg.responseJSON.status, 'warning');
            });
        });
    };

    return {
        init: initialise,
        notify: makeNotify,
        validate: validate
    }

}(jQuery));

var fragments = (function($) {

    'use strict';

    var initialise, clicks, removes, keypresses;

    initialise = function() {

        clicks();
        removes();
        keypresses();
    };

    clicks = function() {

        $('#newAPI').on('click', function() {
            $('#apis').append($("<div />").load("/fragments/api"));
        });

        $('#newPushbullet').on('click', function() {
            $('#notifications').append($("<div />").load("/fragments/notification"));
        });

        $('#addFilter').on('click', function() {

            if ($.trim($('#newFilter').val()).length > 0) {
                $('#filters').append($("<span />").load("/fragments/filter?value=" + $('#newFilter').val()));
                $('#newFilter').val('');
            }
        });
    };

    keypresses = function() {

        $('#newFilter').on('keypress', function(e) {

            if (e.keyCode == 13) {

                if ($.trim($('#newFilter').val()).length > 0) {
                    $('#filters').append($("<span />").load("/fragments/filter?value=" + $('#newFilter').val()));
                    $('#newFilter').val('');
                }
            }
        });
    };

    removes = function() {

        $('#notifications').on('click', '.remove-notification', function() {
            $(this).parents('.notification').remove();
        });

        $('#apis').on('click', '.remove-api', function() {
            $(this).parents('.api').remove();
        });

        $('#filters').on('click', '.filter-close', function() {
            $(this).parents('.filter-label').remove();
        });
    }

    return {
        init: initialise
    };

}(jQuery));

var schedule = (function($, settings) {

    'use strict';

    var initialise, cleanId, success, error;

    initialise = function() {

        $('#schedule-form').on('submit', function(e) {
            e.preventDefault();
            e.stopPropagation();
        });

        $('#saveSchedule').on('click', function() {

            settings.notify('info', 'Saving...', 'glyphicon-info-sign');

            if (settings.validate()) {

                var postData = {

                    id: cleanId($('#id').val()),
                    name: $('#name').val(),
                    interval: parseInt($('#interval option:checked').attr('value'), 10),
                    host: parseInt($('#host option:checked').attr('value'), 10),
                    hostDirectory: $('#hostDirectory').val(),
                    localDirectory: $('#localDirectory').val(),
                    transferType: $('input[name="transferType"]:checked').val(),
                    automatic: $('input[name="automatic"]').prop('checked'),
                    filtersMandatory: $('input[name="filtersMandatory"]').prop('checked'),
                    invertFilters: $('input[name="invertFilters"]').prop('checked'),
                    deleteHostFile: $('input[name="deleteHostFile"]').prop('checked'),
                    moveFileTo: $('#moveFileTo').val(),
                    filters: [],
                    notifications: [],
                    apis: []
                };

                $('.filter-label').each(function() {

                    postData.filters.push({
                        "id": cleanId($(this).attr('data-filter-id')),
                        "value": $(this).attr('data-filter-value')
                    });
                });

                $('#notifications .notification').each(function() {

                    postData.notifications.push({
                        "id": cleanId($(this).attr('data-notification-id')),
                        "apiKey": $(this).find('.apiKey').val()
                    });
                });

                $('#apis .api').each(function() {

                    postData.apis.push({
                        "id": cleanId($(this).attr('data-api-id')),
                        "url": $(this).find('.url').val(),
                        "method": $(this).find('.method option:checked').attr('value'),
                        "contentType": $(this).find('.contentType').val(),
                        "body": $(this).find('.body').val()
                    });
                });

                var url = "/api/v2/schedule";
                var method = "POST";

                if (null !== cleanId($('#id').val())) {

                    url += "/" + cleanId($('#id').val());
                    method = "PUT";
                }

                $.ajax({

                    method: method,
                    url: url,
                    dataType: "json",
                    contentType: 'application/json',
                    data: JSON.stringify(postData)

                }).done(success).fail(error);

            } else {
                settings.notify('danger', 'Required fields are missing.', 'glyphicon-warning-sign');
            }
        });

        $('#deleteSchedule').on('click', function() {

            $.ajax({
                method: 'DELETE',
                url: '/api/v2/schedule/' + $('#id').val()
            }).done(function(msg) {
                window.location.replace('/schedules');
            }).fail(error);
        });

        $('.start-schedule').on('click', function() {

            var id = $(this).attr('data-schedule-id'),
                name = $(this).attr('data-schedule-name');

            settings.notify('info', 'Starting schedule "' + name + '"', 'glyphicon-info-sign');

            $.ajax({

                method: 'POST',
                url: '/api/v2/schedule/' + id + '/execute',
                dataType: "json",
                contentType: 'application/json',
                data: JSON.stringify({
                    command: 'START'
                })

            }).done(function(msg) {

                settings.notify('success', 'Schedule Started', 'glyphicon-ok-sign');

                $('span[data-schedule-id="' + id + '"].start-schedule').toggleClass('hide');
                $('span[data-schedule-id="' + id + '"].stop-schedule').parents('span').toggleClass('hide');

            }).fail(error);

        });

        $('.stop-schedule').on('click', function() {

            var id = $(this).attr('data-schedule-id'),
                name = $(this).attr('data-schedule-name');

            settings.notify('info', 'Stopping schedule "' + name + '"', 'glyphicon-info-sign');

            $.ajax({

                method: 'POST',
                url: '/api/v2/schedule/' + id + '/execute',
                dataType: "json",
                contentType: 'application/json',
                data: JSON.stringify({
                    command: 'STOP'
                })

            }).done(function(msg) {

                settings.notify('success', 'Schedule Stopped', 'glyphicon-ok-sign');

                $('span[data-schedule-id="' + id + '"].start-schedule').toggleClass('hide');
                $('span[data-schedule-id="' + id + '"].stop-schedule').parents('span').toggleClass('hide');

            }).fail(error);

        });

    };

    cleanId = function(id) {

        if (id && $.trim(id).length > 0) {
            return parseInt(id, 10);
        }

        return null;
    };

    success = function(msg) {

        settings.notify('success', 'Schedule Saved', 'glyphicon-ok-sign');

        if (window.location.pathname === '/schedules/new') {
            window.location.replace('/schedules/' + msg.body.id);
        }
    };

    error = function(msg) {
        settings.notify('danger', 'There was an error: ' + msg.responseJSON.status, 'glyphicon-warning-sign');
    };

    return {
        init: initialise
    }

}(jQuery, settings));

var host = (function($, settings) {

    'use strict';

    var initialise, cleanId, makeRequest, success, error, validate;

    initialise = function() {

        $('#testConnection').on('click', function() {

            settings.notify('info', 'Testing connection...', 'glyphicon-info-sign');

            var postData = {
                address: $('#address').val(),
                port: parseInt($('#port').val(), 10),
                protocol: $('input[name="protocol"]:checked').val(),
                username: $('#username').val(),
                password: $('#password').val()
            };

            var url = "/api/v2/testConnection";
            var method = "POST";

            makeRequest(url, method, postData, function(msg) {
                settings.notify('success', 'Connection successful!', 'glyphicon-ok-sign');
            }, error);

        });

        $('#saveHost').on('click', function() {

            settings.notify('info', 'Saving...', 'glyphicon-info-sign');

            if (settings.validate()) {

                var postData = {
                    id: cleanId($('#id').val()),
                    name: $('#name').val(),
                    address: $('#address').val(),
                    port: parseInt($('#port').val(), 10),
                    protocol: $('input[name="protocol"]:checked').val(),
                    username: $('#username').val(),
                    password: $('#password').val()
                };

                var url = "/api/v2/host";
                var method = "POST";

                if (null !== cleanId($('#id').val())) {

                    url += "/" + cleanId($('#id').val());
                    method = "PUT";
                }

                makeRequest(url, method, postData, success, error);
            } else {
                settings.notify('danger', 'Required fields are missing', 'glyphicon-warning-sign');
            }
        });

        $('#deleteHost').on('click', function() {

            $.ajax({
                method: 'DELETE',
                url: '/api/v2/host/' + $('#id').val()
            }).done(function(msg) {
                window.location.replace('/hosts');
            }).fail(error);
        });
    };

    makeRequest = function(url, method, postData, successCallback, errorCallback) {

        $.ajax({

            method: method,
            url: url,
            dataType: "json",
            contentType: 'application/json',
            data: JSON.stringify(postData)

        }).done(successCallback).fail(errorCallback);

    };

    success = function(msg) {

        settings.notify('success', 'Host Saved!', 'glyphicon-ok-sign');

        if (window.location.pathname === '/hosts/new') {
            window.location.replace('/hosts/' + msg.body.id);
        }
    };

    error = function(msg) {
        settings.notify('danger', 'There was an error: ' + msg.responseJSON.body, 'glyphicon-warning-sign');
    };

    cleanId = function(id) {

        if (id && $.trim(id).length > 0) {
            return parseInt(id, 10);
        }

        return null;
    };

    return {
        init: initialise
    }

}(jQuery, settings))

jQuery(document).ready(host.init);
jQuery(document).ready(schedule.init);
jQuery(document).ready(fragments.init);
jQuery(document).ready(settings.init);
