import java.util.*;

class CLCSFast {
	
	static class Path {
		
		public int start;
		public int m;  
		public int n;
		public int[] max;
		public int[] min;
		public int[] maxRow;
		public int[] minRow;
		
		public Path(int start, int m, int n) {
			this.start = start;
			this.m = m;
			this.n = n;
			max = new int[n+1];
			min = new int[n+1];
			for (int i = 0; i <= n; i++) {
				min[i] = -1;
				max[i] = -1;
			}
			maxRow = new int[m+1];
			minRow = new int[m+1];
			for (int i = 0; i <= m; i++) {
				maxRow[i] = -1;
				minRow[i] = -1;
			}
		}
		
		public void addNode(int x, int y) {
			System.err.println(x);
			System.err.println(this.m);
			if (x > max[y] || max[y] == -1) {
				max[y] = x;
			}
			if (x < min[y] || min[y] == -1) {
				min[y] = x;
			}
			if (y > maxRow[x-this.start] || maxRow[x-this.start] == -1) {
				maxRow[x-this.start] = y;
			}
			if (y < minRow[x-this.start] || minRow[x-this.start] == -1) {
				minRow[x-this.start] = y;
			}
		}
		
		public boolean isBelow(int x, int y) {
			return min[y] >= x;
		}
		
		public boolean isAbove(int x, int y) {
			return max[y] <= x;
		}
		
		public int firstInRow(int x) {
			if (x - this.start < 0) {
				return 1;
			} else if (x - this.start > this.m) {
				System.err.println("BAD");
				return -2;
			} else {
				return minRow[x-this.start];
			}
		}
		
		public int lastInRow(int x) {
			if (x - this.start < 0) {
				System.err.println("BAD");
				return -2;
			} else if (x - this.start >= this.m) { 
				return this.n; 
			} else {
				return maxRow[x-this.start];
			}
		}
		
	}
	
	static int[][] arr;
	static char[] A, B;
	static Map<Integer, Path> p;
	static Map<Integer, Integer> pScore;
	
	private static Path singleShortestPathUnbounded(int mid){
		int m = A.length, n = B.length;
		char[][] backtrace = new char[2048][2048];
		for (int i = 1 + mid; i <= m + mid; i++) {
			for (int j = 1; j <= n; j++) {
				if (arr[i-1][j] > arr[i][j-1]) {
					arr[i][j] = arr[i-1][j];
					backtrace[i][j] = 'u';
				} else {
					arr[i][j] = arr[i][j-1];					
					backtrace[i][j] = 'l';
				}
				if (A[(i-1)%m] == B[j-1] && arr[i-1][j-1]+1 > arr[i][j]) {
					arr[i][j] = arr[i-1][j-1]+1;					
					backtrace[i][j] = 'd';
				}
				if (j == n) continue;
			}
		}
		pScore.put(mid, arr[mid + m][n]);
		System.err.println("PUT A SCORE: " + mid);
		System.err.println("THE SCORE WAS: " + arr[mid+m][n]);
		return performBacktrace(backtrace, mid, m, n);
	}
	
	private static Path performBacktrace(char[][] backtrace, int mid, int m, int n) {
		Path newPath = new Path(mid+1, m, n);
		int i = mid + m;
		int j = n;	
		char direction = backtrace[mid + m][n];
		while(i != mid+1 && j != 1) {
			System.err.println("i " + i);
			System.err.println("j " + j);
			System.err.println(backtrace[i][j]);
			newPath.addNode(i, j);
			direction = backtrace[i][j];
			if (direction == 'l') {
				j--;
			} else if (direction == 'u'){
				i--;
			} else if (direction == 'd') {
				i--;
				j--;
			} else {
				System.err.println("BAD SHIT HERE" + i + " " +  j);
				break;
			}
		}
		if (mid==7) {
			System.err.println("mid is 7, adding node i=");
		}
		newPath.addNode(mid + 1, 1);		
		return newPath;
	}
	
