/**
 * JavaScript code for the carmanagement.scala.html view.
 *
 * @author Wouter Pinnoo
 * @author Stijn Seghers
 */

$(document).ready(function() {
    // Toggle on click for all lists
    listToggleConfig();
});

var listToggleConfig = function() {
    var toggle = function($title, $list, $icon) {
        $title.click(function() {
            $list.slideToggle('slow');
            $icon.toggleClass('glyphicon-plus-sign glyphicon-minus-sign');
        });
    };

    toggle($('#showRejResTable'), $('#rejectedReservationsTable'), $('#rejIcon'));
    toggle($('#showAccResTable'), $('#acceptedReservationsTable'), $('#accIcon'));
    toggle($('#showPendingResTable'), $('#pendingReservationsTable'), $('#resvIcon'));
    toggle($('#showAcceptedRidesTable'), $('#acceptedRidesTable'), $('#accRideIcon'));
    toggle($('#showPendingRidesTable'), $('#pendingRidesTable'), $('#penRideIcon'));
    toggle($('#showRejectedRidesTable'), $('#rejectedRidesTable'), $('#rejRideIcon'));
    toggle($('#showPrevResTable'),$('#previousReservationTable'),$('#previousResIcon'));

};