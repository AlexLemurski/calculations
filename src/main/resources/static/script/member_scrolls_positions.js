function restoreScrollPosition() {
    const tables = document.querySelectorAll('.left-content, .right-content');
    tables.forEach(table => {
        const id = table.id;
        const scrollPosition = localStorage.getItem(id);
        if (scrollPosition) {
            table.scrollTop = parseInt(scrollPosition, 10);
        }
    });
}

function saveScrollPosition() {
    const tables = document.querySelectorAll('.left-content, .right-content');
    tables.forEach(table => {
        const id = table.id;
        localStorage.setItem(id, table.scrollTop);
    });
}

window.onload = restoreScrollPosition;

const tables = document.querySelectorAll('.left-content, .right-content');
tables.forEach(table => {
    table.addEventListener('scroll', saveScrollPosition);
});