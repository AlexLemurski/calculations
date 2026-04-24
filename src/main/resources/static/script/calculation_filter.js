const STORAGE_KEYS = {
    //input search
    lotFilter: 'lotFilter',
    projectNameFilter: 'projectNameFilter',
    locationFilter: 'locationFilter',
    //checkboxes
    customerFilter: 'customerFilter',
};

const filters = {
    //input search
    lotName: document.getElementById('lotName'),
    projectName: document.getElementById('projectName'),
    location: document.getElementById('location'),
    //checkboxes
    customerCheckboxes: document.querySelectorAll('.customer-filter'),
};

const tableBodyEmp = document.querySelector('#table-menu-sort tbody');
const resetButtonEmp = document.getElementById('resetFilters');

function saveState() {
    localStorage.setItem(STORAGE_KEYS.lotFilter, filters.lotName.value);
    localStorage.setItem(STORAGE_KEYS.projectNameFilter, filters.projectName.value);
    localStorage.setItem(STORAGE_KEYS.locationFilter, filters.location.value);
    const customerValues = Array.from(filters.customerCheckboxes)
        .filter(checkbox => checkbox.checked)
        .map(checkbox => checkbox.value);
    localStorage.setItem(STORAGE_KEYS.customerFilter, JSON.stringify(customerValues));
}

function loadState() {
    const savedLotFilter = localStorage.getItem(STORAGE_KEYS.lotFilter);
    const savedProjectNameFilter = localStorage.getItem(STORAGE_KEYS.projectNameFilter);
    const savedLocationFilter = localStorage.getItem(STORAGE_KEYS.locationFilter);
    if (savedLotFilter) {
        filters.lotName.value = savedLotFilter;
    }
    if (savedProjectNameFilter) {
        filters.projectName.value = savedProjectNameFilter;
    }
    if (savedLocationFilter) {
        filters.location.value = savedLocationFilter;
    }
    const customerFilters = JSON.parse(localStorage.getItem(STORAGE_KEYS.customerFilter)) || [];
    filters.customerCheckboxes.forEach(checkbox => {
        checkbox.checked = customerFilters.includes(checkbox.value);
    });
}

function filterTable() {
    const rows = tableBodyEmp.querySelectorAll('tr');
    rows.forEach(row => {
        const cells = row.querySelectorAll('td');
        // Проверяем каждую колонку
        let showRow = true;
        // Фильтр по Лоту
        const lotVal = cells[1].textContent.toLowerCase();
        const lotFilterVal = filters.lotName.value.toLowerCase();
        if (lotFilterVal && !lotVal.includes(lotFilterVal)) showRow = false;
        // Фильтр по Проекту
        if (showRow) {
            const projectNameVal = cells[2].textContent.toLowerCase();
            const projectNameFilterVal = filters.projectName.value.toLowerCase();
            if (projectNameFilterVal && !projectNameVal.includes(projectNameFilterVal)) showRow = false;
        }
        // Фильтр по Локации
        if (showRow) {
            const locationVal = cells[4].textContent.toLowerCase();
            const locationFilterVal = filters.location.value.toLowerCase();
            if (locationFilterVal && !locationVal.includes(locationFilterVal)) showRow = false;
        }
        // Фильтр по Заказчику
        if (showRow) {
            const customerVal = cells[3].textContent;
            const selectedCustomerValues = Array.from(filters.customerCheckboxes)
                .filter(checkbox => checkbox.checked)
                .map(checkbox => checkbox.value);
            if (selectedCustomerValues.length > 0 && !selectedCustomerValues
                .some(e => customerVal.includes(e))) showRow = false;
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
    filters.lotName.value = '';
    filters.projectName.value = '';
    filters.location.value = '';
    filters.customerCheckboxes.forEach(checkbox => checkbox.checked = false);
    localStorage.removeItem(STORAGE_KEYS.lotFilter);
    localStorage.removeItem(STORAGE_KEYS.projectNameFilter);
    localStorage.removeItem(STORAGE_KEYS.locationFilter);
    localStorage.removeItem(STORAGE_KEYS.customerFilter);
    filterTable(); // Обновляем таблицу после сброса
}

// Инициализация
loadState();
filterTable();
// Навешиваем обработчики на текстовые поля ввода
filters.lotName.addEventListener('input', onInputChange);
filters.projectName.addEventListener('input', onInputChange);
filters.location.addEventListener('input', onInputChange);
// Навешиваем обработчики на чекбоксы
filters.customerCheckboxes.forEach(checkbox =>
    checkbox.addEventListener('change', () => {
        saveState();
        filterTable();
    })
);
// Обработчик для кнопки сброса
resetButtonEmp.addEventListener('click', resetFilters);