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

jQuery(document).ready(fragments.init);
jQuery(document).ready(edit.init);
