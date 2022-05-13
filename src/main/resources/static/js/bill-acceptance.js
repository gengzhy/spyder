$(document).ready(function () {
    document.getElementById("showMonth").value = formatCurrentMonth(new Date())
})

/**
 * 文件下载
 */
function downloadExcelTemplate() {
    const data = new FormData()
    data.append("showMonth", showMonth)
    axios({
        method: 'post',
        url: 'download/template',
        responseType: 'blob'
    }).then(resp => {
        // 需使用decodeURIComponent解码
        downloadFile(resp.data, decodeURIComponent(resp.headers['excel-file-name']))
    }).catch(error => alert(error))
}

function fileUpload() {
    const files = document.getElementById("excel-file").files
    if (files.length === 0) {
        alert("上传的文件为空")
        return
    }
    const data = new FormData()
    data.append("file", files[0])
    axios({
        method: 'post',
        url: 'upload',
        data: data,
        responseType: 'json'
    }).then(resp => {
        alert(resp.data.message)
    }).catch(error => alert(error))
}

/**
 * 文件下载
 */
function downloadExcel() {
    const showMonth = $("#showMonth").val();
    if (showMonth === 'undefined' || showMonth === '') {
        alert("{披露信息时点日期}不能为空！")
        return
    }
    const data = new FormData()
    data.append("showMonth", showMonth)
    axios({
        method: 'post',
        url: 'download',
        data: data,
        responseType: 'blob'
    }).then(resp => {
        // 需使用decodeURIComponent解码
        let fileName = resp.headers['excel-file-name']
        fileName = fileName ? decodeURIComponent(fileName) : "下载错误"
        downloadFile(resp.data, fileName)
    }).catch(error => alert(error))
}

/**
 * blob文件下载
 * @param content blob文件流
 * @param fileName 文件名称
 */
function downloadFile(content, fileName) {
    // const blob = new Blob([content], { type: 'application/vnd.ms-excel' });
    const blob = new Blob([content], {type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'});
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

/**
 * 是否为JSON对象
 * @param obj
 * @returns {boolean}
 */
function isJSON(obj) {
    return typeof (obj) == "object" && Object.prototype.toString.call(obj).toLowerCase() === "[object object]" && !obj.length;
}

// 上一个年月
function formatLastMonth(date) {
    let y = date.getFullYear()
    let m = date.getMonth()
    const ym = y + "-" + (m < 10 ? '0' + m : m)
    return ym
}

// 当前年月
function formatCurrentMonth(date) {
    let y = date.getFullYear()
    let m = date.getMonth() + 1
    const ym = y + "-" + (m < 10 ? '0' + m : m)
    return ym
}