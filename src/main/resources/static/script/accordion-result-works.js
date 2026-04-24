function updateButtonAndHeadersResultWorks() {
    const sections = document.querySelectorAll('.accordion-result-works');
    let allOpen = true;
    let allClosed = true;

    sections.forEach(section => {
        const header = section.querySelector('.accordion-header-result-works');
        const content = section.querySelector('.accordion-content-result-works');
        const isOpenResultWorks = content.style.display === 'block';

        if (isOpenResultWorks) {
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

document.querySelectorAll('.accordion-result-works').forEach(item => {
    const id = item.getAttribute('data-id');
    const header = item.querySelector('.accordion-header-result-works');
    const content = item.querySelector('.accordion-content-result-works');
    const savedState = localStorage.getItem('accordion_' + id);
    if (savedState === 'open') {
        content.style.display = 'block';
    } else {
        content.style.display = 'none';
    }
    header.addEventListener('click', () => {
        const isOpenResultWorks = content.style.display === 'block';
        content.style.display = isOpenResultWorks ? 'none' : 'block';
        localStorage.setItem('accordion_' + id, isOpenResultWorks ? 'close' : 'open');

        updateButtonAndHeadersResultWorks();
    });
});

window.addEventListener('load', () => {
    updateButtonAndHeadersResultWorks();
});