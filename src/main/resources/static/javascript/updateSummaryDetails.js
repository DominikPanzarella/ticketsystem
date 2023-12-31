
const numberOpen = document.getElementById("number_open");
const numberInp = document.getElementById("number_inp");
const numberDone = document.getElementById("number_done");


function updateCountElement(url, htmlElement)
{
    fetch(url, {method : "GET"})
        .then(response =>
        {
            if(!response.ok)    throw new Error('Network response was not ok');
            return response.json();
        })
        .then(data => {
            htmlElement.innerHTML = data.length;
        });
}

function updateSummaryDetails(){



    function updateCounts(){
        const urlOpen = "/tickets/open";
        const urlInp = "/tickets/inprogress";
        const urlDone = "/tickets/done";

        updateCountElement(urlOpen, numberOpen);
        updateCountElement(urlInp, numberInp);
        updateCountElement(urlDone, numberDone);
    }

    updateCounts();
}

updateSummaryDetails();

