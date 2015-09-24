$(document).ready(function() {
    var loggedOnEmail;
    getLoggedOnUser(function(data) {
        loggedOnEmail = data.email;
        $("#logout").html(data.firstName + " " + data.lastName + ": logg ut");
    });

    // Betal
    $("#payForm").submit(function(event) {
        event.preventDefault();
        $.ajax({
            url: 'webresources/user/' + loggedOnEmail + '/transaction',
            type: 'POST',
            data: JSON.stringify({
                toEmail: $("#email").val(),
                fromEmail: loggedOnEmail,
                text: $("#description").val(),
                amount: $("#amount").val(),
                transactionTime: new Date().toJSON()
            }),
            contentType: 'application/json; charset=utf-8',
            success: function(data) {
                window.location.href="account.html";
            },
            error: function() {
                window.location.href = "error.html";
            }           
        });
    });
});