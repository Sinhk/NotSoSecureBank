function initTable(table, email) {
    $.ajax({
        url: 'webresources/user/' + email + '/transaction',
        type: 'GET',
        dataType: 'json',
        success: function(data) {
            var balance = 0;
            for(i=0; i<data.length; i++) {
                var inAmount='', outAmount='', subject;
                if(data[i].fromEmail===email) {
                    subject = data[i].toEmail;
                    outAmount = data[i].amount;
                    balance -= data[i].amount;
                } else {
                    subject = data[i].fromEmail;
                    inAmount = data[i].amount;
                    balance += data[i].amount;
                }
                var date = new Date(data[i].transactionTime);
                table.row.add([
                    date.toLocaleDateString() + ' ' + date.toLocaleTimeString(),
                    subject,
                    data[i].text,
                    inAmount,
                    outAmount
                ]);
                table.draw();
            }
            $('#balance').html(balance);
        },
        error: function() {
            window.location.href = "error.html";
        }
    });
}

$(document).ready(function() {
    var t = $('#transactions').DataTable();

    getLoggedOnUser(function(data) {
        $("#logout").html(data.firstName + " " + data.lastName + ": logg ut");
        initTable(t, data.email);
    });
});