window.onload = function() {
    const inputElement = document.querySelector('.input');
    const todoContainer = document.querySelector('.items');

    function createTodoItem(text) {
        const uuid = `data-${String(Math.random()).slice(2)}}`;
        return `
        <div class="item" data-uuid="${uuid}">
            <i class="active"></i>
            <span class="text">${text}</span>
            <img src="images/knife-left.svg" alt="knife-left-todos" class="deleted">
        </div>
        `;
    }

    inputElement.addEventListener('keypress', (event) => {
        if(event.key === 'Enter') {
            const target = event.target;
            // 获取输入框中的值后清空输入框
            const value = target.value;
            target.value = '';

            todoContainer.innerHTML += createTodoItem(value);
        }
    })

    todoContainer.addEventListener('click', (event) => {
        const target = event.target;
        const targetParent = target.parentNode;
        // 如果点击了展示的todo,说明动作为完成/取消完成
        const classList = target.classList;
        if(classList.contains('active')) {
            const parentClassList = targetParent.classList;
            const isCompleted = parentClassList.contains('completed');
            if(isCompleted) {
                parentClassList.remove('completed');
            }else{
                parentClassList.add('completed');
            }
        }
        // 如果点击了删除的图标，说明需要删除该todo,也即当前图标的父元素
        else if(classList.contains('deleted')) {
            todoContainer.removeChild(targetParent);
        }
    })
}