const emailInput = document.getElementById('emailCheck');
const emailPattern = /^.+@.+\..+$/;
emailInput.addEventListener('input', function () {
    const value = this.value;
    if (emailPattern.test(value)) {
        this.style.color = 'green';  // Формат правильный
    } else {
        this.style.color = 'red';    // Формат неправильный
    }
});