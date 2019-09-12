jQuery(document).ready(function () {
    jQuery(".menu-toggle").click(function (e) {
        e.preventDefault();
        $("#wrapper").toggleClass("toggled");
    });

    jQuery('[data-for-page]').click(function () {
        hideAll();
        showPage(jQuery(this).attr('data-for-page'))
    })

    hideAll();
    showPage('page1')

})

function hideAll() {
    jQuery('.page').each(function () {
        jQuery(this).hide();
    })
}

function showPage(page) {
    jQuery('#' + page).show();
}

jQuery('body').on('click', '.removeApp', function () {
    var table = jQuery("#apps-table")
    var row = jQuery(this).parent().parent()
    var _deviceKey = row.find('.appToken').html().trim()
    var data = {
        appToken: _deviceKey
    }
    var token = jQuery("meta[name='_csrf']").attr("content");
    jQuery.ajax({
        url: 'account/app/remove',
        type: 'POST',
        dataType: 'json',
        contentType: 'application/json; charset=utf-8',
        headers: { "X-CSRF-TOKEN": token },
        data: JSON.stringify(data),
        success: function (data) {
            var success = data['success'];
            if (success != true) {
                alert(getErrorMsg(data['data']));
                return;
            }
            row.remove()
            resetCount(table)
        }
    })
})

function resetCount(context) {
    var count = 1;
    jQuery(context).find('.appCount').each(function () {
        jQuery(this).html(count);
        count++;
    })
}

jQuery("#btnAddAppName").click(function () {
    var table = jQuery("#apps-table")
    var _deviceKey = jQuery("#appNameInput").val()
    var data = {
        appName: _deviceKey
    }
    var token = jQuery("meta[name='_csrf']").attr("content");
    var count = jQuery('#apps-table tbody tr').length + 1
    jQuery.ajax({
        url: 'account/app/add',
        type: 'POST',
        dataType: 'json',
        contentType: 'application/json; charset=utf-8',
        headers: { "X-CSRF-TOKEN": token },
        data: JSON.stringify(data),
        success: function (data) {
            var success = data['success'];
            if (success != true) {
                alert(getErrorMsg(data['data']));
                return;
            }
            var app = data['data'];
            debugger
            jQuery("#appNameInput").val('')
            table.append("<tr><td class=\"appCount\"> " + count + "</td><td class=\"appToken\"> " + app['token'] + "</td><td>"+app['name']+"</td><td>0</td><td>0</td><td>0</td><td>0</td><td> <button class=\"btn btn-danger removeApp\">Remove App</button></td></tr>");
        }
    })
})

function getErrorMsg(data){
    var str = "";
    for(var i in data){
        str += data[i]
    }
    return str;
}