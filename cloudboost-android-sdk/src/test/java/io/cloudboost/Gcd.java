package io.cloudboost;

import java.util.ArrayList;
import java.util.List;


public class Gcd {

	static int count = 0;
	public static int recursivelySwitch(int[] a) {
		int len = a.length;
		int[] b =a;

		int k=getK(b, 5);
		if(k!=0){
			return k;
		}
		for (int i = 0; i < len; i++) {
			int newInd = i - 1;
			if (newInd < 0)
				newInd = len + newInd;
			b[newInd] = a[i];
		}
		count++;
		if (count == len)
			return 0;
		else
			recursivelySwitch(b);
		
		return 0;

	}
	public static int getK(int[] b, int x){
		// look for K
		int eq=0;
		int neq=0;
		List<int[]> arrs=splitArrs(b);
		int[] arr1=arrs.get(0);
		int[] arr2=arrs.get(1);
		for(int i:arr1){
			if(i<0||i>100000)
				return 0;
			if(i==x)
				eq++;
			}
		for(int i:arr2){
			if(i!=x)
				neq++;}
		if(eq==neq){
			int k=eq*2;
			return k;
		}
		else return 0;

	}

	public static List<int[]> splitArrs(int[] a) {
		List<int[]> arrs = new ArrayList<>();
		if (a.length % 2 == 0) {
			int half = a.length / 2;
			int[] arr1 = new int[half];
			int[] arr2 = new int[half];
			for (int i = 0; i < arr1.length; i++)
				arr1[i] = a[i];

			for (int i = half; i < a.length; i++)
				arr2[i - half] = a[i];
			arrs.add(arr1);
			arrs.add(arr2);
		} else {
			int len = a.length;
			int half = len / 2;
			int[] arr1 = new int[half];
			int[] arr2 = new int[half];
			for (int i = 0; i < arr1.length; i++)
				arr1[i] = a[i];
			half++;
			for (int i = half; i < a.length; i++) {
				arr2[i - half] = a[i];
			}
			arrs.add(arr1);
			arrs.add(arr2);
		}
		return arrs;
	}

	public static void printArr(int[] a) {
		for (int i : a) {
			System.out.print(i);
		}
		System.out.println();
	}

    public int solution(int X, int[] A) {
        int N=A.length;
        if(!(N>0&&N<=100000))
        return 0;
        if(!(X>=0&&X<=100000))
        	return 0;
        
        return recursivelySwitch(A);
        
    }
public static void main(String[] args) {
	int[] i={1};
	Gcd in=new Gcd();
	
	in.increment(i);
	System.out.println(i[i.length-1]);
}
void increment(int[] i){
	i[i.length-1]++;
}
}
