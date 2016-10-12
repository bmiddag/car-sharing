/**
 * JavaScript code for the carmanagementsearchdamages.scala.html view.
 *
 * @author Wouter Pinnoo
 * @author Stijn Seghers
 */

$(document).ready(function() {
    // Live search for damages
    damageSearchConfig();
});

var damageSearchConfig = function() {
    function search() {
        var query_value = $('input#search').val();
        $.ajax(searchDamageUrl, {
            type: "POST",
            data: { query: query_value },
            cache: false,
            success: function(html){
                var content = '';
                $(html).find('damage').each(function() {
                    content += '<tr>';
                    content += '<td style="display: none" id="damage_id">' + $(this).attr('id') + '</td>';
                    content += '<td id="damage_status">' + $(this).children('status').text() + '</td>';
                    content += '<td id="damage_car_name">' + $(this).children('car').children('name').text() + '</td>';

                    var user = $(this).children('user');
                    var userStr = user.children('name').text() + ' ' + user.children('surname').text();

                    content += '<td id="damage_user">' + userStr + '</td>';

                    d = new Date(parseInt($(this).children('time').text()));
                    dateStr = d.getDate() + '/' + (d.getMonth() + 1) + '/' + d.getFullYear();
                    content += '<td id="damage_date">' + dateStr + '</td>';
                    content += '<td><button type="button" class="btn btn-default">Bekijk</button></td>';
                    content += '</tr>';
                });
                if (content == '') {
                    content = '<tr><td colspan="10">Er konden geen schadegevallen worden gevonden.</td></tr>';
                }
                $("#damageSearchTableBody").html(content);
            },
            error: function(xhr, status, error) {
                // TODO gebruik flash-bericht
                alert("Er heeft zich een fout voorgedaan: " + error);
            }
        });
        return false;
    }
    search();

    $("input#search").keyup(function(e) {
        clearTimeout($.data(this, 'timer'));
        $(this).data('timer', setTimeout(search, 100));
    });

    $('#damageSearchTable').on('click', 'tbody tr', function(event) {
        $(this).addClass('highlight').siblings().removeClass('highlight');
        var damageId = $(this).children('td').eq(0).text();
        window.location = javascriptRoutes.controllers.CarManagement.carManagementDamage(damageId).url;
    });
};
