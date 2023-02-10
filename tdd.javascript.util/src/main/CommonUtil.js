/**
 * 以下方法因为练习皆不判空或undefined
 */
class CommonUtil {
    static listify(obj, mapFn) {
        let res = [];
        for (let [k, v] of Object.entries(obj)) {
            res.push(mapFn(k, v));
        }
        return res;
    }

    static objectify(arr, mapKey, mapValue = i => i) {
        let res = {};
        for (let map of arr) {
            res[mapKey(map)] = mapValue(map);
        }
        return res;
    }

    static unzip(arr) {
        const res = [];
        // 找到数组子元素最大长度
        let maxRowLength = 0;
        for (let i = 0; i < arr.length; i++) {
            maxRowLength = Math.max(maxRowLength, arr[i].length);
        }
        // 定义数组
        for (let j = 0; j < maxRowLength; j++) {
            res[j] = [];
        }
        // 填充数组
        for (let i = 0; i < arr.length; i++) {
            for (let j = 0; j < arr[i].length; j++) {
                res[j].push(arr[i][j]);
            }
        }
        return res;
    }

    static reducedFilter(data, keys, filterFn) {
        let res = [];
        data.forEach((item) => {
            if (filterFn(item)) {
                let map = {}
                keys.forEach((key) => {
                    map[key] = item[key];
                });
                res.push(map);
            }
        });
        return res;
    }

    static get(from, ...selectors) {
        let res = [];
        selectors.forEach((selector) => {
            const split = selector.replaceAll('[', '.').replaceAll(']', '').split('.');
            let child = from;
            split.forEach(item => {
                child = child[item];
            });
            res.push(child);
        })
        return res;
    }

    static set(from, paths, value) {
        const split = paths.replaceAll('[', '.').replaceAll(']', '').split('.');
        let child = from;
        for (let i = 0; i < split.length; i++) {
            const item = split[i];
            if (i === split.length - 1) {
                child[item] = value;
                break;
            }
            child = child[item];
        }
    }

    static pickBy(obj, valueFilterFn) {
        let res = {};
        for (let [k, v] of Object.entries(obj)) {
            if (valueFilterFn(v)) {
                res[k] = v;
            }

        }
        return res;
    }

    static filterEmptyValue(obj, valueFilterFn) {
        return this.omitBy(obj, valueFilterFn);
    }

    static omitBy(obj, valueFilterFn) {
        let res = {};
        for (let [k, v] of Object.entries(obj)) {
            if (!valueFilterFn(v)) {
                res[k] = v;
            }

        }
        return res;
    }

    // 通用版本不会。。。
    static dataFormat(date, format) {
        const char = "-";
        const colon = ":";
        const day = date.getDate();
        //注意月份需要+1
        const month = date.getMonth() + 1;
        const year = date.getFullYear();
        const h = date.getHours();
        const m = date.getMinutes();
        const s = date.getSeconds();
        //补全0，并拼接
        return year + char + this.completeDate(month) + char + this.completeDate(day)
            + " " + this.completeDate(h) + colon + this.completeDate(m) + colon + this.completeDate(s);
    }

    //补全0
     static completeDate(value) {
        return value < 10 ? "0" + value : value;
    }

    static parseCookie(cname) {
        const name = cname + "=";
        return document.cookie.split(';');
    }
}

module.exports = CommonUtil;
