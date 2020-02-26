$(document).ready(() => {
//    $('#username').keypress((event) => {
//        var keycode = (event.keyCode ? event.keyCode : event.which);
//        if (keycode == '13') {
//            connectSocket(event);
//        }
//    });
    connectSocket();
});

let stompClient = null;
let stompClientP2P = null;
let receiverId = null;
//let username = null;

connectSocket = () => {
//    e.preventDefault();
//    username = $("#username").val().trim();
    if (username) {
        $("#registerDiv").hide();
        $.ajax({
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            'type': 'POST',
            'url': "chat/registerName",
            success: (res) => {
                $("#publicRoomDiv").html(res);
            },
            error: (res) => {
                console.log("error");
            }
        })

        var socket = new SockJS("/chatDemo");
        stompClient = Stomp.over(socket);
        stompClient.connect({}, onConnected, onError);

        var socketP2P = new SockJS("/secured/room");
        stompClientP2P = Stomp.over(socketP2P);
        stompClientP2P.connect({}, onConnectedPrivate, onErrorPrivate);
    }
}

onConnected = () => {
    stompClient.subscribe("/topic/public", onMessageReceived);
    stompClient.send("/api/chat/register", {}, JSON.stringify({sender: {id: userId}, messageType: "JOIN"}));
}

onError = () => {
//    alert("error");
}

onConnectedPrivate = () => {
    stompClientP2P.subscribe('/secured/user/queue/specific-user'
            + '-user' + userId, onPrivateMessageReceived);
}

onErrorPrivate = () => {
//    alert("error private");
}

send = () => {
    let message = $("#message").val().trim();
    if (message && stompClient) {
        let chatMessage = {
            sender: {id: userId},
            content: message,
            messageType: "CHAT",
        }
        stompClient.send("/api/chat/send", {}, JSON.stringify(chatMessage));
        $("#message").val('');
        $("#message").height("10px");

    }
}

onMessageReceived = (payload) => {
    let message = JSON.parse(payload.body);
    let html = "";
    html += "<div class='row col s12'>";
    if (message.messageType == "CHAT") {
        if (message.sender.id == userId) {
            html += "<div class='col s8 offset-s2 txt-bubble-right' style='word-wrap: break-word; text-align:right;'>"
                    + message.content + "</div>";
            html += "<div class='col s2' dir='rtl'>" + genAva(message.sender.username) + "</div>";
        } else {
            html += "<div class='col s2'>" + genAva(message.sender.username) + "</div>";
            html += "<div class='col s8 txt-bubble-left' style='word-wrap: break-word;'>" + message.content + "</div>";
        }
    } else {
        if (message.messageType == "JOIN") {
            html += "<div class='center grey-text text-lighten-1'>" + message.sender.username + " has entered the room!</div>";
        } else {
            html += "<div class='center grey-text text-lighten-1'>" + message.sender.username + " has left the room!</div>";
        }
    }
    html += "</div>";
    $("#chatBoxContent").append(html);
    $("#chatBoxContent").scrollTop(document.getElementById("chatBoxContent").scrollHeight);
}

p2pSend = () => {
    let message = $("#p2pMessage").val().trim();
    let receiver = $("#p2pReceiver").val().trim();
    if (message && stompClient) {
        let chatMessage = {
            sender: {id: userId},
            content: message,
            receiver: receiver,
            messageType: "CHAT",
        }
        stompClient.send("/api/chat/secured/room-" + receiver, {}, JSON.stringify(chatMessage));
        $("#p2pMessage").val('');
        $("#p2pMessage").height("10px");
        let html = "";
        html += "<div class='row col s12'>";
        html += "<div class='col s8 offset-s2 txt-bubble-right' style='word-wrap: break-word; text-align:right;'>"
                + message + "</div>";
        html += "<div class='col s2' dir='rtl'>" + genAva(username) + "</div>";
        html += "</div>";
        $("#p2pChatBoxContent").append(html);
        $("#p2pChatBoxContent").scrollTop(document.getElementById("p2pChatBoxContent").scrollHeight);
    }
}

onPrivateMessageReceived = (payload) => {
    let message = JSON.parse(payload.body);
    let html = "";
    html += "<div class='row col s12'>";

    html += "<div class='col s2'>" + genAva(message.sender) + "</div>";
    html += "<div class='col s8 txt-bubble-left' style='word-wrap: break-word;'>" + message.content + "</div>";


    html += "</div>";
    $("#p2pChatBoxContent").append(html);
    $("#p2pChatBoxContent").scrollTop(document.getElementById("p2pChatBoxContent").scrollHeight);
}

genAva = (name) => {
    let ava = "";
    ava += "<div class='center' style='width:50px; height: 50px; border-radius: 50%; background-color: #ffb74d'>";
    ava += "<p style='font-size: 30px'>" + name.substring(0, 1).toUpperCase() + "</p>";
    ava += "</div>";
    return ava;
}

setReceiver = (receiver) => {
    $("#p2pReceiver").val(receiver);
//    receiverId = 
}

