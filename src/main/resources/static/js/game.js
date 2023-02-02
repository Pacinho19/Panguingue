function selectCard(cardDto){
console.log( JSON.stringify({
                suit:cardDto.suit,
                rank:cardDto.rank
            }));

      $.ajax({
            url: '/panguingue/games/'+ document.getElementById("gameId").value +'/move',
            type: 'POST',
            data: { json: JSON.stringify({
                suit:cardDto.suit,
                rank:cardDto.rank
            })},
            dataType: 'json'
        });
}