const ajaxUrl = "ajax/admin/users/";
let datatableApi;

function setEnabled(id, enabled) {
    $.ajax({
        type: "PUT",
        url: ajaxUrl + id + "?enabled=" + enabled,
    }).done(function () {
        updateTable();
        successNoty("Updated");
    });
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