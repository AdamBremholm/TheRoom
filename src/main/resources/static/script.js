'use strict';
document.querySelector('#loginForm').addEventListener('submit', handleFirstForm, true);
document.querySelector('#registerForm').addEventListener('submit', register, true);
document.querySelector('#dialogueForm').addEventListener('submit', sendMessage, true);

let stompClient = null;
let name = null;
let room = null;
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
                console.log(error);
                document.querySelector('#login-display-message').textContent = "403"
            });
    }

}


function connect(event) {
    name = document.querySelector('#name').value.trim();
    room = document.querySelector('#room').value.trim();

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
        let chatMessage = {
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

    let message = JSON.parse(payload.body);
    let messageElement = document.createElement('li');
    let textElement = document.createElement('p');
    let messageText;
    let backgroundColorString = null;
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
        let element = document.createElement('i');
        let text = document.createTextNode(message.sender[0]);
        element.appendChild(text);
        messageElement.appendChild(element);
        let usernameElement = document.createElement('span');
        let usernameText = document.createTextNode(message.sender);
        let localTime = new Date(message.time );
        let timeString = localTime.toString().split(' ').slice(0, 5).join(' ');
        let timeNode = document.createTextNode(" - " + timeString);

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
        ratingGridRow.append(ratingGridCol1, ratingGridCol2, ratingGridCol3,ratingGridCol4);

        let decrRating = document.createElement('div');
        decrRating.className = "incDecButton";
        decrRating.innerHTML = '-';
        decrRating.addEventListener('click', function() {
            stompClient.send("/app/chat.decreaseRating." + message.uuid, {});
        });

        ratingGridCol2.appendChild(decrRating);

        let rating = document.createElement('div');
        rating.innerHTML = message.rating;
        rating.id = 'rating'+message.uuid;

        ratingGridCol3.appendChild(rating);

        let incrRating = document.createElement('div');
        incrRating.className = "incDecButton";
        incrRating.innerHTML = '+';
        incrRating.addEventListener('click', function () {
            stompClient.send("/app/chat.increaseRating." + message.uuid, {
               Authorization: "Bearer " + token
            });
        });

        ratingGridCol4.appendChild(incrRating);

        messageElement.appendChild(textElement);
        messageElement.appendChild(ratingGrid);

        stompClient.subscribe('/topic/' + message.uuid,  function (payload) {
            let updatedMessage = JSON.parse(payload.body);
            let ratingText = document.getElementById("rating"+message.uuid);
            let rating = updatedMessage.rating;
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
        });
    }

    document.querySelector('#messageList').appendChild(messageElement);
    document.querySelector('#messageList').scrollTop = document
        .querySelector('#messageList').scrollHeight;
}


