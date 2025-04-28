package com.example.algorithm;

import java.util.List;
import java.util.Random;

public class QuickSelect {

    public static int findKthSmallest(List<Integer> nums, int k) {
        return quickSelect(nums, 0, nums.size() - 1, k - 1);
    }

    private static int quickSelect(List<Integer> nums, int left, int right, int k) {
        if (left == right) {
            return nums.get(left);
        }

        Random random = new Random();
        int pivotIndex = left + random.nextInt(right - left + 1);
        pivotIndex = partition(nums, left, right, pivotIndex);

        if (k == pivotIndex) {
            return nums.get(k);
        } else if (k < pivotIndex) {
            return quickSelect(nums, left, pivotIndex - 1, k);
        } else {
            return quickSelect(nums, pivotIndex + 1, right, k);
        }
    }

    private static int partition(List<Integer> nums, int left, int right, int pivotIndex) {
        int pivotValue = nums.get(pivotIndex);
        swap(nums, pivotIndex, right);
        int storeIndex = left;

        for (int i = left; i < right; i++) {
            if (nums.get(i) < pivotValue) {
                swap(nums, storeIndex, i);
                storeIndex++;
            }
        }

        swap(nums, right, storeIndex);
        return storeIndex;
    }

    private static void swap(List<Integer> nums, int i, int j) {
        int temp = nums.get(i);
        nums.set(i, nums.get(j));
        nums.set(j, temp);
    }
}