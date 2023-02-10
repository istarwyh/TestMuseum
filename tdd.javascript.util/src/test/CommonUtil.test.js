const {
    get,
    listify,
    objectify,
    parseCookie,
    pickBy,
    reducedFilter,
    set,
    unzip
} = require("../main/CommonUtil");
const CommonUtil = require("../main/CommonUtil");

const people = {John: {age: 25}, Adam: {age: 30}};
const peopleArr = [{name: "John", age: 25}, {name: "Adam", age: 30}];
const selectedObj = {
    selector: {to: {val: 'val to select'}},
    target: [1, 2, {val: 'test'}]
}

test("map an object to an array happy case", () => {
    expect(
        listify(people, (key, value) => ({name: key, ...value}))
    ).toMatchObject(peopleArr);
});

test("map an array to an object with identity as default mapValue  ", () => {
    expect(
        objectify(
            peopleArr,
            p => p.name.toLowerCase()
        )
    ).toMatchObject({john: {name: 'John', age: 25}, adam: {name: 'Adam', age: 30}});
});

test("map an array to an object happy case", () => {
    expect(
        objectify(
            peopleArr,
            p => p.name.toLowerCase(),
            p => p.age
        )
    ).toMatchObject({john: 25, adam: 30});
});

test("ungroup array elements happy case", () => {
    expect(
        unzip([['a', 1, true], ['b', 2, false]])
    ).toMatchObject([['a', 'b'], [1, 2], [true, false]]);
});

test("ungroup array elements with different children element length", () => {
    expect(
        unzip([['a', 1, true], ['b', 2]])
    ).toMatchObject([['a', 'b'], [1, 2], [true]]);
});

test("filter matching and unspecified values with one field", () => {
    expect(
        reducedFilter(peopleArr, ['name'], p => p.age > 25)
    ).toMatchObject([{name: 'Adam'}]);
});

test("filter matching and unspecified values with two fields", () => {
    expect(
        reducedFilter(peopleArr, ['name', 'age'], p => p.age > 25)
    ).toMatchObject([{name: 'Adam', age: 30}]);
});

test("should get value with input string type selectors", () => {
    expect(
        get(selectedObj, 'selector.to.val', 'target[0]', 'target[2].val')
    ).toMatchObject(['val to select', 1, 'test']);
});

test("should set value with paths and value happy case", () => {
    set(selectedObj, 'selector.to.val', 'hello world');
    expect(get(selectedObj, 'selector.to.val')).toMatchObject(['hello world']);
});

test("pick matching object keys", () => {
    expect(
        pickBy({a: 1, b: "~~~", c: 3}, x => typeof x === 'number')
    ).toMatchObject({'a': 1, 'c': 3});
});

test("filter empty value", () => {
    expect(
        CommonUtil.filterEmptyValue({a: 1, b: "~~~", c: null, d: '', e: false}, x => x === '')
    ).toMatchObject({a: 1, b: "~~~", c: null, e: false});
});


test("data format happy case", () => {
    expect(
        CommonUtil.dataFormat(new Date(2023, 1, 1,8,18,28), 'YYYY-MM-DD HH:mm:ss')
    ).toBe('2023-02-01 08:18:28');
});

// test("parse cookie happy case 1", () => {
//     expect(
//         parseCookie('foo=bar; equation=E%3Dmc%5E2')
//     ).toBe({foo: 'bar', equation: 'E=mc^2'});
// });
