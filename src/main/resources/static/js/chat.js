$(document).ready(() => {
    connectSocket();
    friendsWithNewMessages.map(el => {
        setNewMessAlert(el.id);
    });
    setUsersNoti();
    $(document).mousedown((event) => {
        countClick++;
        if (countClick == 1) {
            initialClick = new Date();
        }
        if (countClick >= 5) {
            countClick = 0;
            lastClick = new Date();
            let diff = lastClick - initialClick;
            if (diff <= 1000) {
                let x = event.clientX - 25;
                let y = event.clientY - 25;
                let html = "<img id='banana' class='dropAndFade' src='banana.gif' onload='onBananaLoad()' style='position: relative; top: " + y + "px; left: " + x + "px;' height='50' width='50'/>";
                $("#bananaDiv").html(html);
            }
        }
    });
});

onBananaLoad = () => {
    setTimeout(() => {
        $("#banana").removeClass('dropAndFade');
        $("#banana").css('top', "calc(100vh - 50px)");
        $("#banana").draggable({
            scroll: false,
            axis: "x,y",
            containment: "#bananaDiv",
            revert: false,
            helper: "orginal",
            disable: false,
            start: function (event, ui) {
                $("#banana").removeClass('dropAndFade');
            },
            drag: function (event, ui) {
            },
            stop: function (event, ui) {
                $("#banana").addClass('dropAndFade');
            }
        });
    }, 500)
}

let initialClick = 0;
let lastClick = 0;
let countClick = 0;

setNewMessAlert = (id) => {
    $("#friend" + id).addClass("newMess");
}

unsetNewMessAlert = (id) => {
    $("#friend" + id).removeClass("newMess");
}

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
        if (message.content) {
            if (message.sender.id == userId) {
                html += "<div class='col s8 offset-s2 txt-bubble-right' style='word-wrap: break-word; text-align:right;'>"
                        + message.content + "</div>";
                html += "<div class='col s2' dir='rtl'>" + genAva(message.sender.username) + "</div>";
            } else {
                html += "<div class='col s2'>" + genAva(message.sender.username) + "</div>";
                html += "<div class='col s8 txt-bubble-left' style='word-wrap: break-word;'>" + message.content + "</div>";
            }
        }
    } else {
        if (message.messageType == "JOIN") {
            setUsersNoti();
            html += "<div class='center grey-text text-lighten-1'>" + message.sender.username + " has entered the room!</div>";
        } else {
            setUsersNoti();
            html += "<div class='center grey-text text-lighten-1'>" + message.sender.username + " has left the room!</div>";
        }
    }
    html += "</div>";
    $("#chatBoxContent").append(html);
    $("#chatBoxContent").scrollTop(document.getElementById("chatBoxContent").scrollHeight);
}

setUsersNoti = () => {
    $.get("api/chat/getPublicUsers", (res) => {
        let html = "<i class='material-icons icon'>people</i>";
        html += "<div class='noti'><span class='noti-content'>" + res.length + "</span></div>";
        $("#usersNoti").html(html);
    });

}

p2pSend = () => {
    let message = $("#p2pMessage").val().trim();
    if (!receiverId) {
        alert("Hãy chọn 1 người bạn");
        return;
    }
//    let receiver = $("#p2pReceiver").val().trim();
    if (message && stompClient) {
        let chatMessage = {
            sender: {id: userId},
            content: message,
            receiver: {id: receiverId},
            messageType: "CHAT",
        }
        stompClient.send("/api/chat/secured/room-" + receiverId, {}, JSON.stringify(chatMessage));
        $("#p2pMessage").val('');
        $("#p2pMessage").height("10px");
        if (message) {
            let html = "";
            html += "<div class='row col s12'>";
            html += "<div class='col s8 offset-s2 txt-bubble-right' style='word-wrap: break-word; text-align:right;'>"
                    + message + "</div>";
            html += "<div class='col s2' dir='rtl'>" + genAva(username) + "</div>";
            html += "</div>";
            $("#p2pChatBoxContent").append(html);
        }
        $("#p2pChatBoxContent").scrollTop(document.getElementById("p2pChatBoxContent").scrollHeight);
    }
}

onPrivateMessageReceived = (payload) => {
    let message = JSON.parse(payload.body);

    if (receiverId && receiverId == message.sender.id) {
        if (message.content) {
            let html = "";
            html += "<div class='row col s12'>";

            html += "<div class='col s2'>" + genAva(message.sender.username) + "</div>";
            html += "<div class='col s8 txt-bubble-left' style='word-wrap: break-word;'>" + message.content + "</div>";
            html += "</div>";
            $("#p2pChatBoxContent").append(html);
        }
    } else {
        if (message.sender.id) {
            setNewMessAlert(message.sender.id);
        }
    }

    $("#p2pChatBoxContent").scrollTop(document.getElementById("p2pChatBoxContent").scrollHeight);
}

genAva = (name) => {
    let ava = "";
    ava += "<div class='center' style='width:50px; height: 50px; border-radius: 50%; background-color: #ffb74d'>";
    ava += "<p style='font-size: 30px'>" + name.substring(0, 1).toUpperCase() + "</p>";
    ava += "</div>";
    return ava;
}

setReceiver = (id, name) => {
    receiverId = id;
    $("#receiverName").html(name);
    $.ajax({
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        'type': 'POST',
        'url': "api/chat/getPrivateMessages/" + userId + "/" + receiverId,
        success: (res) => {
            unsetNewMessAlert(receiverId);
            let chatMessages = res;
            console.log(res);
            $("#p2pChatBoxContent").html("");
            chatMessages.map(el => {
                if (el.sender.id == userId) {
                    if (el.content) {
                        let html = "";
                        html += "<div class='row col s12' title='" + el.time + "'>";
                        html += "<div class='col s8 offset-s2 txt-bubble-right' style='word-wrap: break-word; text-align:right;'>"
                                + el.content + "</div>";
                        html += "<div class='col s2' dir='rtl'>" + genAva(el.sender.username) + "</div>";
                        html += "</div>";
                        $("#p2pChatBoxContent").append(html);
                    }
                } else {
                    if (el.content) {
                        let html = "";
                        html += "<div class='row col s12' title='" + el.time + "'>";

                        html += "<div class='col s2'>" + genAva(el.sender.username) + "</div>";
                        html += "<div class='col s8 txt-bubble-left' style='word-wrap: break-word;'>" + el.content + "</div>";

                        html += "</div>";
                        $("#p2pChatBoxContent").append(html);
                    }
                }
                $("#p2pChatBoxContent").scrollTop(document.getElementById("p2pChatBoxContent").scrollHeight);
            })
        },
        error: (res) => {
            console.log("error");
        }
    })
}