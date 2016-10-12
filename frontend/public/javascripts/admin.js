/**
 * JavaScript code for the admin.scala.html view.
 *
 * @author Wouter Pinnoo
 * @author Gilles Vandewiele
 * @author Steven De Blieck
 * @author Stijn Seghers
 */

$(document).ready(function() {
    // Toggle on click for all lists
    listToggleConfig();

    // Button callback to change the permissions of selected users
    $('#changePermissions').click(changePermissionsCallback);

    // Button callback to preview a template in a modal
    $('#previewTemplateButton').click(templatePreviewCallback);

    // Table row click callback for the template table
    $('#templatesTable').on('click', 'tbody tr', templateTableRowCallback);

    // Editor configuration
    editorConfig();

    // Price range configuration
    priceRangeConfig();

    document.getElementById("contractCheckbox").checked = false;
    document.getElementById("waarborgCheckbox").checked = false;
    document.getElementById("contractCheckboxOwner").checked = false;
    document.getElementById("waarborgCheckboxOwner").checked = false;
    document.getElementById("submitUser").disabled = true;
    document.getElementById("submitOwner").disabled = true;

    $('.addVariable' ).click(function(){
        $('#editor' ).html($('#editor' ).html() + '{{' + $(this).text() + '}}');
    });

    $('.registrationCheckbox').click(function(){
        var checkbox1_user = document.getElementById("contractCheckbox");
        var checkbox2_user = document.getElementById("waarborgCheckbox");
        var checkbox1_owner = document.getElementById("contractCheckboxOwner");
        var checkbox2_owner = document.getElementById("waarborgCheckboxOwner");
        var submit_button_user = document.getElementById("submitUser");
        var submit_button_owner = document.getElementById("submitOwner");

        if(checkbox1_user.checked == true && checkbox2_user.checked == true){
            submit_button_user.disabled = false;
        } else {
            submit_button_user.disabled = true;
        }

        if(checkbox1_owner.checked == true && checkbox2_owner.checked == true){
            submit_button_owner.disabled = false;
        } else {
            submit_button_owner.disabled = true;
        }
    });

});

var listToggleConfig = function() {
    var toggle = function($title, $list, $icon) {
        $title.click(function() {
            $list.slideToggle('slow');
            $icon.toggleClass('glyphicon-plus-sign glyphicon-minus-sign');
        });
    };

    toggle($('#showCarsTable'), $('#PendingCars'), $('#carIcon'));
    toggle($('#showCostTable'), $('#PendingCosts'), $('#costIcon'));
    toggle($('#showRefuelingTable'), $('#PendingRefuelings'), $('#fuelIcon'));
}

var changePermissionsCallback = function(event) {
    var userIdOfFirstSelected = parseInt($('[name=user]:checked').val());
    javascriptRoutes.controllers.Admin.getUserPermissions(userIdOfFirstSelected).ajax({
        success: function(doc) {
            // Select every granted permission
            $(doc).find('permission').each(function() {
                var permissionName = $(this).attr('name');
                $('[id="' + permissionName + '"]').click();
            });
            // Show modal
            $('#permissionsmodal').modal('show');
            // Add hidden field with the user id
            var content = '<input type="hidden" name="userid" value="' + userIdOfFirstSelected + '">';
            $('#permissionsmodalbody').html(content);
        },
        error: function(xhr, status, error) {
            // TODO gebruik flash-bericht
            alert('Er heeft zich een fout voorgedaan: ' + error);
        }
    });
};

var templatePreviewCallback = function(event) {
    var content = $('#editor').html();
    $.ajax({
        type: "POST",
        url: previewTemplateUrl,
        data: { content: content },
        cache: false,
        success: function(html){
            // Hack find previewContent at the time the function is executed, not at load time.
            $('#previewtemplate').find('#previewContent').html(html);
            $('#previewtemplate' ).modal('show');
        }
    });
};

var templateTableRowCallback = function(event) {
    $('#editTemplateContainer').show();
    $(this).addClass('highlight').siblings().removeClass('highlight');

    // Update (hidden) input fields of the editor for the selected template
    var id = $(this).children('td').eq(0).text();
    var content = $(this).children('td').eq(1).html();
    var name = $(this).children('td').eq(2).text();
    $('input[name="templateid"]').val(id);
    $('input[name="templatename"]').val(name);
    $('#editor').html(content);

    // When submitting a template, copy the content of the editor to the hidden
    // input field (since the editor has to be a div, not a <input>)
    $('#template-submit').click(function() {
        var editorContent = $('#editor').html();
        $('#hidden-editor').val(editorContent);
    });

    // Content of delete modal
    $('#deleteTemplateName').text(name);

    // Hidden input field for the delete modal
    $('input[name="deleteTemplateId"]').val(id);
};

var editorConfig = function() {
    function initToolbarBootstrapBindings() {
        var fonts = ['Serif', 'Sans', 'Arial', 'Arial Black', 'Courier',
                'Courier New', 'Comic Sans MS', 'Helvetica', 'Impact',
                'Lucida Grande', 'Lucida Sans', 'Tahoma', 'Times',
                'Times New Roman', 'Verdana'
            ],
            fontTarget = $('[title=Font]').siblings('.dropdown-menu');
        $.each(fonts, function (idx, fontName) {
            fontTarget.append($('<li><a data-edit="fontName ' + fontName + '" style="font-family:\'' + fontName + '\'">' + fontName + '</a></li>'));
        });
        $('a[title]').tooltip({
            container: 'body'
        });
        $('.dropdown-menu input').click(function () {
            return false;
        })
            .change(function () {
                $(this).parent('.dropdown-menu').siblings('.dropdown-toggle').dropdown('toggle');
            })
            .keydown('esc', function () {
                this.value = '';
                $(this).change();
            });

        $('[data-role=magic-overlay]').each(function () {
            var overlay = $(this),
                target = $(overlay.data('target'));
            overlay.css('opacity', 0).css('position', 'absolute').offset(target.offset()).width(target.outerWidth()).height(target.outerHeight());
        });
        if ("onwebkitspeechchange" in document.createElement("input")) {
            var editorOffset = $('#editor').offset();
            $('#voiceBtn').css('position', 'absolute').offset({
                top: editorOffset.top,
                left: editorOffset.left + $('#editor').innerWidth() - 35
            });
        } else {
            $('#voiceBtn').hide();
        }
    };
    initToolbarBootstrapBindings();

    $('#editor').wysiwyg({
        fileUploadError: function(reason, detail) {
            var msg;
            if (reason === 'unsupported-file-type') {
                msg = "Unsupported format: " + detail;
            } else {
                msg = reason + ": " + detail;
            }
            $('<div class="alert"> <button type="button" class="close" data-dismiss="alert">&times;</button>' +
                '<strong>File upload error</strong> ' + msg + ' </div>').prependTo('#alerts');
        }
    });

    window.prettyPrint && prettyPrint();
};

var priceRangeConfig = function() {
    $('.minimum').editable({url: updatePriceRangeUrl});
    $('.maximum').editable({url: updatePriceRangeUrl});
    $('.price').editable({url: updatePriceRangeUrl});
};

