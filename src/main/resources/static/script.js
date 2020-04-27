document.addEventListener("DOMContentLoaded", function(){
    addCountryToForm();
    addRoomsToList();
    doSubmitForForm ();
    closeModalsWhenClicksOutside();
});

function closeModalsWhenClicksOutside (){
    let room = document.getElementById('room-modal');
    let roomForm = document.getElementById('createfrm');
    window.onclick = function(event) {
        if (event.target == room) {
            room.style.display = "none";
        }
        if (event.target == roomForm) {
            roomForm.style.display = "none";
        }
    };
}

function doSubmitForForm (){
    let site_protocol = location.protocol;
    let site_host = location.host;
    let room_path = "/api/rooms";
    let room_url = site_protocol + '//'+ site_host + room_path;
    const ajaxSend = (formData) => {
        fetch(room_url, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(formData)
        })
        .then(response => {
            closeRoomForm();
            alert('Room is created.');
            addRoomsToList();
        })
        .catch(error => {
            alert('Room is not created.');
            console.error(error);
        })
    };
    let roomForm = document.getElementById('room-form');
    roomForm.addEventListener('submit', function (e) {
        e.preventDefault();
        let formData = new FormData(this);
        formData = Object.fromEntries(formData);
        ajaxSend(formData);
        this.reset();
    });
}

let refreshRoomList = setInterval(() => { addRoomsToList(); }, 30000);

function getUserCountryCode(){
    // using "ipdata.co" REST API to locate website visitors by IP Address
    return fetch('https://api.ipdata.co/?api-key=a35215bc95c1420fafdb8cce31a7af836d6c1cfd90febbcb6ce1e7a5&fields=country_code')
        .then(response => response.json())
        .then(date => {
            let code = date.country_code;
            return code;
        });
}

function handleErrors(response) {
    if (!response.ok) {
        throw Error(response.statusText);
    }
    return response;
}

function addCountryToForm(){
    let listElement = document.getElementById("country");
    countryListAlpha2.forEach(function (item) {
        let option = document.createElement('option');
        listElement.appendChild(option);
        option.innerHTML = item.name;
        option.value = item.code;
    });
};

function addRoomsToList(){
    let site_protocol = location.protocol
    let site_host = location.host;
    let rooms_path = "/api/rooms/in";
    let rooms_url = site_protocol + '//'+ site_host + rooms_path;
    fetch(rooms_url)
    .then(response => response.json())
    .then(data => {
        let listElement = document.getElementById("room-list");
        while (listElement.firstChild) {
            listElement.removeChild(listElement.lastChild);
        };
        data.forEach(function (item) {
            let li = document.createElement('li');
            listElement.appendChild(li);
            li.appendChild(document.createTextNode(item.name + '  '));
            let button = document.createElement('button');
            button.innerHTML = "Open room ";
            button.type = "submit";
            button.class = "openroom";
            button.style="width:100px; height: 30px; padding: 5px 5px;"
            let onclickFunction = "enterToRoom("+ item.id+");";
            button.setAttribute("onclick",onclickFunction);
            li.appendChild(button);
        })
    })
    .catch(error => {
        console.log("Mo connection");
        console.log(error);
    });
};

let intervalRefreshLamp;

function stopRefreshLamp() {
    clearInterval(intervalRefreshLamp);
}

function enterToRoom(id){
    document.getElementById('room-modal').style.display='block';
    let site_protocol = location.protocol
    let site_host = location.host;
    let room_path = "/api/rooms/in/";
    getUserCountryCode()
    .then(code =>{
        let room_url = site_protocol + '//'+ site_host + room_path + id +"/from/" + code;
        fetch(room_url)
        .then(handleErrors)
        .then(response =>response.json())
        .then((data) => {
            document.getElementById("my_room_name").innerHTML = data.name;
            intervalRefreshLamp = setInterval(() => {
              getLamp(data.lampId, code);
            }, 500);
            let pressOnClick = "pressLamp(" + data.lampId + ", '" + code + "');"
            document.getElementById("press-button").setAttribute("onclick",pressOnClick);
        }).catch(error => {
            alert("Not Access! OR Not Found this room!");
            closeRoom();
            console.log(error);
        });
    })
};

function getLamp(id, code){
    let site_protocol = location.protocol;
    let site_host = location.host;
    let lamp_path = "/api/lamps/in/";
    let lamp_url = site_protocol + '//'+ site_host + lamp_path + id +"/from/" + code;
    fetch(lamp_url)
    .then((response) => {return response.json();})
    .then((data) => {
        if (data.isLightsOn){
            document.getElementById("room-lamp").setAttribute('src', "lighton.png")
            document.getElementById("room-lamp").setAttribute('alt', "Lamp is lights on.")
        } else {
            document.getElementById("room-lamp").setAttribute('src', "lightoff.png")
            document.getElementById("room-lamp").setAttribute('alt', "Lamp is lights off.")
        }
    })
    .catch(error => {
        closeRoom();
        console.log(error);
    });
};

function pressLamp(id, code){
    let site_protocol = location.protocol
    let site_host = location.host;
    let lamp_path = "/api/lamps/in/";
    let lamp_url = site_protocol + '//'+ site_host + lamp_path + id +"/from/" + code;
    fetch(lamp_url, { method: 'PUT' })
    .catch(error => {
        console.log(error);
        closeRoom();
    });

};

function closeRoomForm() {
    document.getElementById('createfrm').style.display='none';
}

function closeRoom() {
    stopRefreshLamp();
    document.getElementById('room-modal').style.display='none';
    document.getElementById("my_room_name").innerHTML ="";
    let pressOnClick = ""
    document.getElementById("room-lamp").setAttribute('src', "lightoff.png")
    document.getElementById("room-lamp").setAttribute('alt', "Lamp.")
    document.getElementById("press-button").setAttribute("onclick",pressOnClick);
}
