function toggleDropdownDateOfCreate() {
    document.getElementById('dateOfCreateDropdown').classList.toggle('open');
}

window.addEventListener('click', function(e){
    const dropdown = document.getElementById('dateOfCreateDropdown');
    if (!dropdown.contains(e.target)) {
        dropdown.classList.remove('open');
    }
});