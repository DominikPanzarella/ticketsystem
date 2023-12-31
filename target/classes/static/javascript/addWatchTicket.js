
let watchButton = document.getElementById("watch-button");
var context = document.querySelector('base').getAttribute('href');

watchButton.addEventListener("click", function () {
    const idTicket = getTicketId().toString();
    console.log(idTicket);
    if (idTicket) {
         fetch(context + "ticket/"+idTicket+"/watch", {
            method: "POST"
        })
             .then(response => {
                 if (!response.ok) {
                     throw new Error('Network response was not ok');
                 }
                 return response.json();
             })
            .then(data => {
                updateWatchButton();
                updateCountButton(data);
            })
            .catch(error => {
                console.error("Error fetching data: ", error);
            })
    }


    function updateWatchButton() {
        watchButton.textContent = "Watching";

    }

    function updateCountButton(count)
    {
        document.getElementById("count").innerHTML = count;
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
});