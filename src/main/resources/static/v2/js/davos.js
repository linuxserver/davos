/*global $, jQuery, base, Materialize */

var edit = (function($) {

    'use strict';

    var initialise;

    initialise = function() {

    };

    return {
        init: initialise
    }

}(jQuery));

var settings = (function ($) {

    'use strict';

    var initialise;

    initialise = function () {

        $('#logLevel').on('change', function() {

            var logLevel = $(this).find('option:selected').val();

            $.notify({
                icon: 'glyphicon glyphicon-info-sign',
            	message: 'Changing logging level to ' + logLevel
            },{
            	// settings
            	type: 'info',
                placement: {
                	from: "top",
                	align: "right"
                },
                delay: 3000
            });

            $.ajax({
                method: 'POST',
                url: '/api/v2/settings/log?level=' + logLevel
            }).done(function (msg) {

                $.notify({
                    icon: 'glyphicon glyphicon-ok-sign',
                    message: 'Settings saved!'
                },{
                    // settings
                    type: 'success',
                    placement: {
                        from: "top",
                        align: "right"
                    },
                    delay: 3000
                });

            }).fail(function (msg) {

                $.notify({
                    icon: 'glyphicon glyphicon-warning-sign',
                    message: 'There was an error: ' + msg.status
                },{
                    // settings
                    type: 'danger',
                    placement: {
                        from: "top",
                        align: "right"
                    },
                    delay: 3000
                });

            });
        });
    };

    return {
        init: initialise
    }

}(jQuery));

