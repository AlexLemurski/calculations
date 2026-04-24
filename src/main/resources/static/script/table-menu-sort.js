document.addEventListener('DOMContentLoaded', () => {
    const table = document.getElementById('table-menu-sort');
    const resetButton = document.getElementById('resetFilters');

    function saveSort(column, order) {
        localStorage.setItem('sort', JSON.stringify({column, order}));
    }

    function loadSort() {
        const savedSort = JSON.parse(localStorage.getItem('sort'));
        if (savedSort) {
            sortTable(savedSort.column, savedSort.order);
            updateSortClasses(savedSort.column, savedSort.order);
        }
    }

    function updateSortClasses(column, order) {
        const buttons = table.querySelectorAll('button[data-column]');
        buttons.forEach(button => {
            button.classList.remove('asc', 'desc');
            if (button.getAttribute('data-column') === column) {
                button.classList.add(order);
            }
        });
    }

    function parseDate(dateString) {
        const parts = dateString.replace(' г.', '').split('-');
        if (parts.length === 3) {
            return new Date(parseInt(parts[2]), parseInt(parts[1]) - 1, parseInt(parts[0]));
        }
        return null;
    }

    function sortTable(column, order) {
        const tbody = table.querySelector('tbody');
        const rows = Array.from(tbody.rows);
        const headers = Array.from(table.querySelectorAll('th'));
        let columnIndex = -1;
        let headerElement = null;
        for (let i = 0; i < headers.length; i++) {
            if (headers[i].getAttribute('data-column') === column) {
                columnIndex = i;
                headerElement = headers[i];
                break;
            }
        }
        rows.sort((a, b) => {
            const cellA = a.cells[columnIndex];
            const cellB = b.cells[columnIndex];
            let valA = cellA.textContent.trim();
            let valB = cellB.textContent.trim();
            if (column === 'dateOfCreateMenu') {
                const dateA = parseDate(valA);
                const dateB = parseDate(valB);
                if (dateA && dateB) {
                    return (order === 'asc' ? dateA.getTime() - dateB.getTime() : dateB.getTime() - dateA.getTime());
                } else {
                    return (order === 'asc' ? valA.localeCompare(valB) : valB.localeCompare(valA));
                }
            } else {
                return (order === 'asc' ? valA.localeCompare(valB) : valB.localeCompare(valA));
            }
        });
        rows.forEach(row => tbody.appendChild(row));
    }

    document.querySelectorAll('th button[data-column]').forEach(button => {
        button.addEventListener('click', () => {
            const column = button.getAttribute('data-column');
            let order = 'asc';
            if (button.classList.contains('asc')) {
                order = 'desc';
            } else if (button.classList.contains('desc')) {
                order = 'asc';
            }

            sortTable(column, order);
            saveSort(column, order);
            updateSortClasses(column, order);
        });
    });
    resetButton.addEventListener('click', () => {
        localStorage.removeItem('sort');
        location.reload();
    });
    loadSort();
});
