const url = "/ticket/watching"


function checkTicketWatching()
{
    fetch(url, {method:"GET"})
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            const idTicket = getTicketId().toString();
            console.log(Math.max());
            data.forEach(ticket => {

                if(ticket.id == idTicket)
                {
                    changeWatchButton();
                }
            });
        })
        .catch(error => {
            console.error("Error fetching data: ", error);
        })
}

function getTicketId() {
    // Get the current URL
    var currentUrl = window.location.href;

    // Use a regular expression to extract the ticket ID from the URL
    var match = currentUrl.match(/\/ticket\/(\d+)/);

    // Check if a match is found
    if (match && match[1]) {
        // Return the captured ticket ID
        return match[1];
    } else {
        // Handle the case when the ticket ID is not found
        console.error("Ticket ID not found in the URL");
        return null;
    }
}

function changeWatchButton()
{
    watchButton.innerHTML = "Watching";
}

checkTicketWatching();