	private static Path singleShortestPath(int mid, int lower, int upper) {
		Path upperPath = p.get(upper);
		Path lowerPath = p.get(lower);
		int m = A.length, n = B.length;
		char[][] backtrace = new char[2048][2048];
		
		clearTable(mid, m, n, upperPath, lowerPath);
		System.err.println(arr[mid+1][1]);
		
		for (int i = 2 + mid; i <= m + mid; i++) {
			int j = upperPath.firstInRow(i);
//			if (j == 1){
//				j = 2;
//			}
			while (j <= lowerPath.lastInRow(i)) {
				System.err.println("i: "  + i);
				System.err.println("J: " + j);
				System.err.println("upperstart: " + upperPath.start);
				//if (j == n) continue;
//				 if (lowerPath.isBelow(i-1,j)) {
//					 System.err.println("here1");
//					 
//					 arr[i][j] = arr[i-1][j];
//					 backtrace[i][j] = 'u';
//				 }
//				 if (upperPath.isAbove(i, j-1) && arr[i][j-1] > arr[i][j]) {
//					 System.err.println("here2");
//					 arr[i][j] = arr[i][j-1];
//					 backtrace[i][j] = 'l';
//							 
//					 
//				 }
//				 if (upperPath.isAbove(i-1, j-1) && lowerPath.isBelow(i-1, j-1) && A[(i-1)%m] == B[j-1] && arr[i-1][j-1]+1 > arr[i][j]){
//					System.err.println("here3");
//					arr[i][j] = arr[i-1][j-1]+1;
//					backtrace[i][j] = 'd';
//				 }
				
				
				if (arr[i-1][j] > arr[i][j-1]) {
					arr[i][j] = arr[i-1][j];
					backtrace[i][j] = 'u';
				} else {
					arr[i][j] = arr[i][j-1];
					backtrace[i][j] = 'l';
				}
				if (A[(i-1)%m] == B[j-1] && arr[i-1][j-1]+1 > arr[i][j]) {
					arr[i][j] = arr[i-1][j-1]+1;
					backtrace[i][j] = 'd';
				}
				j++;
			}  
		}
		System.err.println("putting a score for mid= " + mid);
		System.err.println("The score was: " + arr[mid + m][n]);
		pScore.put(mid, arr[mid + m][n]);
		System.err.println("HERE ABOUT TO PERFORM BACKTRACE");
		return performBacktrace(backtrace, mid, m, n);
	}
	
	private static void findShortestPaths(int lower, int upper){
		System.err.println("lower " + lower);
		System.err.println("upper " + upper);
		if (upper - lower <= 1) return;
		int mid = (upper + lower)/2;
		System.err.println("mid " + mid);
		p.put(mid, singleShortestPath(mid, lower, upper));
		findShortestPaths(lower, mid);
		findShortestPaths(mid, upper);
	}
	
	private static void getBestScore(int m) {
		int max = 0;
		for (int i = 0; i <= m; i++) {
			System.err.println(i);
			if(pScore.get(i) > max) max = pScore.get(i);
		}
		System.out.println(max);
	}
	
	private static void clearTable(int mid, int m, int n, Path upper, Path lower) {
		for (int i = 1 + mid; i <= m + mid; i++) {
			for (int j=1; j <= n; j++){
//			int j = upper.firstInRow(i);
//			while (j <= lower.lastInRow(i)) {
				arr[i][j] = 0;
//				arr[i-1][j] = 0;
//				arr[i][j-1] = 0;
//				arr[i-1][j-1] = 0;
//				try{
//					arr[i][j] = 0;
//				} catch(Exception e) {
//					System.err.println(upper.minRow);
//					System.err.println(lower.maxRow);
//				}
//				j++;
			}
		}	
	}
	
	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
    	int T = s.nextInt();
    	for (int tc = 0; tc < T; tc++) {
    		A = s.next().toCharArray();
    	    B = s.next().toCharArray();
    	    arr = new int[2048][2048];
    	    p = new HashMap<Integer,Path>();
    	    pScore = new HashMap<Integer, Integer>();
    	    p.put(A.length, singleShortestPathUnbounded(A.length));
    	    p.put(0, singleShortestPathUnbounded(0));
    	    System.err.println("m " + A.length);
    	    System.err.println("n " + B.length);
    	    findShortestPaths(0, A.length);
    	    getBestScore(A.length);
    	}
	}
	
	
}