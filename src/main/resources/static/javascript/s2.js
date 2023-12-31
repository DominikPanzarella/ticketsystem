const searchInput = document.getElementById("searchInput");
const context = document.querySelector('base').getAttribute('href');

searchInput.addEventListener("keyup", function()
{
    var content = searchInput.value;
    console.log(content);
    if(content.length === 0 || content.length >=3)
    {
        searchFunction();
    }
});

function searchFunction()
{

        fetch("/ticket/search?q=" + searchInput.value,{method: "GET"})
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            })
            .then(data=> {
                console.log(data);
                updateTicketList(data);
            })
            .catch(error => {
                console.error("Error fetching data: ",error);
            })

}

function updateTicketList(tickets) {
    const container = document.querySelector("#content_container");

    let articles = '';
    for(var i=0; i< tickets.length; i++){
        const date = new Date(tickets[i].date);
        const dateFormatted =  formatDate(tickets[i].creationDate)
        articles +=
            '<article class="col-sm-6 col-md-4">\n'+
            '    <div class="card mb-4 shadow-sm">\n'+
            '        <div class="card-body">\n'+
            '            <p style="color:grey">\n'+
            '                <span>#'+tickets[i].id+'</span>\n'+
            '               <span class="badge bg-primary detail-status">'+tickets[i].status+'</span>\n'+
            '                <strong>'+tickets[i].type+'</strong> | <span>'+dateFormatted+'</span>, by <span style="color:dodgerblue" href="#">'+tickets[i].user.username+'</span>\n'+
            '           </p>\n'+
            '           <strong><span class="card-title">'+tickets[i].title+'</span></strong>\n'+
            '            <hr>\n'+
            '                <p class="card-description"><span>'+tickets[i].description+'</span>\n'+
            '                </p>\n'+
            '                <div class="d-flex justify-content-between align-items-center">\n'+
            '                    <div class="btn-group">\n'+
            '                        <a class="btn btn-sm btn-outline-secondary" href="'+'ticket/'+tickets[i].id+'">View</a>\n'+
            '                    </div>\n'+
            '                </div>\n'+
            '        </div>\n'+
            '    </div>\n'+
            '</article>\n';
    }

    if(tickets.length===0){
        articles = '<article class="col-md-4"><p>Nessun ticket trovato</p></article>';
    }

    container.innerHTML = '<h2 class="mt-4">Risultati ricerca: '+searchInput.value+' <a class="btn btn-sm btn-danger" href="'+window.location.href+'">Chiudi</a></h2>' +
        '<section class="row">\n' +
        '        \n' +
        articles +
        '            \n' +
        '        \n' +
        '    </section>';


}

function formatDate(dateString) {
    // Parse the date string
    var date = new Date(dateString);

    // Define the formatting options
    var options = { hour: '2-digit', minute: '2-digit', month: '2-digit', day: '2-digit', year: 'numeric' };

    // Format the date
    var formatter = new Intl.DateTimeFormat('en-US', options);
    return formatter.format(date);
}