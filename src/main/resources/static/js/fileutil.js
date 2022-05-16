/**
 * blob文件下载
 * @param fileBytes blob文件流
 * @param fileName 文件名称
 */
function downloadExcelFile(fileBytes, fileName) {
    // const blobFileType = 'application/vnd.ms-excel'
    const blobFileType = 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
    downloadFile(fileBytes, fileName, blobFileType)
}

/**
 * blob文件下载
 * @param fileBytes blob文件流
 * @param fileName 文件名称
 * @param blobFileType 下载文件的blob类型（字符串）
 */
function downloadFile(fileBytes, fileName, blobFileType) {
    if (!fileBytes && !fileName && blobFileType) {
        console.error('params{fileBytes, fileName, blobFileType} must be not null.')
        return
    }
    const blob = new Blob([fileBytes], {type: blobFileType});
    if (window.navigator.msSaveOrOpenBlob) { // 兼容IE10
        navigator.msSaveBlob(blob, fileName)
    } else {
        const a = document.createElement("a");
        const url = window.URL.createObjectURL(blob);
        a.href = url;
        a.download = fileName;
        a.click();
        window.URL.revokeObjectURL(url);
    }
}