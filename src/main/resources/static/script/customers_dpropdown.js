function toggleDropdown() {
    document.getElementById('customerDropdown').classList.toggle('open');
}

window.addEventListener('click', function(e){
    const dropdown = document.getElementById('customerDropdown');
    if (!dropdown.contains(e.target)) {
        dropdown.classList.remove('open');
    }
});