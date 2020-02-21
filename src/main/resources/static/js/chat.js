$(document).ready(() => {
    $('#username').keypress((event) => {
        var keycode = (event.keyCode ? event.keyCode : event.which);
        if (keycode == '13') {
            connectSocket(event);
        }
    });
});

let stompClient = null;
let username = null;

connectSocket = (e) => {
    e.preventDefault();
    username = $("#username").val().trim();
    if (username) {
        $("#registerDiv").hide();
        $.ajax({
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            'type': 'POST',
            'url': "chat/registerName",
            'data': JSON.stringify({username}),
            success: (res) => {
                $("#publicRoomDiv").html(res);
            },
            error: (res) => {
                console.log("error");
            }
        })

        $("#chatRoomTitle").mouse

        var socket = new SockJS("/chatDemo");
        stompClient = Stomp.over(socket);

        stompClient.connect({}, onConnected, onError);

        socket.onclose = () => {
            console.log('close');
            let chatMessage = {
                sender: username,
                content: username + " has left the room!",
                messageType: "LEAVE",
            }
            stompClient.send("/api/chat/send", {}, JSON.stringify(chatMessage));
//            stompClient.disconnect();
//            setConnected();
        };
    }
}

onConnected = () => {
    stompClient.subscribe("/room/public", onMessageReceived);

    stompClient.send("/api/chat/register", {}, JSON.stringify({sender: username, messageType: "JOIN"}));
}

onError = () => {
    alert("error");
}

send = () => {
    let message = $("#message").val().trim();
    if (message && stompClient) {
        let chatMessage = {
            sender: username,
            content: message,
            messageType: "CHAT",
        }
        $("#message").val("");
        stompClient.send("/api/chat/send", {}, JSON.stringify(chatMessage));
    }
}

onMessageReceived = (payload) => {
    let message = JSON.parse(payload.body);
    let html = "";
    html += "<div class='row col s12'>";
    if (message.sender == username) {
        html += "<div class='col s8 offset-s2 txt-bubble-right' style='word-wrap: break-word;' dir='rtl'>" + message.content + "</div>";
        html += "<div class='col s2' dir='rtl'>" + genAva(message.sender) + "</div>";
    } else {
        html += "<div class='col s2'>" + genAva(message.sender) + "</div>";
        html += "<div class='col s8 txt-bubble-left' style='word-wrap: break-word;'>" + message.content + "</div>";
    }
    html += "</div>";
    $("#chatBoxContent").append(html);
    $("#chatBoxContent").scrollTop(document.getElementById("chatBoxContent").scrollHeight);
}

genAva = (name) => {
    let ava = "";
    ava += "<div class='center' style='width:50px; height: 50px; border-radius: 50%; background-color: #ffb74d'>";
    ava += "<p style='font-size: 30px'>" + name.substring(0, 1).toUpperCase() + "</p>";
    ava += "</div>";
    return ava;
}
