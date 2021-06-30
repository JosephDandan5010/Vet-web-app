const dropdown = document.querySelector("#selectSearch")
const searchForm = document.querySelector("#searchForm")

function chgAction()
{
    if( dropdown.value =="animals" ) {
        searchForm.action = "/user/search/animals";
    }
    else if( dropdown.value =="appointments" ) {
       searchForm.action = "/user/search/appointments";
    }
}

chgAction();
dropdown.addEventListener("change", chgAction);