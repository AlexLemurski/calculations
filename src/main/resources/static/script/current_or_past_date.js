const dateOfCreate = document.getElementById('dateOfCreate');
const today = new Date();
const year = today.getFullYear();
const month = ('0' + (today.getMonth() + 1)).slice(-2);
const day = ('0' + today.getDate()).slice(-2);
const todayStr = `${year}-${month}-${day}`;
dateOfCreate.setAttribute('max', todayStr);