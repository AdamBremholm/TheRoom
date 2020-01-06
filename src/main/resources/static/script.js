'use strict';
document.querySelector('#loginForm').addEventListener('submit', handleFirstForm, true);
document.querySelector('#registerForm').addEventListener('submit', register, true);
document.querySelector('#dialogueForm').addEventListener('submit', sendMessage, true);

var stompClient = null;
var name = null;
var room = null;
let token = null;

document.querySelector('#html5colorpicker').addEventListener('change', sendMessage, true);

function handleFirstForm(event) {
    event.preventDefault();
    name = document.querySelector('#name').value.trim();
    if(event.target.value==='login')
        login(event)
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
            email: email,
            roles: "USER",
            permissions: ""

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
                console.log(error.response.data.message);
                document.querySelector('#login-display-message').textContent = error.response.data.message;
            });
    }

}


function connect(event) {
    name = document.querySelector('#name').value.trim();
    room = document.querySelector('#room').value.trim();

    if (name) {
        var socket = new SockJS('/websocketApp');
        stompClient = Stomp.over(socket);
            stompClient.connect({Authorization: "Bearer " + token}, function () {
                connectionSuccess();
                token="";
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
    document.getElementById("roomTitle").innerHTML = room;
        stompClient.subscribe('/topic/' + room,  onMessageReceivedSubscription);
        stompClient.send("/app/chat.newUser." + room, {}, JSON.stringify({
            sender: name,
            type: 'newUser',
            roomName: room,
        }))
}
function sendMessage(event) {
    let type = null;
    let bgColor = null;
    let messageContent = null;
    if(event.target.id==='html5colorpicker') {
        type = "BG_CHANGE";
        bgColor = document.querySelector('#html5colorpicker').value.trim();
    } else if (event.target.id==='dialogueForm') {
        type = "CHAT";
        messageContent = document.querySelector('#chatMessage').value.trim();
    }
    if (stompClient) {
        var chatMessage = {
            sender : name,
            content : messageContent,
            type : type,
            roomName : room,
            rating : document.querySelector('#chatMessage').rating,
            roomBackgroundColor :  bgColor
        };
        stompClient.send("/app/chat.sendMessage."+room, {}, JSON
            .stringify(chatMessage));
        document.querySelector('#chatMessage').value = '';
    }
    event.preventDefault();
}

function onMessageReceivedSubscription(payload){
        document.querySelector('#login-page').classList.add('hidden');
        document.querySelector('#dialogue-page').classList.remove('hidden');
        onMessageReceived(payload)

}


function onMessageReceived(payload) {

    var message = JSON.parse(payload.body);
    var messageElement = document.createElement('li');
    var textElement = document.createElement('p');
    var messageText;
    if (message.type === 'newUser') {
        messageElement.classList.add('event-data');
        message.content = message.sender + ' has joined the chat';
        messageText = document.createTextNode(message.content);
        textElement.appendChild(messageText);
        messageElement.appendChild(textElement);
        backgroundColorString = message.roomBackgroundColor;
        if(backgroundColorString!=null) {
            document.getElementById('dialogue-page').style.backgroundColor = backgroundColorString;
            document.getElementById('html5colorpicker').value = backgroundColorString;
        }
    } else if (message.type === 'Leave') {
        messageElement.classList.add('event-data');
        message.content = message.sender.userName + 'has left the chat';
        messageText = document.createTextNode(message.content);
        textElement.appendChild(messageText);
        messageElement.appendChild(textElement);
    } else {
        messageElement.classList.add('message-data');
        var element = document.createElement('i');
        var text = document.createTextNode(message.sender[0]);
        element.appendChild(text);
        messageElement.appendChild(element);
        var usernameElement = document.createElement('span');
        var usernameText = document.createTextNode(message.sender);
        var localTime = new Date(message.time );
        var timeString = localTime.toString().split(' ').slice(0, 5).join(' ');
        var timeNode = document.createTextNode(" - " + timeString);
        var backgroundColorString = null;
        if(message.roomBackgroundColor!=null) {
            backgroundColorString = message.roomBackgroundColor;
            document.getElementById('dialogue-page').style.backgroundColor = backgroundColorString;
            document.getElementById('html5colorpicker').value = backgroundColorString;
        }
        messageText = document.createTextNode(message.content);
        textElement.appendChild(messageText);
        usernameElement.appendChild(usernameText);
        messageElement.appendChild(usernameElement);
        usernameElement.appendChild(timeNode);

        var ratingGrid = document.createElement('div');
        ratingGrid.className = "container ratingGrid";

        var ratingGridRow = document.createElement('div');
        ratingGridRow.className = "row justify-content-end";

        var ratingGridCol1 = document.createElement('div');
        ratingGridCol1.className = "col-10";
        var ratingGridCol2 = document.createElement('div');
        ratingGridCol2.className = "col col-custom";
        var ratingGridCol3 = document.createElement('div');
        ratingGridCol3.className = "col col-custom";
        var ratingGridCol4 = document.createElement('div');
        ratingGridCol4.className = "col col-custom";

        ratingGrid.appendChild(ratingGridRow);
        ratingGridRow.append(ratingGridCol1, ratingGridCol2, ratingGridCol3,ratingGridCol4);

        var decrRating = document.createElement('div');
        decrRating.className = "incDecButton";
        decrRating.innerHTML = '-';
        decrRating.addEventListener('click', function (evt) {
            var xhttp = new XMLHttpRequest();
            xhttp.onreadystatechange = function() {
                if (this.readyState == 4 && this.status == 200) {
                    var ratingText = document.getElementById("rating"+message.uuid);
                    var rating = this.responseText;
                    ratingText.innerHTML = rating;

                    if(rating > 0){
                        ratingText.style.color = "green";
                    }
                    else if(rating < 0){
                        ratingText.style.color = "red";
                    }
                    else{
                        ratingText.style.color = "black";
                    }
                }
            };
            xhttp.open("PUT", "/api/messages/decRating." + message.uuid, true);
            xhttp.send();
        });

        ratingGridCol2.appendChild(decrRating);

        var rating = document.createElement('div');
        rating.innerHTML = message.rating;
        rating.id = 'rating'+message.uuid;

        ratingGridCol3.appendChild(rating);

        var incrRating = document.createElement('div');
        incrRating.className = "incDecButton";
        incrRating.innerHTML = '+';
        incrRating.addEventListener('click', function (evt) {
            var xhttp = new XMLHttpRequest();
            xhttp.onreadystatechange = function() {
                if (this.readyState == 4 && this.status == 200) {
                    var ratingText = document.getElementById("rating"+message.uuid);
                    var rating = this.responseText;
                    ratingText.innerHTML = rating;

                    if(rating > 0){
                        ratingText.style.color = "green";
                    }
                    else if(rating < 0){
                        ratingText.style.color = "red";
                    }
                    else{
                        ratingText.style.color = "black";
                    }
                }
            };
            xhttp.open("PUT", "/api/messages/incRating." + message.uuid, true);
            xhttp.send();
        });

        ratingGridCol4.appendChild(incrRating);

        messageElement.appendChild(textElement);
        messageElement.appendChild(ratingGrid);
    }

    document.querySelector('#messageList').appendChild(messageElement);
    document.querySelector('#messageList').scrollTop = document
        .querySelector('#messageList').scrollHeight;

    document.querySelector('#decrease'+message.uuid)
}


