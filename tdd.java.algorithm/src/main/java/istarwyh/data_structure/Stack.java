package istarwyh.data_structure;

public class Stack {

    private final Object[] elements = new Object[2];

    private int size = 0;

    public boolean isEmpty() {
        return size == 0;
    }

    public Object pop() {
        if(size == 0){
            throw new UnderFlowException();
        }
        return elements[--size];
    }

    public void push(Object anElement) {
        this.elements[size++] = anElement;
    }

    public int size() {
        return size;
    }
}
