import java.util.*;

class CLCSFast {
	
	static class Path {
		
		public int start;
		public int m;  
		public int n;
		public int[] maxRow;
		public int[] minRow;
		
		public Path(int start, int m, int n) {
			this.start = start;
			this.m = m;
			this.n = n;
			maxRow = new int[m+1];
			minRow = new int[m+1];
			for (int i = 0; i <= m; i++) {
				maxRow[i] = -1;
				minRow[i] = -1;
			}
		}
		
		public void addNode(int x, int y) {
			if (y > maxRow[x] || maxRow[x] == -1) {
				maxRow[x] = y;
			}
			if (y < minRow[x] || minRow[x] == -1) {
				minRow[x] = y;
			}
		}
		
		// public boolean isBelow(int curr, int x, int y) {
		// 	return min[y] >= (curr - this.start) + x;
		// }
		
		// public boolean isAbove(int curr, int x, int y) {
		// 	return max[y] <= (curr - this.start) + x;
		// }
		
		public int firstInRow(int curr, int x) {
			if (curr - this.start + x <= 0) {
				return 0;
			}
			//  else if (this.start - curr < 0) {
			// 	System.err.println("BAD FIRST");
			// 	return -2;
			// } 
			else {
				return minRow[curr - this.start + x];
			}
		}
		
		public int lastInRow(int curr, int x) {
			// if (curr - this.start + x < this.start) {
			// 	System.err.println("BAD LAST");
			// 	return -2;
			// } else 
			if (curr - this.start + x >= this.m) { 
				return this.n; 
			} else {
				return maxRow[curr - this.start + x];
			}
		}
		
	}
	
	static int[][] arr;
	static char[] A, B;
	static Map<Integer, Path> p;
	static int maxScore;
	
	private static Path singleShortestPathUnbounded(int mid){
		int m = A.length, n = B.length;
		char[][] backtrace = new char[m+2][n+2];		
		// for (int i = 0; i <= n; i++) {
		// 	backtrace[0][i] = 'l';
		// }
		// for (int i = 0; i <= m; i++) {
		// 	backtrace[i][0] = 'u';
		// }
		for (int i = 1; i <= m; i++) {
			for (int j = 1; j <= n; j++) {	
				// System.err.println("i " + i);
				// System.err.println("j " + j);
				if (arr[i-1][j] > arr[i][j-1]) {
					arr[i][j] = arr[i-1][j];
					backtrace[i][j] = 'u';
					// System.err.println("UP");
				} else {
					arr[i][j] = arr[i][j-1];					
					backtrace[i][j] = 'l';
					// System.err.println("LEFT");
				}
				if (A[(i+mid-2)%m] == B[j-1] && arr[i-1][j-1]+1 > arr[i][j]) {
					arr[i][j] = arr[i-1][j-1]+1;					
					backtrace[i][j] = 'd';
					// System.err.println("DIAG");
				}
			}
		}
		if (arr[m][n] > maxScore) maxScore = arr[m][n];
		// System.err.println("PUT A SCORE: " + mid);
		// System.err.println("THE SCORE WAS: " + arr[m][n]);
		return performBacktrace(backtrace, mid, m, n);
	}
	
	private static Path performBacktrace(char[][] backtrace, int mid, int m, int n) {
		Path newPath = new Path(mid, m, n);
		int i = m;
		int j = n;	
		char direction = backtrace[m+1][n+1];
		while(i > 0 || j > 0) {
			// System.err.println("i " + i);
			// System.err.println("j " + j);
			// System.err.println(backtrace[i][j]);
			newPath.addNode(i, j);
			direction = backtrace[i][j];
			if (direction == 'l') {
				j--;
			} else if (direction == 'u'){
				i--;
			} else if (direction == 'd') {
				i--;
				j--;
			} else if (i==0) {
				j--;
			} else if (j==0) {
				i--;
			} else {
				System.err.println("problem");
				break;
			}
		}
		
		
		
		// if (mid==7) {
		// 	System.err.println("mid is 7, adding node i=");
		// }
		newPath.addNode(0, 0);		
		return newPath;
	}

	private static boolean inBounds(Path upper, Path lower, int mid, int x, int y) {
		int firstInX = upper.firstInRow(mid, x);
		int lastInX = lower.lastInRow(mid, x);
		return x >= 0 && y >= firstInX && y <= lastInX;
	}
	
