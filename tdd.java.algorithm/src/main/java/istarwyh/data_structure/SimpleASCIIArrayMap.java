package istarwyh.data_structure;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author mac
 */
public class SimpleASCIIArrayMap {

  private final int[] array = new int[128];

  public SimpleASCIIArrayMap() {
    Arrays.fill(array, -1);
  }

  /**
   * @param key ASCII
   * @return the last occurrence index of key in the original string.
   */
  public int get(char key) {
    return array[key];
  }

  /**
   * @param key ASCII
   * @param index the last occurrence index of key in the original string.
   */
  public void put(char key, int index) {
    array[key] = index;
  }
}

class Solution {
  public int maxSubarrayLength(int[] nums, int k) {
    FrequencyMap map =  new FrequencyMap();
    int left = 0;
    int maxLen = 0;
    for(int right = 0; right < nums.length; right ++){
      map.add(nums[right]);
      if(map.get(nums[left]) > k){
        while(map.get(nums[left]) == k){
          map.remove(nums[left]);
          left +=1;
        }
      }
      maxLen = Math.max(maxLen,right - left + 1);
    }
    return maxLen;
  }

  class FrequencyMap{
    Map<Integer,Integer> map = new HashMap<>(2048);

    public void add(int key){
      map.put(key, get(key) + 1);
    }

    public void remove(int key){
      int value = get(key);
      if(value > 0){
        map.put(key,value - 1);
      }
    }

    public int get(int key){
      return map.getOrDefault(key,0);
    }
  }
}
