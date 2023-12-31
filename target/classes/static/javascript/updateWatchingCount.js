const count = document.getElementById("count");
function ticketWatchingSize()
{
    const url = "/ticket/watch/size";

    fetch(url, {method:"GET"})
        .then(response =>
        {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            count.innerHTML = data;
        })
        .catch(error => {
            console.error("Error fetching data: ", error);
        })
}

ticketWatchingSize();
