/**
 * Contains the JavaScript for notifications.
 */

$(document).ready(function() {
    $('#notificationsTable').on('click', 'tbody tr', notificationClickCallback);
});

var notificationClickCallback = function(event) {
    // Let the notification container appear
    $('#showNotificationContainer').show();

    // Update (hidden) input fields of the editor for the selected notification
    var time = $($(this).children()[1]).text();
    var subject = $($(this).children()[2]).html();
    var content = $($(this).children()[3]).html();
    $('#notificationTime').html(time);
    $('#notificationSubject').html(subject);
    $('#notificationContent').html(content);

    // Mark the message as read in the database and, if successful, also in
    // the view (list and menu).
    var id = parseInt($(this).attr('data-id'));
    var $message_pic = $(this).find('td img');
    javascriptRoutes.controllers.Notifications.read(id).ajax({
        type: 'POST',
        success: function(data) {
            var read_pic_url = javascriptRoutes.controllers.Assets.at("images/message_read.png").url;
            // Check whether the message already has a "read message" pic
            if ($message_pic.attr('src') != read_pic_url) {
                // Set the pic to a read message
                $message_pic.attr('src', read_pic_url);
                // Set the notification count in the menu bar
                var current_notification_count = parseInt($('#notification-count').text());
                if (current_notification_count > 1) {
                    $('#notification-count').text(current_notification_count - 1);
                } else {
                    $('#menu-notifications').text('Notificaties');
                }
            }
        },
        error: function(xhr, status, error) {
            // TODO use flash message
            alert("Er heeft zich een fout voorgedaan: " + error);
        },
    });
};
