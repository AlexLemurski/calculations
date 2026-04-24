document.addEventListener("DOMContentLoaded", function () {
    const fileNames = document.querySelectorAll('.file-name');

    function getColorByExtension(fileName) {
        const ext = fileName.toLowerCase().split('.').pop();

        switch (ext) {
            case 'xls':
            case 'xlsx':
            case 'xlsm':
            case 'xlsb':
            case 'xltx':
                return 'Green';
            case 'pdf':
                return 'Red';
            case 'doc':
            case 'docx':
            case 'docm':
            case 'dotx':
            case 'dotm':
                return 'MediumBlue';
            case 'rar':
            case 'zip':
            case '7z':
            case 'tgz':
            case 'tar.gz':
            case 'cab':
            case 'iso':
                return 'DarkOrange'
            default:
                return 'Black';
        }
    }

    fileNames.forEach(function (element) {
        const name = element.textContent.trim();
        element.style.color = getColorByExtension(name);
    });
});