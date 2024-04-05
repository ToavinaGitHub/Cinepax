
setTimeout(function() {
    var alertMessage = document.getElementById('alertMessage');
    alertMessage.classList.add('fade'); // Ajouter la classe fade pour déclencher la transition CSS
    setTimeout(function() {
        alertMessage.remove(); // Supprimer l'alerte après la transition CSS
    }, 1000); // Attendre 1 seconde avant de supprimer l'élément après la transition
}, 3000);

