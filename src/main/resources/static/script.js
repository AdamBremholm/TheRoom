'use strict';
document.querySelector('#loginForm').addEventListener('submit', handleFirstForm, true);
document.querySelector('#registerForm').addEventListener('submit', register, true);
document.querySelector('#dialogueForm').addEventListener('submit', sendMessage, true);
document.querySelector('#html5colorpicker').addEventListener('change', changeBGColor, true);

let stompClient = null;
let name = null;
let room = null;
let token = null;



function handleFirstForm(event) {
    event.preventDefault();
    name = document.querySelector('#name').value.trim();
    if(event.target.value==='login')
        login(event);
    else
        switchToRegisterForm(event)
}


function switchToRegisterForm(event){
    document.querySelector('#login-page').classList.add('hidden');
    document.querySelector('#register-page').classList.remove('hidden');
    document.querySelector('#register-name').value = name
}

function switchToLoginForm(event){
    document.querySelector('#login-page').classList.remove('hidden');
    document.querySelector('#register-page').classList.add('hidden');
    document.querySelector('#name').value = name
}

function register(event){
    name = document.querySelector('#register-name').value.trim();
    let firstName = document.querySelector('#register-firstName').value.trim();
    let lastName = document.querySelector('#register-firstName').value.trim();
    let password = document.querySelector('#register-password').value.trim();
    let passwordConfirm = document.querySelector('#register-password-confirm').value.trim();
    let email = document.querySelector('#register-email').value.trim();
    event.preventDefault();
    if(name && password && passwordConfirm) {
        axios.post('/api/users', {
            userName: name,
            firstName: firstName,
            lastName: lastName,
            password: password,
            passwordConfirm : passwordConfirm,
            email: email
        })
            .then(function (response) {
                console.log(response.data);
                document.querySelector('#register-display-message').textContent = "User Created";
                setTimeout(function(){  switchToLoginForm(event); }, 1000);
            })
            .catch(function (error) {
                console.log(error.response.data.message);
                document.querySelector('#register-display-message').textContent = error.response.data.message;
            });
    }

}


function login(event){
    let password = document.querySelector('#password').value.trim();
    event.preventDefault();
    if(name && password) {
        axios.post('/login', {
            username: name,
            password: password
        })
            .then(function (response) {
                token = response.data.token;
                connect(event);
            })
            .catch(function (error) {
                console.log(error);
                document.querySelector('#login-display-message').textContent = "403"
            });
    }

}

function banUser() {
    let param = new URLSearchParams();
    let user = document.querySelector('#uname-sheet').value.trim();
    let room = document.getElementById('roomTitle').innerHTML;
    param.append('username', user);
    param.append('roomname', room);
    axios({
        method: 'put',
        url: '/api/admin/ban',
        headers: { Authorization: "Bearer " + token },
        data: param
    }).then(function (res) {
        console.log(res);
    }).catch(function (error) {
        console.log(error);
    });
}

function unbanUser() {
    let param = new URLSearchParams();
    let user = document.querySelector('#uname-sheet').value.trim();
    let room = document.getElementById('roomTitle').innerHTML;
    param.append('username', user);
    param.append('roomname', room);
    axios({
        method: 'put',
        url: '/api/admin/unban',
        headers: { Authorization: "Bearer " + token },
        data: param
    }).then(function (res) {
        console.log(res);
    }).catch(function (error) {
        console.log(error);
    });
}

function giveAdminPrivileges() {
    let param = new URLSearchParams();
    let user = document.querySelector('#uname-sheet').value.trim();
    param.append('username', user);
    axios({
        method: 'put',
        url: '/api/admin/upgrade',
        headers: { Authorization: "Bearer " + token },
        data: param
    }).then(function (res) {
        console.log(res);
    }).catch(function (error) {
        console.log(error);
    });
}

