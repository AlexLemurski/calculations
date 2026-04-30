const inputINN = document.getElementById('customerINNCodeCheck');
const inputKPP = document.getElementById('customerKPPCodeCheck');
const inputOGRN = document.getElementById('customerOGRNCodeCheck');

inputINN.addEventListener('input', function() {
    this.value = this.value.replace(/\D/g, '');
    if (this.value.length > 10) {
        this.value = this.value.slice(0, 10);
    }
    if (this.value.length < 10) {
        this.style.color = 'red';
    } else {
        this.style.color = 'green';
    }
});
inputKPP.addEventListener('input', function() {
    this.value = this.value.replace(/\D/g, '');
    if (this.value.length > 9) {
        this.value = this.value.slice(0, 9);
    }
    if (this.value.length < 9) {
        this.style.color = 'red';
    } else {
        this.style.color = 'green';
    }
});
inputOGRN.addEventListener('input', function() {
    this.value = this.value.replace(/\D/g, '');
    if (this.value.length > 13) {
        this.value = this.value.slice(0, 13);
    }
    if (this.value.length < 13) {
        this.style.color = 'red';
    } else {
        this.style.color = 'green';
    }
});