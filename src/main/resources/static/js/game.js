let selectedCards = [];

function selectCard(cardDto, cards) {
    rankCards = filterCards(cardDto.rank, cards);

    var cardRankCount = cardDto.rank === 'NINE' && heartNineNotExists(cards) ? 3 : 4;

    if (rankCards.length == cardRankCount && selectedCards.length < cardRankCount) {
        // showThrowQuestion(cardDto, rankCards);  
        slideOutCard(cardDto);

        if (selectedCards.length == cardRankCount) {
            sendCardRequest()
            return;
        }else if(selectedCards.length == 1){
            console.log('addThrowOneBtn');
            addThrowOneBtn();
            return;
        }else{
            removeThrowOneBtn();
            return;
        }
    } 
    
    selectedCards.push(cardDto);
    sendCardRequest()
}

function removeThrowOneBtn(){
    var throwOneBtnDiv = document.getElementById('throwOneBtnDiv');
    throwOneBtnDiv.innerHTML="";
}

function addThrowOneBtn(){
    var throwOneBtnDiv = document.getElementById('throwOneBtnDiv');

    var btnElement = document.createElement('button');
    btnElement.setAttribute('class', 'btn btn-warning');
    btnElement.onclick = function () { sendCardRequest() };
    btnElement.style.width='40%';
    btnElement.innerHTML='Throw';

    throwOneBtnDiv.appendChild(btnElement)
}

function slideOutCard(cardDto) {
    var cardSpan = document.getElementById(cardDto.suit + '_' + cardDto.rank);
    var selectedCard = getSelectedCard(cardDto);
    cardSpan.style.marginBottom = selectedCard != null ? '0%' : '3%';
    if (selectedCard != null) selectedCards.splice(selectedCards.indexOf(selectedCard), 1)
    else selectedCards.push(cardDto);
}

function getSelectedCard(cardDto) {
    return selectedCards.filter(card => {
        return card.rank === cardDto.rank
            && card.suit === cardDto.suit
    }
    )[0];
}

function showThrowQuestion(cardDto, rankCards) {
    jQuery.noConflict();
    var cardsToThrowDiv = document.getElementById('cardsToThrow');
    cardsToThrowDiv.innerHTML = '';
    rankCards.forEach(card => {
        cardsToThrowDiv.appendChild(
            createCardDiv(card)
        );
    });
    document.getElementById('throwOneBtn').onclick = function () {
        throwAction(cardDto, false);
    };
    document.getElementById('throwAllBtn').onclick = function () {
        throwAction(cardDto, true);
    };

    $('#selectCardsModalFrag').modal('show');
}

function throwAction(cardDto, throwAll) {
    hideModal();
    sendCardRequest(cardDto, throwAll);
}

function hideModal() {
    $('#selectCardsModalFrag').modal('hide');
}

function createCardDiv(card) {
    var cardSpan = document.createElement('span');
    cardSpan.setAttribute('style', 'margin-left:1%;border-radius:5%; border: 2px solid black;width:15%;height:80%;display: inline-block;background-color:white;')

    var cardContent = document.createElement('div');
    cardContent.setAttribute('class', 'row');
    cardContent.setAttribute('style', 'margin-left:1%;');

    var cardRank = document.createElement('b');
    cardRank.setAttribute('style', 'font-size:1.1vw;color:' + getColor(card.suit));
    cardRank.innerHTML = getRankName(card.rank)
    cardContent.appendChild(cardRank);

    var suitIconDiv = document.createElement("div");
    suitIconDiv.setAttribute("class", "row");
    suitIconDiv.setAttribute("style", "width:100%;height:100%;text-align:center;margin: 0 auto;");

    var suitIcon = document.createElement("i");
    suitIcon.setAttribute("style", "margin: 0 auto;width:100%;text-align:center;font-size:1.5vw;color:" + getColor(card.suit))
    suitIcon.setAttribute("class", getSuitIconClass(card.suit));
    suitIconDiv.appendChild(suitIcon);

    cardContent.appendChild(suitIconDiv);

    cardSpan.appendChild(cardContent);
    return cardSpan;
}

function getSuitIconClass(suit) {
    switch (suit) {
        case 'CLUBS': return 'bi bi-suit-club-fill';
        case 'DIAMONDS': return 'bi bi-suit-diamond-fill';
        case 'HEARTS': return 'bi bi-suit-heart-fill';
        case 'SPADES': return 'bi bi-suit-spade-fill';
    }
}

function getRankName(rank) {
    switch (rank) {
        case 'NINE': return '9';
        case 'TEN': return '10';
        case 'JACK': return 'J';
        case 'QUEEN': return 'Q';
        case 'KING': return 'K';
        case 'ACE': return 'A';
    }
}

function getColor(suit) {
    if (suit === 'HEARTS' || suit === 'DIAMONDS') return 'red';
    else return 'black';
}

function sendCardRequest() {
    var xhr = new XMLHttpRequest();
    var url = '/panguingue/games/' + document.getElementById("gameId").value + '/move';
    xhr.open("POST", url, true);
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.onreadystatechange = function () { };
    var data = JSON.stringify(selectedCards);
    xhr.send(data);

    selectedCards = [];
}

function filterCards(rank, cards) {
    return cards.filter(card =>
        card.rank === rank
    );
}

function heartNineNotExists(cards) {
    var filteredCards = cards.filter(card =>
        card.rank === 'NINE'
        && card.suit === 'HEARTS'
    );
    return filteredCards.length == 0;
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
    var xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function () {
        if (xhr.readyState == XMLHttpRequest.DONE) {
            $("#board").replaceWith(xhr.responseText);
        }
    }
    xhr.open('GET', "/panguingue/games/" + document.getElementById("gameId").value + "/board/reload", true);
    xhr.send(null);
}

function takeCards() {
    var xhr = new XMLHttpRequest();
    var url = '/panguingue/games/' + document.getElementById("gameId").value + '/take-cards';
    xhr.open("POST", url, true);
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.onreadystatechange = function () { };
    xhr.send();
}