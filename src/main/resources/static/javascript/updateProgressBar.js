const outerDiv = document.getElementById("card-time-spent");

function updateProgress(progressBar, value, max) {
    const percentage = max > 0 ? Math.round((value / max) * 100) : 0;
    progressBar.style.width = percentage + '%';
    progressBar.textContent = percentage + '%';
}

function getAllTickets() {
    const url = "/tickets/";
    fetch(url, { method: "GET" })
        .then(response => {
            if (!response.ok) throw new Error('Network response was not ok');
            return response.json();
        })
        .then(data => {
            let articles = "";
            data.forEach(ticket => {
                articles +=
                    '<div class="container-fluid">' +
                    '<p>' + ticket.title + '</p>' +
                    '<div class="progress" role="progressbar" aria-label="Ticket Progress" aria-valuenow="' + ticket.time_spent + '" aria-valuemin="0" aria-valuemax="' + ticket.estimate + '">' +
                    '<div class="progress-bar" style="width: ' + ((ticket.time_spent / ticket.estimate) * 100) + '%">' +
                    Math.round((ticket.time_spent / ticket.estimate) * 100) + '%' +
                    '</div>' +
                    '</div>' +
                    '</div><br>';
            });

            outerDiv.innerHTML = articles;
        })
        .catch(error => {
            console.error("Error fetching data: ", error.message);
        });
}

getAllTickets();
