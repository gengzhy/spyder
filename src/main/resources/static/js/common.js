/**
 * 是否为JSON对象
 * @param obj
 * @returns {boolean}
 */
function isJSON(obj) {
    return typeof (obj) == "object" && Object.prototype.toString.call(obj).toLowerCase() === "[object object]" && !obj.length;
}

/**
 * 取当前日期的上一个月
 * @returns {string} - yyyy-MM-dd
 */
function getCurrentDate() {
    const date = new Date();
    let y = date.getFullYear()
    let m = date.getMonth() + 1
    let d = date.getDate()
    const ym = y + "-" + (m < 10 ? '0' + m : m) + '-' + (d < 10 ? '0' + d : d)
    return ym
}

/**
 * 取当前日期的上一个月
 * @returns {string} - yyyy-MM`
 */
function getLastMonth() {
    const date = new Date();
    let y = date.getFullYear()
    let m = date.getMonth()
    const ym = y + "-" + (m < 10 ? '0' + m : m)
    return ym
}

/**
 * 取当当前日期的上一个月
 * @returns {string} - yyyy-MM
 */
function getCurrentMonth() {
    const date = new Date();
    let y = date.getFullYear()
    let m = date.getMonth() + 1
    return y + "-" + (m < 10 ? '0' + m : m)
}