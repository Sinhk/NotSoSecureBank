getLoggedOnUser(function(data) {
    $("#logout").html(data.firstName + " " + data.lastName + ": logg ut");
});