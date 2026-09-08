document.getElementById('clearLS').addEventListener(
    'click',
    function () {
        localStorage.clear();
        console.log('LocalStorage очищен')
    });