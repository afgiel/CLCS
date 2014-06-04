//
// Andrew Giel - agiel - 005718013
// Hussain Kader - hkader - 005748791
// Ankit Kumar - ankitk - 005760506
//

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
		
		public int firstInRow(int curr, int x) {
			if (curr - this.start + x <= 0) {
				return 0;
			}
			else {
				return minRow[curr - this.start + x];
			}
		}
		
		public int lastInRow(int curr, int x) {
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
	static char[][] backtrace;
	
	private static Path singleShortestPathUnbounded(int mid){
		int m = A.length, n = B.length;
		for (int i = 1; i <= m; i++) {
			for (int j = 1; j <= n; j++) {	
				if (arr[i-1][j] > arr[i][j-1]) {
					arr[i][j] = arr[i-1][j];
					backtrace[i][j] = 'u';
				} else {
					arr[i][j] = arr[i][j-1];					
					backtrace[i][j] = 'l';
				}
				if (A[(i+mid-2)%m] == B[j-1] && arr[i-1][j-1]+1 > arr[i][j]) {
					arr[i][j] = arr[i-1][j-1]+1;					
					backtrace[i][j] = 'd';
				}
			}
		}
		if (arr[m][n] > maxScore) maxScore = arr[m][n];
		return performBacktrace(mid, m, n);
	}
	
	private static Path performBacktrace(int mid, int m, int n) {
		Path newPath = new Path(mid, m, n);
		int i = m;
		int j = n;	
		char direction = backtrace[m+1][n+1];
		while(i > 0 || j > 0) {
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
				System.err.println("backtrace error");
				break;
			}
		}		
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
		for (int i = 1; i <= m; i++) {
			int j = upperPath.firstInRow(mid, i);
			while (j <= lowerPath.lastInRow(mid, i)) {
				int curr = -1;
				char currdir = 'p';
				if (inBounds(upperPath, lowerPath, mid, i-1, j)) {
					curr = arr[i-1][j];
					currdir = 'u';
				}
				if (inBounds(upperPath, lowerPath, mid, i, j-1) && arr[i][j-1] >= curr) {
					curr = arr[i][j-1];
					currdir = 'l';
				} 
				if (inBounds(upperPath, lowerPath, mid, i-1, j-1) && A[(i+mid-2)%m] == B[j-1] && arr[i-1][j-1]+1 > curr) {
					curr = arr[i-1][j-1] + 1;
					currdir = 'd';
				}
				arr[i][j] = curr;
				backtrace[i][j] = currdir;
				j++;
			}  
		}
		if (arr[m][n] > maxScore) maxScore = arr[m][n];
		return performBacktrace(mid, m, n);
	}
	
	private static void findShortestPaths(int lower, int upper){
		if (upper - lower <= 1) return;
		int mid = (upper + lower)/2;
		p.put(mid, singleShortestPath(mid, lower, upper));
		findShortestPaths(lower, mid);
		findShortestPaths(mid, upper);
	}
	
	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
    	int T = s.nextInt();
    	for (int tc = 0; tc < T; tc++) {
    		A = s.next().toCharArray();
    	    B = s.next().toCharArray();
    	    arr = new int[A.length+2][B.length+2];
    	    p = new HashMap<Integer,Path>();
    	    backtrace = new char[A.length+2][B.length+2];
    	    maxScore = 0;
    	    p.put(A.length+1, singleShortestPathUnbounded(A.length+1));
    	    p.put(1, singleShortestPathUnbounded(1));
    	    findShortestPaths(1, A.length+1);
    	    System.out.println(maxScore);
    	}
	}
}