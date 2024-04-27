from typing import List


class Solution:
    def sortColors(self, nums: List[int]) -> None:
        """
        Do not return anything, modify nums in-place instead.
        """
        zero = nums.count(0)
        one = nums.count(1)
        two = len(nums) - zero - one
        nums[:zero] = [0] * zero
        nums[zero: zero + one] = [1] * one
        nums[one + zero:] = [2] * two