function connect(event) {
    name = document.querySelector('#name').value.trim();


    if (name) {
        let socket = new SockJS('/websocketApp');
        stompClient = Stomp.over(socket);
        stompClient.connect({Authorization: "Bearer " + token}, function () {
            connectionSuccess();
        }, function (message) {
            if(message.toString().includes("Unauthorized")){
                document.querySelector('#login-display-message').textContent = "Unauthorized"
            } else if(message.toString().includes("AccessDeniedException")){
                document.querySelector('#login-display-message').textContent = "Access to this room is denied"
            }
        });
    }
    event.preventDefault();
}

function connectionSuccess() {
    room = document.querySelector('#room').value.trim();
    document.getElementById("roomTitle").innerHTML = room;

    stompClient.subscribe('/topic/' + name + "." + room,  retrieveAllRoomMessages,{id: name+"retrieveAll"});
    stompClient.send('/app/chat.retrieveAll.' + name + "." + room, {});
}



function retrieveAllRoomMessages(payload) {

    loadOldMessages(payload);

    stompClient.unsubscribe(name+"retrieveAll");

    document.querySelector('#login-page').classList.add('hidden');
    document.querySelector('#dialogue-page').classList.remove('hidden');

    stompClient.subscribe('/topic/chatMessages.' + room,  onNewMessage);
    stompClient.subscribe('/topic/alerts.' + room, onNewAlert);
    stompClient.subscribe('/topic/backgroundChange.' + room, onBackgroundChange);
    stompClient.subscribe('/topic/rating.' + room, onRatingChange);

    stompClient.send("/app/chat.newUser." + room, {}, JSON.stringify({
        sender: name,
        type: 'newUser',
        rating: null,
        roomName: room
    }));
}

function changeBGColor(event){
    let type = "BG_CHANGE";
    let bgColor = document.querySelector('#html5colorpicker').value.trim();
    let messageUri = "/app/chat.changeBgColor." + room;

    if (stompClient) {
        let chatMessage = {
            sender : name,
            type : type,
            roomName : room,
            rating: null,
            roomBackgroundColor :  bgColor
        };
        stompClient.send(messageUri, {}, JSON
            .stringify(chatMessage));
        document.querySelector('#chatMessage').value = '';
    }
    event.preventDefault();
}

function sendMessage(event) {

    let type = "CHAT";
    let messageContent = document.querySelector('#chatMessage').value.trim();
    let messageUri = "/app/chat.sendMessage." + room;

    if (stompClient) {
        let chatMessage = {
            sender : name,
            content : messageContent,
            type : type,
            roomName : room
        };
        stompClient.send(messageUri, {}, JSON
            .stringify(chatMessage));
        document.querySelector('#chatMessage').value = '';
    }
    event.preventDefault();
}

function onNewMessage(payload){

    let response = JSON.parse(payload.body);

    if(response.statusCodeValue === 200){
        let message = response.body;
        createMessage(message);

    } else {
        badResponse(response);
    }

}

function onBackgroundChange(payload) {

    let response = JSON.parse(payload.body);

    if(response.statusCodeValue === 200){
        let bgColor = response.body;

        if (bgColor != null) {
            document.getElementById('dialogue-page').style.backgroundColor = bgColor;
            document.getElementById('html5colorpicker').value = bgColor;
        }

    } else {
        badResponse(response);
    }

}

