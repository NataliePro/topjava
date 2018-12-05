const ajaxUrl = "ajax/admin/users/";
let datatableApi;

function setEnabled(id, enabled) {
    $.ajax({
        type: "PUT",
        url: ajaxUrl + id + "?enabled=" + enabled,
    }).done(function () {
        let color = enabled ? "green" : "gray";
        $('tr[id=' + id + ']').css("color", color);
        successNoty(enabled ? "Enabled" : "Disabled");
    }).fail(failNoty("Failed to set "+ (enabled ? "enabled" : "disabled")));
}

function updateTable() {
    $.ajax({
        type: "GET",
        url: ajaxUrl,
    }).done(drawTable);
}

// $(document).ready(function () {
$(function () {
    datatableApi = $("#datatable").DataTable({
        "paging": false,
        "info": true,
        "columns": [
            {
                "data": "name"
            },
            {
                "data": "email"
            },
            {
                "data": "roles"
            },
            {
                "data": "enabled"
            },
            {
                "data": "registered"
            },
            {
                "defaultContent": "Edit",
                "orderable": false
            },
            {
                "defaultContent": "Delete",
                "orderable": false
            }
        ],
        "order": [
            [
                0,
                "asc"
            ]
        ]
    });
    makeEditable();
});