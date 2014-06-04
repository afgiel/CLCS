//
// Andrew Giel - agiel - 005718013
// Hussain Kader - hkader - 005748791
// Ankit Kumar - ankitk - 005760506
//

import java.util.*;

class CLCSSlow {
	
	static int[][] arr = new int[2048][2048];
	static char[] origA, A, B;
	
	private static void cut(int k){
		char[] newFront = Arrays.copyOfRange(origA, k, origA.length);		
		System.arraycopy(newFront, 0, A, 0, newFront.length);
		char[] newBack = Arrays.copyOfRange(origA, 0, k);		
		System.arraycopy(newBack, 0, A, newFront.length, newBack.length);
	}
	
	static int LCS() {
		int m = A.length, n = B.length;
		int i, j;
		for (i = 0; i <= m; i++) arr[i][0] = 0;
		for (j = 0; j <= n; j++) arr[0][j] = 0;

		for (i = 1; i <= m; i++) {
			for (j = 1; j <= n; j++) {
				arr[i][j] = Math.max(arr[i-1][j], arr[i][j-1]);
				if (A[i-1] == B[j-1]) arr[i][j] = Math.max(arr[i][j], arr[i-1][j-1]+1);
			}
		}

		return arr[m][n];
	}

	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
    	int T = s.nextInt();
    	for (int tc = 0; tc < T; tc++) {
    		origA = s.next().toCharArray();
    		A = origA.clone();
    	    B = s.next().toCharArray();
    	    int max = 0;
    	    for (int k=0; k < origA.length; k++) {
        	    if (k != 0) cut(k);
        	    int result = LCS();
        	    if (result > max) max = result;
    	    }
    	    System.out.println(max);
    	}
//    	System.err.println(A);
	}

}