function createMessage(message){

    let messageElement = document.createElement('li');
    let textElement = document.createElement('p');
    let messageText;
    let element = document.createElement('i');
    let text = document.createTextNode(message.sender[0]);
    let usernameElement = document.createElement('span');
    let usernameText = document.createTextNode(message.sender);
    let localTime = new Date(message.time );
    let timeString = localTime.toString().split(' ').slice(0, 5).join(' ');
    let timeNode = document.createTextNode(" - " + timeString);
    let bgColor = message.roomBackgroundColor;

    document.getElementById('dialogue-page').style.backgroundColor = bgColor;
    document.getElementById('html5colorpicker').value = bgColor;

    messageElement.classList.add('message-data');

    element.appendChild(text);
    messageElement.appendChild(element);

    messageText = document.createTextNode(message.content);
    textElement.appendChild(messageText);
    usernameElement.appendChild(usernameText);
    messageElement.appendChild(usernameElement);
    usernameElement.appendChild(timeNode);

    let ratingGrid = document.createElement('div');
    ratingGrid.className = "container ratingGrid";

    let ratingGridRow = document.createElement('div');
    ratingGridRow.className = "row justify-content-end";

    let ratingGridCol1 = document.createElement('div');
    ratingGridCol1.className = "col-10";
    let ratingGridCol2 = document.createElement('div');
    ratingGridCol2.className = "col col-custom";
    let ratingGridCol3 = document.createElement('div');
    ratingGridCol3.className = "col col-custom";
    let ratingGridCol4 = document.createElement('div');
    ratingGridCol4.className = "col col-custom";

    ratingGrid.appendChild(ratingGridRow);
    ratingGridRow.append(ratingGridCol1, ratingGridCol2, ratingGridCol3, ratingGridCol4);

    let decrRating = document.createElement('div');
    decrRating.className = "incDecButton";
    decrRating.id = "decreaseRating";
    decrRating.innerHTML = '-';
    decrRating.addEventListener('click', function() {
        stompClient.send("/app/chat.decreaseRating." + message.uuid + "." + room, {});
    });

    ratingGridCol2.appendChild(decrRating);

    let rating = document.createElement('div');
    rating.innerHTML = message.rating;
    rating.id = 'rating' + message.uuid;
    changeRatingColor(rating);
    ratingGridCol3.appendChild(rating);

    let incrRating = document.createElement('div');
    incrRating.className = "incDecButton";
    incrRating.id = "increaseRating";
    incrRating.innerHTML = '+';
    incrRating.addEventListener('click', function() {
        stompClient.send("/app/chat.increaseRating." + message.uuid + "." + room, {});
    });

    ratingGridCol4.appendChild(incrRating);

    messageElement.appendChild(textElement);
    messageElement.appendChild(ratingGrid);
    document.querySelector('#messageList').appendChild(messageElement);
}

function loadOldMessages(payload){
    let response = JSON.parse(payload.body);

    if(response.statusCodeValue === 200){
        let messageArray = response.body;

        messageArray.forEach(function(message){
            createMessage(message)
        })

    } else {
        badResponse(response);
    }


}

function onNewAlert(payload){

    let response = JSON.parse(payload.body);

    if(response.statusCodeValue === 200){
        let message = response.body;
        let messageElement = document.createElement('li');
        let textElement = document.createElement('p');
        let messageText;

        if(message.type === 'newUser'){
            messageElement.classList.add('event-data');
            message.content = message.sender + ' has joined the chat';
            messageText = document.createTextNode(message.content);
            textElement.appendChild(messageText);
            messageElement.appendChild(textElement);

        }
        else if (message.type === 'Leave'){
            messageElement.classList.add('event-data');
            message.content = message.sender.userName + 'has left the chat';
            messageText = document.createTextNode(message.content);
            textElement.appendChild(messageText);
            messageElement.appendChild(textElement);

        }

        document.querySelector('#messageList').appendChild(messageElement);

        document.querySelector('#messageList').scrollTop = document
            .querySelector('#messageList').scrollHeight;

    } else {
        badResponse(response);
    }

}

function onRatingChange(payload){

    let response = JSON.parse(payload.body);

    if(response.statusCodeValue === 200){
        let message = response.body;
        let ratingElement = document.getElementById("rating"+message.uuid);
        ratingElement.innerHTML = message.rating;
        changeRatingColor(ratingElement);

    } else {
        badResponse(response);
    }

}

function changeRatingColor(ratingElement){

    let rating = ratingElement.innerHTML;
    if(rating > 0){
        ratingElement.style.color = "green";
    }
    else if(rating < 0){
        ratingElement.style.color = "red";
    }
    else{
        ratingElement.style.color = "black";
    }
}

function badResponse(response){

    if(response.headers.User[0] === name){
        console.error(response.body);
    }
}




