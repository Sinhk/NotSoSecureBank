var user;

$(document).ready(function() {
    getLoggedOnUser(function(data) {
        $("#logout").html(data.firstName + " " + data.lastName + ": logg ut");
        $("#email").html(data.email);
        $("#firstName").val(data.firstName);
        $("#lastName").val(data.lastName);
        user = data;
    });
      
    // Betal
    $("#userForm").submit(function(event) {
        event.preventDefault();
        $.ajax({
            url: 'webresources/user/' + user.email,
            type: 'PUT',
            data: JSON.stringify({
                email: user.email,
                firstName: $("#firstName").val(),
                lastName: $("#lastName").val(),
                password: user.password
            }),
            contentType: 'application/json; charset=utf-8',
            success: function() {
                location.reload();
            },
            error: function() {
                window.location.href = "error.html";
            }           
        });
    });
});