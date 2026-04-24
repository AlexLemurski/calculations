function updateButtonAndHeadersResultMaterials() {
    const sections = document.querySelectorAll('.accordion-result-materials');
    let allOpen = true;
    let allClosed = true;

    sections.forEach(section => {
        const header = section.querySelector('.accordion-header-result-materials');
        const content = section.querySelector('.accordion-content-result-materials');
        const isOpenResultMaterials = content.style.display === 'block';

        if (isOpenResultMaterials) {
            header.classList.add('open');
            header.style.backgroundColor = '#81C0BB'; // зелёный
            allClosed = false;
        } else {
            header.classList.remove('open');
            header.style.backgroundColor = 'lightgray'; // серый
            allOpen = false;
        }
    });
}

document.querySelectorAll('.accordion-result-materials').forEach(item => {
    const id = item.getAttribute('data-id');
    const header = item.querySelector('.accordion-header-result-materials');
    const content = item.querySelector('.accordion-content-result-materials');
    const savedState = localStorage.getItem('accordion_' + id);
    if (savedState === 'open') {
        content.style.display = 'block';
    } else {
        content.style.display = 'none';
    }
    header.addEventListener('click', () => {
        const isOpenResultMaterials = content.style.display === 'block';
        content.style.display = isOpenResultMaterials ? 'none' : 'block';
        localStorage.setItem('accordion_' + id, isOpenResultMaterials ? 'close' : 'open');

        updateButtonAndHeadersResultMaterials();
    });
});

window.addEventListener('load', () => {
    updateButtonAndHeadersResultMaterials();
});