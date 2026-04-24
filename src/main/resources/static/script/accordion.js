function updateButtonAndHeaders() {
    const sections = document.querySelectorAll('.accordion');
    let allOpen = true;
    let allClosed = true;

    sections.forEach(section => {
        const header = section.querySelector('.accordion-header');
        const content = section.querySelector('.accordion-content');
        const isOpen = content.style.display === 'block';

        if (isOpen) {
            header.classList.add('open');
            header.style.backgroundColor = '#81C0BB'; // зелёный
            allClosed = false;
        } else {
            header.classList.remove('open');
            header.style.backgroundColor = 'lightgray'; // серый
            allOpen = false;
        }
    });

    const btnExpand = document.getElementById('expandAll');
    const btnCollapse = document.getElementById('collapseAll');

    if (allOpen) {
        btnExpand.classList.add('active');
    } else {
        btnExpand.classList.remove('active');
    }

    if (allClosed) {
        btnCollapse.classList.add('active');
    } else {
        btnCollapse.classList.remove('active');
    }
}

document.querySelectorAll('.accordion').forEach(item => {
    const id = item.getAttribute('data-id');
    const header = item.querySelector('.accordion-header');
    const content = item.querySelector('.accordion-content');
    const savedState = localStorage.getItem('accordion_' + id);
    if (savedState === 'open') {
        content.style.display = 'block';
    } else {
        content.style.display = 'none';
    }

    header.addEventListener('click', () => {
        const isOpen = content.style.display === 'block';
        content.style.display = isOpen ? 'none' : 'block';
        localStorage.setItem('accordion_' + id, isOpen ? 'close' : 'open');

        updateButtonAndHeaders();
    });
});

document.getElementById('expandAll').addEventListener('click', () => {
    document.querySelectorAll('.accordion').forEach(item => {
        const id = item.getAttribute('data-id');
        const content = item.querySelector('.accordion-content');
        content.style.display = 'block';
        localStorage.setItem('accordion_' + id, 'open');
    });
    updateButtonAndHeaders();
});

document.getElementById('collapseAll').addEventListener('click', () => {
    document.querySelectorAll('.accordion').forEach(item => {
        const id = item.getAttribute('data-id');
        const content = item.querySelector('.accordion-content');
        content.style.display = 'none';
        localStorage.setItem('accordion_' + id, 'close');
    });
    updateButtonAndHeaders();
});

window.addEventListener('load', () => {
    updateButtonAndHeaders();
});