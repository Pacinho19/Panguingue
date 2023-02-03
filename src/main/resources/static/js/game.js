function selectCard(cardDto) {

    var xhr = new XMLHttpRequest();
    var url = '/panguingue/games/' + document.getElementById("gameId").value + '/move';
    xhr.open("POST", url, true);
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.onreadystatechange = function () {};
    var data = JSON.stringify({ "suit": cardDto.suit, "rank": cardDto.rank });
    xhr.send(data);

}

var stompClient = null;
var privateStompClient = null;

socket = new SockJS('/ws');
privateStompClient = Stomp.over(socket);
privateStompClient.connect({}, function (frame) {
    var gameId = document.getElementById('gameId').value;
    privateStompClient.subscribe('/reload-board/' + gameId, function (result) {
        updateBoard();
    });
});

stompClient = Stomp.over(socket);

function updateBoard() {
 $.get("/panguingue/games/" + document.getElementById("gameId").value + "/board/reload", function(data){
    $("#board").replaceWith(data);
  });
}