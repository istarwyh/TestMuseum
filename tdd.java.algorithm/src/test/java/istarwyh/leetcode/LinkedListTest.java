package istarwyh.leetcode;

import com.github.istarwyh.ListNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class LinkedListTest {

  private LinkedList linkedList;
  private ListNode node1;

  private ListNode oddNodeListHead;
  private ListNode evenNodeListHead;

  @BeforeEach
  void setUp() {
    linkedList = new LinkedList();
    node1 = new ListNode(1);
    oddNodeListHead = ListNode.createNodeList(1, 2, 3, 4, 5);
    evenNodeListHead = ListNode.createNodeList(1, 2, 3, 4);
  }

  @Nested
  class ReorderList {

    /**
     * Actually it is difficult to verify do nothing in {@link LinkedList#reorderList(ListNode)}
     * when we don't know what exact implement in it
     */
    @Test
    void should_do_nothing_when_input_head_is_null() {
      linkedList.reorderList(null);
    }

    @Test
    void should_return_the_same_when_input_one_node() {
      ListNode input = node1;
      linkedList.reorderList(input);
      assertSame(node1, input);
    }

    @Test
    void should_pass_when_the_length_is_odd() {
      linkedList.reorderList(oddNodeListHead);

      assertEquals(
          "{ val:1  next:{ val:5  next:{ val:2  next:{ val:4  next:{ val:3  next:null}}}}}",
          oddNodeListHead.toString());
    }

    @Test
    void should_pass_when_the_length_is_even() {
      linkedList.reorderList(evenNodeListHead);

      assertEquals(
          "{ val:1  next:{ val:4  next:{ val:2  next:{ val:3  next:null}}}}",
          evenNodeListHead.toString());
    }

    @Nested
    class GetFirstPartEndNodeAsSplitNode {

      @Test
      void should_get_first_part_end_node_as_split_node_when_the_length_is_odd() {
        ListNode splitNode = linkedList.getFirstPartEndNodeAsSplitNode(oddNodeListHead);
        assertEquals(
            "{ val:2  next:{ val:3  next:{ val:4  next:{ val:5  next:null}}}}",
            splitNode.toString());
      }

      @Test
      void should_get_first_part_end_node_as_split_node_when_the_length_is_even() {
        ListNode splitNode = linkedList.getFirstPartEndNodeAsSplitNode(evenNodeListHead);
        assertEquals("{ val:2  next:{ val:3  next:{ val:4  next:null}}}", splitNode.toString());
      }
    }

    @Nested
    class GetFirstPart {
      @Test
      void should_cult_to_first_part() {
        ListNode firstPart = linkedList.getFirstPartHeadNode(oddNodeListHead, oddNodeListHead.next);
        assertEquals("{ val:1  next:{ val:2  next:null}}", firstPart.toString());
      }
    }

    @Nested
    class GetSecondPart {

      @Test
      void should_directly_reverse_odd_list() {
        ListNode listNode = linkedList.getSecondPartHeadNode(oddNodeListHead);
        assertEquals(
            "{ val:5  next:{ val:4  next:{ val:3  next:{ val:2  next:{ val:1  next:null}}}}}",
            listNode.toString());
      }

      @Test
      void should_directly_reverse_even_list() {
        ListNode listNode = linkedList.getSecondPartHeadNode(evenNodeListHead);
        assertEquals(
            "{ val:4  next:{ val:3  next:{ val:2  next:{ val:1  next:null}}}}",
            listNode.toString());
      }
    }

    @Nested
    class Merge {

      @Test
      void should_joint_two_node_List_in_turn_with_order_even_odd() {
        linkedList.merge(evenNodeListHead, oddNodeListHead);
        assertEquals(
            "{ val:1  next:{ val:1  next:{ val:2  next:{ val:2  next:{ val:3  "
                + "next:{ val:3  next:{ val:4  next:{ val:4  next:{ val:5  next:null}}}}}}}}}",
            evenNodeListHead.toString());
      }

      @Test
      void should_joint_two_node_List_in_turn_with_order_odd_even() {
        linkedList.merge(oddNodeListHead, evenNodeListHead);
        assertEquals(
            "{ val:1  next:{ val:1  next:{ val:2  next:{ val:2  next:{ val:3  "
                + "next:{ val:3  next:{ val:4  next:{ val:4  next:{ val:5  next:null}}}}}}}}}",
            oddNodeListHead.toString());
      }
    }
  }
}
