/**
 * JavaScript for the reserve.scala.html view.
 *
 * @author Stijn Seghers
 * @author Wouter Pinnoo
 */

var STATE = {
    AVAILABLE: {style: 'bg-success'},
    UNKNOWN: {style: 'bg-warning'},
    UNAVAILABLE: {style: 'bg-danger'},
};

var selectedStartDate, selectedEndDate,
    map = {};

/**
 * Initializes all event handlers and dynamic content.
 */
$(document).ready(function() {
    calendarConfig();

    spinnerConfig();

    timepickerConfig();

    $('#ridesTable').on('click', 'tbody tr', rideClickCallback);

    fileUploadConfig();

    $('#carSearchTable').on('click', 'tbody tr', searchCarClickCallback);

    $('#carsTable').on('click', 'tbody tr', carClickCallback);

    previousReservationsConfig();

    /* Dynamicly add id's for use in the Selenium tests */
    $($('td')[14]).attr('id', 'test-day1');
    $($('td')[16]).attr('id', 'test-day2');
});

/**
 * Configuration of the calendar plugin. This includes a rendering callback, an
 * AJAX call to get the existing reservations, a selection callback and Dutch
 * translations and time formats. For more information, see
 * http://arshaw.com/fullcalendar/docs/
 */
var calendarConfig = function() {
    customMonthNames = ['januari', 'februari', 'maart', 'april', 'mei', 'juni',
        'juli', 'augustus', 'september', 'oktober', 'november', 'december'];

    $('#calendar').fullCalendar({
        header: {
            left: 'prev,next today',
            center: 'title',
            right: ''
        },
        firstDay: 1,
        aspectRatio: 1.5,
        allDaySlot: false,
        axisFormat: "H'u'(mm)",
        timeFormat: "H'u'(mm)",
        columnFormat: {
            week: 'ddd d/M',
            month: 'ddd',
        },
        titleFormat: {
            week: 'd MMM yyyy',
            month: 'MMMM yyyy',
        },
        buttonText: {
            prev:     '&lsaquo;',
            next:     '&rsaquo;',
            today:    'vandaag',
            day:      'dag',
            week:     'week',
            month:    'maand',
        },
        monthNames: customMonthNames,
        monthNamesShort: ['jan', 'feb', 'mrt', 'apr', 'mei', 'jun', 'jul', 'aug', 'sep', 'okt', 'nov', 'dec'],
        dayNames: ['zondag', 'maandag', 'dinsdag', 'woensdag', 'donderdag', 'vrijdag', 'zaterdag'],
        dayNamesShort: ['zo', 'ma', 'di', 'wo', 'do', 'vr', 'za'],
        allDayDefault: false,
        selectable: true,
        events: eventGenerator,
        dayRender: function(date, cell) {
            cell.addClass(getAvailability(date).style);
        },
        select: selectCallback,
    });
};

/**
 * Callback handler for the selection of files (refueling proofs and damage
 * proofs). This shows information of the selection in the text field besides
 * the file selector. In case of selection of one file, it shows the file name;
 * in case there are more selected, it shows how many files are selected.
 */
