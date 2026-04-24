function parseDateStr(dateStr) {
    const regex = /(\d{2})-(\d{2})-(\d{4}) г\./;
    const match = dateStr.match(regex);
    if (match) {
        const day = parseInt(match[1], 10);
        const month = parseInt(match[2], 10) - 1; // месяцы с 0
        const year = parseInt(match[3], 10);
        return new Date(year, month, day);
    }
    return null;
}

function filterRows(startDateStr, endDateStr) {
    const rows = document.querySelectorAll('.left-table-body tr');
    const startDate = startDateStr ? new Date(startDateStr) : null;
    const endDate = endDateStr ? new Date(endDateStr) : null;
    rows.forEach(row => {
        const dateText = row.querySelector('td:nth-child(6)').innerText;
        if (dateText) {
            const rowDate = parseDateStr(dateText);
            if (rowDate) {
                let show = true;
                if (startDate && rowDate < startDate) {
                    show = false;
                }
                if (endDate && rowDate > endDate) {
                    show = false;
                }
                row.style.display = show ? '' : 'none';
            } else {
                row.style.display = '';
            }
        }
    });
}

document.getElementById('applyDateFilter').addEventListener('click', () => {
    const startDate = document.getElementById('startDate').value;
    const endDate = document.getElementById('endDate').value;
    localStorage.setItem('startDate', startDate);
    localStorage.setItem('endDate', endDate);
    filterRows(startDate, endDate);
});

document.getElementById('resetFilters').addEventListener('click', () => {
    document.getElementById('startDate').value = '';
    document.getElementById('endDate').value = '';
    localStorage.removeItem('startDate');
    localStorage.removeItem('endDate');
    const rows = document.querySelectorAll('.left-table-body tr');
    rows.forEach(row => {
        row.style.display = '';
    });
});

window.addEventListener('load', () => {
    const savedStart = localStorage.getItem('startDate') || '';
    const savedEnd = localStorage.getItem('endDate') || '';
    document.getElementById('startDate').value = savedStart;
    document.getElementById('endDate').value = savedEnd;
    if (savedStart || savedEnd) {
        filterRows(savedStart, savedEnd);
    }
});