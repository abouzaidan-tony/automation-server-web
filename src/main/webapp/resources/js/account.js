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

jQuery('body').on('click', '.removeDevice', function () {
    var table = jQuery("#devices-table")
    var row = jQuery(this).parent().parent()
    var _deviceKey = row.find('.deviceKey').html().trim()
    var data = {
        deviceKey: _deviceKey
    }
    var token = jQuery("meta[name='_csrf']").attr("content");
    jQuery.ajax({
        url: 'account/device/remove',
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

jQuery('body').on('click', '.removeUser', function () {
    var table = jQuery("#users-table")
    var row = jQuery(this).parent().parent()
    var _deviceKey = row.find('.deviceKey').html().trim()
    var data = {
        deviceKey: _deviceKey
    }
    var token = jQuery("meta[name='_csrf']").attr("content");
    jQuery.ajax({
        url: 'account/user/remove',
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
    jQuery(context).find('.deviceCount').each(function () {
        jQuery(this).html(count);
        count++;
    })
}

jQuery("#btnAddDeviceKey").click(function () {
    var table = jQuery("#devices-table")
    var _deviceKey = jQuery("#deviceKeyInput").val()
    var data = {
        deviceKey: _deviceKey
    }
    var token = jQuery("meta[name='_csrf']").attr("content");
    var count = jQuery('#devices-table tbody tr').length + 1
    jQuery.ajax({
        url: 'account/device/add',
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
            table.append("<tr><td class=\"deviceCount\"> " + count + "</td><td class=\"deviceKey\"> " + _deviceKey + "</td><td><span class=\"dot red\"></span></td> <td> <button class=\"btn btn-danger removeDevice\">Remove Device</button></td></tr>");
        }
    })
})


jQuery("#btnAddUserKey").click(function () {
    var table = jQuery("#users-table")
    var _deviceKey = jQuery("#userKeyInput").val()
    var data = {
        deviceKey: _deviceKey
    }
    var token = jQuery("meta[name='_csrf']").attr("content");
    var count = jQuery('#users-table tbody tr').length + 1
    jQuery.ajax({
        url: 'account/user/add',
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
            table.append("<tr><td class=\"deviceCount\"> " + count + "</td><td class=\"deviceKey\"> " + _deviceKey + "</td><td><span class=\"dot red\"></span></td> <td> <button class=\"btn btn-danger removeUser\">Remove Use</button></td></tr>");
        }
    })
})

function getErrorMsg(data){
    var str = "";
    for(var i in data){
        str += data[i]+'\n'
    }
}