var fileUploadConfig = function() {
    $('.btn-file :file').change(function(event) {
        var numFiles = $(this).get(0).files ? $(this).get(0).files.length : 1,
            label = $(this).val().replace(/\\/g, '/').replace(/.*\//, ''),
            $text_field = $(this).parents('.input-group').find(':text'),
            text = numFiles > 1 ? numFiles + ' bestanden' : label;

        $text_field.val(text);
    });
};

/**
 * Configuration of the duration spinner. This is pure jQuery, no plugin is
 * used.
 */
var spinnerConfig = function() {
    // Click handler for up button (days)
    $('#days .btn:first-of-type').click(function() {
        $('#days input').val(parseInt($('#days input').val()) + 1);
        return false;
    });

    // Click handler for down button (days)
    $('#days .btn:last-of-type').click(function() {
        var days = parseInt($('#days input').val());
        if (days > 0) {
            $('#days input').val(days - 1);
        }
        return false;
    });

    // Click handler for up button (hours)
    $('#hours .btn:first-of-type').click(function() {
        var hours = parseInt($('#hours input').val());
        if (hours < 23) {
            $('#hours input').val(hours + 1);
        } else {
            $('#hours input').val(0);
            $('#days input').val(parseInt($('#days input').val()) + 1);
        }
        return false;
    });

    // Click handler for down button (hours)
    $('#hours .btn:last-of-type').click(function() {
        var hours = parseInt($('#hours input').val());
        if (hours > 1) {
            $('#hours input').val(hours - 1);
        }
        return false;
    });

    var validateDaySpinner = function() {
        var days = parseInt($(this).val());
        if ($(this).val() != "") {
            if (isNaN(days) || value < 0) {
                $(this).val(0);
            }
        }
    };

    var validateHourSpinner = function() {
        var hours = parseInt($(this).val());
        var days = parseInt($('#days input').val());
        if ($(this).val() != "") {
            if (isNaN(hours) || hours < 0) {
                $(this).val(0);
            } else if (hours < 1 && days < 1) {
                $(this).val(1);
            } else if (hours > 23) {
                $(this).val(23);
            }
        }
    };

    // Validate day and hour input on keystroke and focus events
    $('#days input').keyup(validateDaySpinner);
    $('#days input').focus(validateDaySpinner);
    $('#days input').focusout(validateDaySpinner);
    $('#hours input').keyup(validateHourSpinner);
    $('#hours input').focus(validateHourSpinner);
    $('#hours input').focusout(validateHourSpinner);
};

/**
 * Configuration of the timepicker that shows when a period is selected.
 * TODO: use the timetable of the car owner.
 */
var timepickerConfig = function() {
    if ($('#picker-start').length > 0) {
        var startPicker = $('#picker-start').datetimepicker({
            datepicker: false,
            format: 'H:i',
            value: '13:00',
            step: 30,
            maxTime: '20:30'
        });
        var endPicker = $('#picker-end').datetimepicker({
            datepicker: false,
            format: 'H:i',
            value: '20:30',
            step: 30,
            minTime: '13:00'
        });
        startPicker.data('xdsoft_datetimepicker').setOptions({
            onChangeDateTime: function(currentDateTime) {
                var options = {minTime: startPicker.val()};
                if (parseTime(startPicker) > parseTime(endPicker))
                    options['value'] = startPicker.val();
                else
                    options['value'] = endPicker.val();
                endPicker.data('xdsoft_datetimepicker').setOptions(options);
            }
        });
        endPicker.data('xdsoft_datetimepicker').setOptions({
            onChangeDateTime: function(currentDateTime) {
                var options = {maxTime: endPicker.val()};
                if (parseTime(startPicker) > parseTime(endPicker))
                    options['value'] = endPicker.val();
                else
                    options['value'] = startPicker.val();
                startPicker.data('xdsoft_datetimepicker').setOptions(options);
            }
        });
    }
};

/**
 * Callback function for a click on a car. A popup with the car info is
 * dynamically created and shown.
 */
var carClickCallback = function(event) {
    var carId = $(this).children('td').eq(0).text();
    $('input[name="carModalCarId"]').val(carId);

    $('#carModalCarName').text(map[carId]['name']);
    $('#carModalCarOwner').text(map[carId]['owner']);
    $('#carModalCarZone').text(map[carId]['zone']);
    $('#carModalCarAddress').text(map[carId]['address']);
    $('#carModalCarModel').text(map[carId]['model']);
    $('#carModalCarType').text(map[carId]['type']);
    $('#carModalCarDescription').text(map[carId]['description']);
    $('#carModalCarFuel').text(map[carId]['fuel']);
    $('#carModalCarDoors').text(map[carId]['doors']);
    $('#carModalCarCapacity').text(map[carId]['capacity']);
    $('#carModalCarTow').text(map[carId]['tow']);
    $('#carModalCarGps').text(map[carId]['gps']);
    $('#carModalCarYear').text(map[carId]['year']);

    var begin = new Date(parseInt($('#try_begin' + carId).text()));
    var end = new Date(parseInt($('#try_end' + carId).text()));
   
    $('#carModalTimeStart').text(('00' + begin.getHours()).slice(-2) + ':' + ('00' + begin.getMinutes()).slice(-2)).val();
    $('#carModalDateStart').text(begin.getDate() + '/' + begin.getMonth() + '/' + begin.getFullYear()).val();
    $('input[name="carModalStart"]').val(begin.getTime());

    $('#carModalTimeEnd').text(('00' + end.getHours()).slice(-2) + ':' + ('00' + end.getMinutes()).slice(-2)).val();
    $('#carModalDateEnd').text(end.getDate() + '/' + end.getMonth() + '/' + end.getFullYear()).val();
    $('input[name="carModalEnd"]').val(end.getTime());

    $('#carModal').modal('show');
};

/**
 * Callback function for searching cars within a given time range. An AJAX call
 * is made to get the available cars and a list is dynamically created.
 */
var submitTimePicker = function() {
    var start = selectedStartDate.getTime() + parseTime($('#picker-start')),
        end = selectedEndDate.getTime() + parseTime($('#picker-end')),
        carFilterString = $('#filter').serialize();

    javascriptRoutes.controllers.Reserve.getAvailableCars(start, end, carFilterString).ajax({
        success: function(doc) {
            var content = '';
            $(doc).find('car').each(function() {
                content += '<tr>';
                content += '<td style="display: none" id="car_id">' + $(this).attr('id') + '</td>';
                content += '<td style="display: none" id="try_begin' + $(this).attr('id') + '">' + $(this).children('try-begin').text() + '</td>';
                content += '<td style="display: none" id="try_end' + $(this).attr('id') + '">' + $(this).children('try-end').text() + '</td>';
                content += '<td id="car_name">' + $(this).children('name').text() + '</td>';
                
                var begin = new Date(parseInt($(this).children('try-begin').text()));
                var end = new Date(parseInt($(this).children('try-end').text()));
                content += '<td>' + begin.getDate() + '/' + begin.getMonth() + '/' + begin.getFullYear() + ' ' + ('00' + begin.getHours()).slice(-2) + ':' + ('00' + begin.getMinutes()).slice(-2) + '</td>';
                content += '<td>' + end.getDate() + '/' + end.getMonth() + '/' + end.getFullYear() + ' ' + ('00' + end.getHours()).slice(-2) + ':' + ('00' + end.getMinutes()).slice(-2) + '</td>';
                content += '<td><button type="button" class="btn btn-default">Bekijk</button></td>';
                content += '</tr>';

                var user = $(this).children('owner').children('user');

                //console.log($(this));
                map[$(this).attr('id')] = {
                    'id': $(this).attr('id'),
                    'name': $(this).children('name').text(),
                    'owner': user.children('name').text() + ' ' + user.children('surname').text() + (user.children('phone').text() != "" ? ' (' + user.children('phone').text() + ')' : ''),
                    'zone': $(this).children('zone').text(),
                    'address': $(this).children('address').text(),
                    'model': $(this).children('model').text(),
                    'type': $(this).children('type').text(),
                    'description': $(this).children('description').text(),
                    'fuel': $(this).children('fuel').text(),
                    'doors': $(this).children('doors').text(),
                    'capacity': $(this).children('capacity').text(),
                    'tow': $(this).children('tow').text(),
                    'gps': $(this).children('gps').text(),
                    'year': $(this).children('year').text(),
                };
            });
            if (content == '') {
                content = '<tr><td colspan="10">Er konden geen auto\'s worden gevonden.</td></tr>';
            }
            $("#carTableBody").html(content);
            $('#carTableContainer').show();
        },
        error: function(xhr, status, error) {
            // TODO gebruik flash-bericht
            alert("Er heeft zich een fout voorgedaan: " + error);
        },
    });
};

/**
 * Config for the add-ride-modal
 */
var addRide = function() {
    $('#rideModal').modal('show');

    $('#ride-start').datetimepicker({
        format:'d/m/Y H:i',
        onChangeDateTime: function(dp,$input){
            $("input[name='ride-start']").val($input.val());
        }
    });

    var formattedDate = $.fullCalendar.formatDate(new Date(), 'dd/MM/yyyy HH:mm', {});
    $('#ride-start').val(formattedDate);
    $("input[name='ride-start']").val(formattedDate);

    $('#ride-end').datetimepicker({
        format:'d/m/Y H:i',
        onChangeDateTime: function(dp,$input){
            $("input[name='ride-end']").val($input.val());
        }
    });

    var formattedDate = $.fullCalendar.formatDate(new Date(), 'dd/MM/yyyy HH:mm', {});
    $('#ride-end').val(formattedDate);
    $("input[name='ride-end']").val(formattedDate);

    // Live Search
    function search() {
        var query_value = $('input#search').val();
        if(query_value !== ''){
            $.ajax(searchCarUrl, {
                type: "POST",
                data: { query: query_value },
                cache: false,
                success: function(html){
                    $("tbody#carSearchTableBody").html(html);
                }
            });
        }
        return false;
    }

    $("input#search").keyup(function(e) {
        clearTimeout($.data(this, 'timer'));
        if ($(this).val() != '') {
            $(this).data('timer', setTimeout(search, 100));
        };
    });
};

/**
 * Config for the add-ride-manual-modal
 */
var addRideManual = function() {
    $('#rideModalManual').modal('show');
};

/**
 * Click callback when searching for a car when adding ride data. This puts the
 * id of the selected car in a hidden input field.
 */
var searchCarClickCallback = function(event) {
    $(this).addClass('tablehighlight').siblings().removeClass('tablehighlight');
    var carId = $(this).children('td').eq(0).text();
    $('input[name="carId"]').val(carId);
};

/**
 * Callback function for showing information about a specific ride.
 */
var rideClickCallback = function(event) {
    $(this).addClass('tablehighlight').siblings().removeClass('tablehighlight');
    if ($(this).children('td').length > 1) {
        $('#rideDetailsContainer').show();
        var rideId = $(this).children('td').eq(0).text();

        $.fn.editable.defaults.mode = 'popup';

        $('#km-start').text(rideMap[rideId]["km-start"])
            .attr('data-pk', rideId)
            .editable({url: updateRideUrl, emptytext: '0'});
        $('#km-end').text(rideMap[rideId]["km-end"])
            .attr('data-pk', rideId)
            .editable({url: updateRideUrl, emptytext: '0'});

        $('#refuelingsTableBody').html('');
        for (var refuelingId in rideMap[rideId]["refuelings"]) {
            var r = rideMap[rideId]["refuelings"][refuelingId]
            var row = '<tr><td><a href="#" id="fueltype' + refuelingId + '" data-name="type" data-pk="' + refuelingId + '">' + r["type"] + '</a></td>'
                        + '<td><a href="#" id="fuellitre' + refuelingId + '" data-name="litre" data-pk="' + refuelingId + '">' + r["litre"] + '</a></td>'
                        + '<td><a href="#" id="fuelprice' + refuelingId + '" data-name="price" data-pk="' + refuelingId + '">' + r["price"] + '</a></td>'
                        + '<td>';

            if (r["proof"] != null && r["proof"] != "") {
                row += '<img class="img-responsive img-thumbnail" imgType="refuelingDoc" src="' + r["proof"] + '" id="' + refuelingId + '" style="max-width:50px; max-height:50px;">';
            } else {
                row += '<form id="extraRefuelingFileForm' + refuelingId + '" role="form" action="' + extraRefuelingFileUrl + '" method="post" enctype="multipart/form-data">'
                    + '<input type="hidden" name="id" value="' + refuelingId + '">'
                    + '<span class="input-group-btn">'
                    + '<button type="button" class="btn btn-default btn-file">'
                    + '    <span class="glyphicon glyphicon-upload"></span> Voeg toe<input onchange="javascript:this.form.submit();" type="file" id="extraRefuelingFileInput' + refuelingId + '" name="file" multiple="">'
                    + '</button></form>';
            }

            row += '</td>';

            row += '<td><form role="form" action="' + deleteRefuelingUrl + '" method="post">'
                + '<input type="hidden" name="refuelingId" value="' + refuelingId + '"/>'
                + '<button type="submit" class="btn" name="submit"><span class="glyphicon glyphicon-minus-sign"></span></button>'
                + '</form></td>';

            $('#refuelingsTableBody').append(row);
            $('#fueltype' + refuelingId).editable({url: updateRefuelingUrl, emptytext: 'Brandstof'});
            $('#fuellitre' + refuelingId).editable({url: updateRefuelingUrl, emptytext: '0'});
            $('#fuelprice' + refuelingId).editable({url: updateRefuelingUrl, emptytext: '0'});
        }

        $('input[name="newRefuelingRideId"]').val(rideId);

        $('#damagesTableBody').html('');
        for (var damageId in rideMap[rideId]["damages"]) {
            var damage = rideMap[rideId]["damages"][damageId]
            var url = javascriptRoutes.controllers.Reserve.damage(damageId).url;

            var row = '<tr><td><form action="' + url + '"><button class="btn btn-default" type="submit">Bekijk</button></form></td>'
                        + '<td><a href="#" id="damagetime' + damageId + '" data-type="combodate" data-name="time" data-pk="' + damageId + '">' + damage["time"] + '</a></td>'
                        + '<td><a href="#" id="damagedescription' + damageId + '" data-type="textarea" data-name="description" data-pk="' + damageId + '">' + damage["description"] + '</a></td>'
                        + '<td>';

            for (var docId in damage["docs"]) {
                row += '<img class="img-responsive img-thumbnail" imgType="damageDoc" src="' + damage["docs"][docId]["url"] + '" id="' + docId + '" style="max-width:50px; max-height:50px;">';
            }

            row += '<form id="extraDamageFileForm' + damageId + '" role="form" action="' + extraDamageFileUrl + '" method="post" enctype="multipart/form-data">'
                + '<input type="hidden" name="id" value="' + damageId + '">'
                + '<span class="input-group-btn">'
                + '<button type="button" class="btn btn-default btn-file">'
                + '    <span class="glyphicon glyphicon-upload"></span> Voeg toe<input onchange="javascript:this.form.submit();" type="file" id="extraDamageFileInput' + damageId + '" name="file" multiple="">'
                + '</button></form>';

            row += '</td>'
                + '<td><form role="form" action="' + deleteDamageUrl + '" method="post">'
                + '<input type="hidden" name="damageId" value="' + damageId + '"/>';

            for (var docId in damage["docs"]) {
                row +=  '<input type="hidden" name="damageDocId" value="' + docId + '"/>';
            }
            row +=  '<button type="submit" class="btn" name="submit"><span class="glyphicon glyphicon-minus-sign"></span></button>'
                + '</form></td>';

            $('#damagesTableBody').append(row);
            $('#damagetime' + damageId).editable({
                format: 'DD/MM/YYYY HH:mm',
                viewformat: 'DD/MM/YYYY HH:mm',
                template: 'DD / MMMM / YYYY   HH : mm',
                url: updateDamageUrl,
                combodate: {
                    minuteStep: 1
                }
            });
            $('#damagedescription' + damageId).editable({url: updateDamageUrl, emptytext: 'Beschrijving'});
        }
        $('input[name="newDamageRideId"]').val(rideId);

        $('#damage-date').datetimepicker({
            format:'d/m/Y H:i',
            onChangeDateTime: function(dp,$input){
                $("input[name='date-hidden']").val($input.val());
            },
            minDate: $.fullCalendar.formatDate(new Date(rideMap[rideId]["begin"]), 'dd/MM/yyyy', {}),
            maxDate: $.fullCalendar.formatDate(new Date(rideMap[rideId]["end"]), 'dd/MM/yyyy', {}),
            formatDate: 'd/m/Y'
        });

        var formattedDate = $.fullCalendar.formatDate(new Date(rideMap[rideId]["begin"]), 'dd/MM/yyyy HH:mm', {});
        $('#damage-date').val(formattedDate);
        $("input[name='date-hidden']").val(formattedDate);

        $('img').click(function() {
             var sr = $(this).attr('src');
             var id = $(this).attr('id');
             var type = $(this).attr('imgType');
             $('#picsModalButton').attr('href',sr);
             $("input[name='picsModalDeleteId']").val(id);
             $("input[name='picsModalDeleteType']").val(type);
             $('#picsModalImg').attr('src',sr);
             $('#picsModal').modal('show');
        });
    }
};

/**
 * Checks whether there are cars available for a specific date on the calendar.
 */
var getAvailability = function(date) {
    var dayStart = date.getTime(),
        nextDayStart = dayStart + 24*60*60*1000,
        durationDays = parseInt($('#days input').val()),
        durationHours = parseInt($('#hours input').val()),
        availableTo = 30000000000000, // Pretty far in the future
        dayOverlay = false;

    if (date < new Date()) {
        return STATE.UNAVAILABLE;
    }

    // Make sure this function returns decent answer, even if the duration
    // spinner has bogus input
    if (isNaN(durationDays))
        durationDays = 0;
    if (isNaN(durationHours))
        durationHours = 0;
    var durationMs = 3600000 * (durationDays * 24 + durationHours);

    // Check every unavailable moment, from end to start
    for (var i = unavailableMoments.length - 1; i >= 0; i--) {
        // If we've passed this day, stop iterating
        if (unavailableMoments[i].end <= dayStart)
            break;

        // If the unavailable moment has nothing to do with this day, continue
        if (unavailableMoments[i].start >= nextDayStart) {
            // Remember the end of the available period
            availableTo = unavailableMoments[i].start;

            continue;
        }

        // Now we now this day overlays with an unavailable moment
        dayOverlay = true;

        // If there the whole day is in the unavailable period, it's
        // unavailable
        if (unavailableMoments[i].start <= dayStart &&
                unavailableMoments[i].end >= nextDayStart)
            return STATE.UNAVAILABLE;

        // If there's a time this day from which there's an available period of
        // minimum the required duration, then this day is available
        if (unavailableMoments[i].end + durationMs <= availableTo)
            return STATE.AVAILABLE;

        // Remember the end of the available period
        availableTo = unavailableMoments[i].end
    };

    // If there was an overlay with some unavailableMoment, there isn't an
    // available moment, else there is
    return dayOverlay ? STATE.UNAVAILABLE : STATE.AVAILABLE;
};

/**
 * Callback function to get a list of reservations the user made earlier. This
 * is done in an AJAX call. The fullcalendar plugin requests that the given
 * function "callback" is called once the reservations are fetched.
 */
var eventGenerator = function(start, end, callback) {
    javascriptRoutes.controllers.Reserve.getReservations(start.getTime() / 1000, end.getTime() / 1000).ajax({
        success: function(doc) {
            var events = [];
            $(doc).find('reservation').each(function() {
                events.push({
                    title: $(this).attr('title'),
                    start: $(this).attr('start'),
                    end: $(this).attr('end'),
                    url: javascriptRoutes.controllers.Reserve.index("my-reservations").url,
                });
            });
            callback(events);
        },
        error: function(xhr, status, error) {
            // TODO gebruik flash-bericht
            alert("Er heeft zich een fout voorgedaan: " + error);
        },
    });
};

/**
 * Callback function that is called when a period is selected in the calendar.
 * A new pane is shown where the user can select a specific time for the start
 * and end.
 */
var selectCallback = function(startDate, endDate, allDay, jsEvent, view) {
    // Set the date in the confirmation box
    options = { monthNames: customMonthNames };

    if (startDate < new Date()) {
        $('#invalidSelection').show();
        $('#confirmation').hide();
    } else {
        $('#invalidSelection').hide();
        $('#confirmation').show();

        $('#confirmation-start-date').text($.fullCalendar.formatDate(startDate, 'dd MMMM', options));
        $('#confirmation-end-date').text($.fullCalendar.formatDate(endDate, 'dd MMMM', options));
        $('#carModalDateStart').text($.fullCalendar.formatDate(startDate, 'dd MMMM', options));
        $('#carModalDateEnd').text($.fullCalendar.formatDate(endDate, 'dd MMMM', options));
    }

    // Remember the selected start and end date in a global variable
    selectedStartDate = startDate;
    selectedEndDate = endDate;

    $('html, body').animate({
        scrollTop: $("#confirmation").offset().top
    }, 2000);
};

/**
 * Click handler for previous reservation list
 */
var previousReservationsConfig = function() {
    $('#showPrevResv').click(function() {
        $('#prevReservations').slideToggle("slow");
        $('#prevResIcon').toggleClass('glyphicon-minus-sign glyphicon-plus-sign');
    });
}

/**
 * Helper function to parse the time from an input field to an amount in
 * milliseconds since the epoch.
 */
var parseTime = function($input_object) {
    var time_array = $input_object.val().split(':');
    return 60000 * (parseInt(time_array[0]) * 60 + parseInt(time_array[1]));
}
