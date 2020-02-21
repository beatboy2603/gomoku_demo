/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

//$(document).ready(() => {
//    $("#publicRoomDiv").hide();
//});

connectSocket = (e) => {
    let username = $("#username").val();
    if (username) {
        $("#registerDiv").hide();
//        $.post("chat/registerName?username=" + username
//                , (res) => {
//            console.log(res);
//            $("#publicRoomDiv").html(res);
//        });
//        
        $.ajax({
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            'type': 'POST',
            'url': "chat/registerName",
            'data': JSON.stringify({username}),
            'dataType': 'json',
            success:  (res) => {
                console.log("abc")
                console.log(res);
                $("#publicRoomDiv").html(res);
            },
            error: (res)=>{
                console.log("wut")
                console.log(res);
                $("#publicRoomDiv").html(res.responseText);
            }
        })
    }
}


