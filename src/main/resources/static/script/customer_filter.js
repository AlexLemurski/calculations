const STORAGE_KEYS = {
    //input search
    nameFilter: 'nameFilter',
    InnFilter: 'InnFilter',
    mainActivityFilter: 'mainActivityFilter',
};

const filters = {
    //input search
    customerName: document.getElementById('customerNameHead'),
    customerInn: document.getElementById('customerInnHead'),
    customerMainActivity: document.getElementById('mainActivityHead'),
};

const tableBodyEmp = document.querySelector('#table-menu-sort tbody');
const resetButtonEmp = document.getElementById('resetFilters');

function saveState() {
    localStorage.setItem(STORAGE_KEYS.nameFilter, filters.customerName.value);
    localStorage.setItem(STORAGE_KEYS.InnFilter, filters.customerInn.value);
    localStorage.setItem(STORAGE_KEYS.mainActivityFilter, filters.customerMainActivity.value);
}

function loadState() {
    const savedNameFilter = localStorage.getItem(STORAGE_KEYS.nameFilter);
    const savedInnFilter = localStorage.getItem(STORAGE_KEYS.InnFilter);
    const savedMainActivityFilter = localStorage.getItem(STORAGE_KEYS.mainActivityFilter);
    if (savedNameFilter) {
        filters.customerName.value = savedNameFilter;
    }
    if (savedInnFilter) {
        filters.customerInn.value = savedInnFilter;
    }
    if (savedMainActivityFilter) {
        filters.customerMainActivity.value = savedMainActivityFilter;
    }
}

function filterTable() {
    const rows = tableBodyEmp.querySelectorAll('tr');
    rows.forEach(row => {
        const cells = row.querySelectorAll('td');
        // Проверяем каждую колонку
        let showRow = true;
        // Фильтр по Наименования
        const nameVal = cells[1].textContent.toLowerCase();
        const nameFilterVal = filters.customerName.value.toLowerCase();
        if (nameFilterVal && !nameVal.includes(nameFilterVal)) showRow = false;
        // Фильтр по ИНН
        if (showRow) {
            const InnVal = cells[2].textContent.toLowerCase();
            const InnFilterVal = filters.customerInn.value.toLowerCase();
            if (InnFilterVal && !InnVal.includes(InnFilterVal)) showRow = false;
        }
        // Фильтр по Деятельности
        if (showRow) {
            const activityVal = cells[3].textContent.toLowerCase();
            const activityFilterVal = filters.customerMainActivity.value.toLowerCase();
            if (activityFilterVal && !activityVal.includes(activityFilterVal)) showRow = false;
        }
        row.style.display = showRow ? '' : 'none';
    });
}

function onInputChange() {
    saveState();
    filterTable();
}

// Сброс всех фильтров
function resetFilters() {
    filters.customerName.value = '';
    filters.customerInn.value = '';
    filters.customerMainActivity.value = '';
    localStorage.removeItem(STORAGE_KEYS.nameFilter);
    localStorage.removeItem(STORAGE_KEYS.InnFilter);
    localStorage.removeItem(STORAGE_KEYS.mainActivityFilter);
    filterTable(); // Обновляем таблицу после сброса
}

// Инициализация
loadState();
filterTable();
// Навешиваем обработчики на текстовые поля ввода
filters.customerName.addEventListener('input', onInputChange);
filters.customerInn.addEventListener('input', onInputChange);
filters.customerMainActivity.addEventListener('input', onInputChange);
// Обработчик для кнопки сброса
resetButtonEmp.addEventListener('click', resetFilters);