var fragments = (function ($) {

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
            $('#filters').append($("<span />").load("/fragments/filter?value=" + $('#newFilter').val()));
            $('#newFilter').val('');
        });
    };

    keypresses = function() {

        $('#newFilter').on('keypress', function (e) {

            if (e.keyCode == 13) {

                $('#filters').append($("<span />").load("/fragments/filter?value=" + $('#newFilter').val()));
                $('#newFilter').val('');
            }
        });
    };

    removes = function() {

        $('#notifications').on('click', '.remove', function() {
            $(this).parents('.notification').remove();
        });

        $('#apis').on('click', '.remove', function() {
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

var schedule = (function ($) {

    'use strict';

    var initialise, cleanId, success, error;

    initialise = function () {

        $('#saveSchedule').on('click', function () {

            $.notify({
                icon: 'glyphicon glyphicon-info-sign',
            	message: 'Saving...'
            },{
            	// settings
            	type: 'info',
                placement: {
                	from: "top",
                	align: "right"
                },
                delay: 3000
            });

            var postData = {

                id: cleanId($('#id').val()),
                name: $('#name').val(),
                interval: parseInt($('#interval option:checked').attr('value'), 10),
                host: parseInt($('#host option:checked').attr('value'), 10),
                hostDirectory: $('#hostDirectory').val(),
                localDirectory: $('#localDirectory').val(),
                transferType: $('input[name="transferType"]:checked').val(),
                automatic: $('input[name="automatic"]').prop('checked'),
                moveFileTo: $('#moveFileTo').val(),
                filters: [],
                notifications: [],
                apis: []
            };

            $('.filter-label').each(function() {

                postData.filters.push(
                    {
                        "id": cleanId($(this).attr('data-filter-id')),
                        "value": $(this).attr('data-filter-value')
                    }
                );
            });

            $('#notifications .notification').each(function() {

                postData.notifications.push(
                    {
                        "id": cleanId($(this).attr('data-notification-id')),
                        "apiKey": $(this).find('.apiKey').val()
                    }
                );
            });

            $('#apis .api').each(function() {

                postData.apis.push(
                    {
                        "id": cleanId($(this).attr('data-api-id')),
                        "url": $(this).find('.url').val(),
                        "method": $(this).find('.method option:checked').attr('value'),
                        "contentType": $(this).find('.contentType').val(),
                        "body": $(this).find('.body').val()
                    }
                );
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
        });

        $('#deleteSchedule').on('click', function () {

            $.ajax({
                method: 'DELETE',
                url: '/api/v2/schedule/' + $('#id').val()
            }).done(function (msg) {
                window.location.replace('/v2/schedules');
            }).fail(error);
        });

        $('.start-schedule').on('click', function () {

            var id = $(this).attr('data-schedule-id'), name = $(this).attr('data-schedule-name');

            $.notify({
                icon: 'glyphicon glyphicon-info-sign',
                message: 'Starting schedule "' + name + '"'
            },{
                // settings
                type: 'info',
                placement: {
                    from: "top",
                    align: "right"
                },
                delay: 3000
            });

            $.ajax({

                method: 'POST',
                url: '/api/v2/schedule/' + id + '/execute' ,
                dataType: "json",
                contentType: 'application/json',
                data: JSON.stringify({
                    command: 'START'
                })

            }).done(function (msg) {

                $.notify({
                    icon: 'glyphicon glyphicon-ok-sign',
                    message: 'Schedule Started'
                },{
                    // settings
                    type: 'success',
                    placement: {
                        from: "top",
                        align: "right"
                    },
                    delay: 3000
                });

                $('span[data-schedule-id="' + id +'"].start-schedule').toggleClass('hide');
                $('span[data-schedule-id="' + id +'"].stop-schedule').parents('span').toggleClass('hide');

            }).fail(error);

        });

        $('.stop-schedule').on('click', function () {

            var id = $(this).attr('data-schedule-id'), name = $(this).attr('data-schedule-name');

            $.notify({
                icon: 'glyphicon glyphicon-info-sign',
                message: 'Stopping schedule "' + name + '"'
            },{
                // settings
                type: 'info',
                placement: {
                    from: "top",
                    align: "right"
                },
                delay: 3000
            });

            $.ajax({

                method: 'POST',
                url: '/api/v2/schedule/' + id + '/execute' ,
                dataType: "json",
                contentType: 'application/json',
                data: JSON.stringify({
                    command: 'STOP'
                })

            }).done(function (msg) {

                $.notify({
                    icon: 'glyphicon glyphicon-ok-sign',
                    message: 'Schedule Stopped'
                },{
                    // settings
                    type: 'success',
                    placement: {
                        from: "top",
                        align: "right"
                    },
                    delay: 3000
                });

                $('span[data-schedule-id="' + id +'"].start-schedule').toggleClass('hide');
                $('span[data-schedule-id="' + id +'"].stop-schedule').parents('span').toggleClass('hide');

            }).fail(error);

        });

    };

    cleanId = function (id) {

        if (id && $.trim(id).length > 0) {
            return parseInt(id, 10);
        }

        return null;
    };

    success = function (msg) {

        $.notify({
            icon: 'glyphicon glyphicon-ok-sign',
            message: 'Schedule Saved!'
        },{
            // settings
            type: 'success',
            placement: {
                from: "top",
                align: "right"
            },
            delay: 3000
        });

        if (window.location.pathname === '/v2/schedules/new') {
            window.location.replace('/v2/schedules/' + msg.body.id);
        }
    };

    error = function (msg) {

        $.notify({
            icon: 'glyphicon glyphicon-warning-sign',
            message: 'There was an error: ' + msg.status
        },{
            // settings
            type: 'danger',
            placement: {
                from: "top",
                align: "right"
            },
            delay: 3000
        });
    };

    return {
        init: initialise
    }

}(jQuery));

var host = (function ($) {

    'use strict';

    var initialise, cleanId, success, error;

    initialise = function () {

        $('#saveHost').on('click', function () {

            $.notify({
                icon: 'glyphicon glyphicon-info-sign',
                message: 'Saving...'
            },{
                // settings
                type: 'info',
                placement: {
                    from: "top",
                    align: "right"
                },
                delay: 3000
            });

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

            $.ajax({

                method: method,
                url: url,
                dataType: "json",
                contentType: 'application/json',
                data: JSON.stringify(postData)

            }).done(success).fail(error);
        });

        $('#deleteHost').on('click', function () {

            $.ajax({
                method: 'DELETE',
                url: '/api/v2/host/' + $('#id').val()
            }).done(function (msg) {
                window.location.replace('/v2/hosts');
            }).fail(error);
        });
    };

    success = function (msg) {

        $.notify({
            icon: 'glyphicon glyphicon-ok-sign',
            message: 'Host Saved!'
        },{
            // settings
            type: 'success',
            placement: {
                from: "top",
                align: "right"
            },
            delay: 3000
        });

        if (window.location.pathname === '/v2/hosts/new') {
            window.location.replace('/v2/hosts/' + msg.body.id);
        }
    };

    error = function (msg) {

        $.notify({
            icon: 'glyphicon glyphicon-warning-sign',
            message: 'There was an error: ' + msg.status
        },{
            // settings
            type: 'danger',
            placement: {
                from: "top",
                align: "right"
            },
            delay: 3000
        });
    };

    cleanId = function (id) {

        if (id && $.trim(id).length > 0) {
            return parseInt(id, 10);
        }

        return null;
    };

    return {
        init: initialise
    }

}(jQuery))

jQuery(document).ready(host.init);
jQuery(document).ready(schedule.init);
jQuery(document).ready(fragments.init);
jQuery(document).ready(edit.init);
jQuery(document).ready(settings.init);
