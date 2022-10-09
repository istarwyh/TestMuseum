package istarwyh.leetcode;

import com.github.istarwyh.ListNode;

public class LinkedList {

    /**
     * <a href="https://leetcode.cn/problems/reorder-list/">reorder-list</a>
     * @param head {@link ListNode} head node
     */
    public void reorderList(ListNode head) {
        if (head == null || head.next == null)
            return;
        ListNode splitNode = getFirstPartEndNodeAsSplitNode(head);
        ListNode secondPartStartNode = splitNode.next;
        ListNode l1 = getFirstPartHeadNode(head,splitNode);
        ListNode l2 = getSecondPartHeadNode(secondPartStartNode);
        merge(l1, l2);
    }

    /**
     *将要反转的链表看为两部分,返回第一部分尾结点
     */
    public ListNode getFirstPartEndNodeAsSplitNode(ListNode head){
        ListNode prev = null, slow = head, fast = head;
        while (fast != null && fast.next != null) {
            prev = slow;
            slow = slow.next;
            fast = fast.next.next;
        }
        return prev;
    }

    /**
     * 切割得到第一部分链表
     * @param head 链表原始头结点
     * @param endNode 切割后第一部分链表尾结点
     * @return ListNode
     */
    public ListNode getFirstPartHeadNode(ListNode head, ListNode endNode) {
        endNode.next = null;
        return head;
    }

    public ListNode getSecondPartHeadNode(ListNode head) {
        if (head == null)
            return null;
        ListNode prev = null;
        ListNode cur = head;
        while (cur != null) {
            final ListNode next = cur.next;

            pointToPreNode(prev, cur);

            prev = cur;
            cur = next;
        }
        return prev;
    }

    private void pointToPreNode(ListNode prev, ListNode cur) {
        cur.next = prev;
    }

    /**
     * 交替相连接结点,新链表 以 l1 为头结点
     * @param l1 head所在的链表，同时也是切割后第一部分链表
     * @param l2 切割后第二部分链表
     */
    public void merge(ListNode l1, ListNode l2) {
        while (l1 != null && l2 != null) {
            final ListNode l1Next = l1.next, l2Next = l2.next;
            jointInTurn(l1, l2, l1Next,l2Next);

            l1 = l1Next;
            l2 = l2Next;
        }
    }

    private void jointInTurn(ListNode l1, ListNode l2, ListNode l1Next,ListNode l2Next) {
        l1.next = l2;
        // when l1Next is null, we don't need to joint them in turn just pointing to end node -- l2Next
        if(l1Next == null){
            l2.next = l2Next;
        }else {
            l2.next = l1Next;
        }
    }
}
