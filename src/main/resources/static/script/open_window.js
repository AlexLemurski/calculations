document.addEventListener('DOMContentLoaded', () => {
    document.querySelectorAll('.clickable-row').forEach(row => {
        row.addEventListener('click', () => {
            const url = row.getAttribute('data-url');
            if (url) {
                window.location.href = url;
            }
        });
    });
});