	private static Path singleShortestPath(int mid, int lower, int upper) {
		Path upperPath = p.get(upper);
		Path lowerPath = p.get(lower);
		int m = A.length, n = B.length;
		char[][] backtrace = new char[2048][2048];
		// for (int i = 0; i <= n; i++) {
		// 	backtrace[0][i] = 'l';
		// }
		// for (int i = 0; i <= m; i++) {
		// 	backtrace[i][0] = 'u';
		// }
		// clearTable(mid, m, n, upperPath, lowerPath);
		// arr[1][0] = 0;
		// arr[0][1] = 0;
		// arr[0][0] = 0;
		//System.err.println(arr[mid+1][1]);
		
		for (int i = 1; i <= m; i++) {
			int j = upperPath.firstInRow(mid, i);
			while (j <= lowerPath.lastInRow(mid, i)) {
				int curr = -1;
				char currdir = 'p';
				// System.err.println("i " + i);
				// System.err.println("j " + j + " going until " + lowerPath.lastInRow(mid,i));
				if (inBounds(upperPath, lowerPath, mid, i-1, j)) {
					curr = arr[i-1][j];
					currdir = 'u';
					// arr[i][j] = arr[i-1][j];
					// backtrace[i][j] = 'u';
					// System.err.println("UP");
				}
				if (inBounds(upperPath, lowerPath, mid, i, j-1) && arr[i][j-1] >= curr) {
					curr = arr[i][j-1];
					currdir = 'l';
					// arr[i][j] = arr[i][j-1];
					// backtrace[i][j] = 'l';	
					// System.err.println("LEFT");
				} 
				if (inBounds(upperPath, lowerPath, mid, i-1, j-1) && A[(i+mid-2)%m] == B[j-1] && arr[i-1][j-1]+1 > curr) {
					curr = arr[i-1][j-1] + 1;
					currdir = 'd';
					// arr[i][j] = arr[i-1][j-1]+1;
					// backtrace[i][j] = 'd';	
					// System.err.println("DIAG");
				}
				arr[i][j] = curr;
				backtrace[i][j] = currdir;
				j++;
				// System.err.println("i: "  + i);
				// System.err.println("J: " + j);
				// System.err.println("upperstart: " + upperPath.start);
				// if (arr[i-1][j] == arr[i][j-1]) {
				// 	arr[i][j] = arr[i-1][j];
				// 	if (i == 1) {
				// 		backtrace[i][j] = 'l';
				// 	} else {
				// 		backtrace[i][j] = 'u';
				// 	} 
				// } else if (arr[i-1][j] > arr[i][j-1]) {
				// 	arr[i][j] = arr[i-1][j];
				// 	backtrace[i][j] = 'u';
				// } else {
				// 	arr[i][j] = arr[i][j-1];
				// 	backtrace[i][j] = 'l';
				// }
				// if (A[(i+mid-1)%m] == B[j-1] && arr[i-1][j-1]+1 > arr[i][j]) {
				// 	arr[i][j] = arr[i-1][j-1]+1;
				// 	backtrace[i][j] = 'd';
				// }
				// j++;
			}  
		}
		// System.err.println("putting a score for mid= " + mid);
		// System.err.println("The score was: " + arr[m][n]);
		if (arr[m][n] > maxScore) maxScore = arr[m][n];
		// System.err.println("HERE ABOUT TO PERFORM BACKTRACE");
		return performBacktrace(backtrace, mid, m, n);
	}
	
	private static void findShortestPaths(int lower, int upper){
		// System.err.println("lower " + lower);
		// System.err.println("upper " + upper);
		if (upper - lower <= 1) return;
		int mid = (upper + lower)/2;
		// System.err.println("mid " + mid);
		p.put(mid, singleShortestPath(mid, lower, upper));
		findShortestPaths(lower, mid);
		findShortestPaths(mid, upper);
	}
	
	// private static void getBestScore(int m) {
	// 	int max = 0;
	// 	for (int i = 1; i <= m; i++) {
	// 		// System.err.println(i);
	// 		if(pScore.get(i) > max) max = pScore.get(i);
	// 	}
	// 	System.out.println(max);
	// }
	
	// private static void clearTable(int mid, int m, int n, Path upper, Path lower) {
	// 	for (int i = 1; i <= m; i++) {
	// 		int j = upper.firstInRow(mid, i);
	// 		while (j <= lower.lastInRow(mid, i)) {

	// 			arr[i][j] = 0;

	// 			j++;
	// 		}
	// 	}
	// }





// 		for (int i = 1; i <= m; i++) {
// 			for (int j=1; j <= n; j++){
// //			int j = upper.firstInRow(i);
// //			while (j <= lower.lastInRow(i)) {
// 				arr[i][j] = 0;
// //				arr[i-1][j] = 0;
// //				arr[i][j-1] = 0;
// //				arr[i-1][j-1] = 0;
// //				try{
// //					arr[i][j] = 0;
// //				} catch(Exception e) {
// //					System.err.println(upper.minRow);
// //					System.err.println(lower.maxRow);
// //				}
// //				j++;
// 			}
		// }	
	// }
	
	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
    	int T = s.nextInt();
    	for (int tc = 0; tc < T; tc++) {
    		A = s.next().toCharArray();
    	    B = s.next().toCharArray();
    	    arr = new int[A.length+2][B.length+2];
    	    p = new HashMap<Integer,Path>();
    	    maxScore = 0;
    	    p.put(A.length+1, singleShortestPathUnbounded(A.length+1));
    	    p.put(1, singleShortestPathUnbounded(1));
    	    System.err.println("m " + A.length);
    	    System.err.println("n " + B.length);
    	    findShortestPaths(1, A.length+1);
    	    System.out.println(maxScore);
    	}
	}
	
	
}