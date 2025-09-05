package ru.nsu.masolygin;


public class Heapsort {

    public static void heapsort(int[] arr){
        int n = arr.length;
        for (int i = n/2-1; i >=0 ; i--) {
            shiftDown(arr,n,i);
        }
        for (int i = n-1; i >=0 ; i--) {
            swap(arr, 0, i);
            shiftDown(arr,i,0);
        }
    }

    public static void swap(int[] arr, int i, int j){
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    public static void shiftDown(int[] arr, int n, int i){
        int root = i;
        int left=2*i+1;
        int right=2*i+2;


        if (left<n &&arr[left]>arr[root]){
            root = left;
        }
        if (right<n && arr[right]>arr[root]){
            root = right;
        }

        if(root != i){
            swap(arr, i, root);
            shiftDown(arr,n,root);
        }
    }

}