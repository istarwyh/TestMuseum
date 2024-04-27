package istarwyh.leetcode;

/**
 * @author mac
 */
class Solution {

  public static void main(String[] args) {
    long l = new Solution().maximumSubarraySum(new int[] {1, 5, 4, 2, 9, 9, 9}, 3);
    System.out.println(l);
  }

  /** 1 <= k <= nums.length <= 10^5 1 <= nums[i] <= 10^5 */
  public long maximumSubarraySum(int[] nums, int k) {
    ExistMap existMap = new ExistMap();
    int left = 0;
    long maxSum = 0;
    long curSum = 0;
    for (int right = 0; right < nums.length; right++) {
      int rightNum = nums[right];
      curSum += rightNum;
      while (left <= right && (right - left + 1 > k || isOccurred(existMap, rightNum))) {
        int leftNum = nums[left];
        curSum -= leftNum;
        existMap.remove(leftNum);
        left++;
      }
      existMap.add(rightNum);
      if (right - left + 1 == k) {
        maxSum = Math.max(maxSum, curSum);
      }
    }
    return maxSum;
  }

  private static boolean isOccurred(ExistMap existMap, int key) {
    return existMap.get(key) == 1;
  }

  static class ExistMap {
    int[] map = new int[10001];

    public void add(int key) {
      map[key] = 1;
    }

    public int get(int key) {
      return map[key];
    }

    public void remove(int key) {
      map[key] = 0;
    }
  }

}
