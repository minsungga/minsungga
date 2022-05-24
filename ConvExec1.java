package java_sp;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class ConvExec1 {
	
	// 원본데이터
	static int N;			// 원본 사이즈
	static int padMapSize; 			// 패딩 후 사이즈
	static int[][] map;
	
	// CNN 파라미터
	static int P = 0;		// 패딩 크기 (0으로 패딩)
	static int S = 1;		// 스트라이드 크기 (S칸씩 건너뛰기)
	static int F = 3;		// 필터 크기 (3x3)
	static int[][] filter = {{1,0,1}, {0,1,0}, {1,0,1}};		// input.txt, P=0, S=1
//	static int[][] filter = {{1,0,-1}, {1,0,-1}, {1,0,-1}};		// input2.txt, P=0, 풀링 시 S=2로 변경
//	static int[][] filter = {{2,0,1}, {0,1,2}, {1,0,2}};		// input3.txt, P=1, 풀링 시 S=2로 변경
	static int PL = 2;		// 풀링 크기 (2x2)
	
	// 특징 추출
	static int fMapSize;
	static int[][] featureMap;	
	
	// CNN 결과 
	static int rMapSize;
	static int[][] resultMap;
	
	
	public static void main(String[] args) throws IOException, NumberFormatException {
			
		// 파일 읽기
		FileReader fr = new FileReader("./INFILE/input.txt");
		BufferedReader br = new BufferedReader(fr);
		
		// 1. 원본데이터 2차원 배열 구성
		N = Integer.parseInt(br.readLine());		
		map = new int[N][N];
		
		StringTokenizer st;		
		for (int r=0; r < N; r++) {
			st = new StringTokenizer(br.readLine());
			for (int c=0; c<N; c++) {
				if (st.hasMoreTokens()) {
					map[r][c] = Integer.parseInt(st.nextToken());
				}

			}
		}		
		br.close();
		
		// 2. 패딩
		if ( P > 0) {
			padMapSize = N+2*P;	
			map = Padding(map, N, P);
		} else {
			padMapSize = N;
		}	
		
		// 3. 각 위치별로 Conv 연산 수행하여 특징 추출
		ArrayList<Integer> convList = new ArrayList<>();	
		S=1;	// 1칸씩 이동
		for (int r=0; r < padMapSize-F+1; r=r+S) {	
			for (int c=0; c < padMapSize-F+1; c=c+S) {
				convList.add( Conv(r, c) );
			}
		}

		fMapSize = (N - F + 2*P) / S + 1;
		System.out.println("특징추출맵 사이즈 = " + fMapSize);
		featureMap = makeMap(convList, fMapSize);
				
		
		
		// 4. 폴링 연산 수행 (풀링 사이즈, 스트라이드)
		ArrayList<Integer> poolList = new ArrayList<>();			
		S = 2;	// 2칸씩 이동			
		for (int r=0; r < fMapSize-PL+1; r=r+S) {		
			for (int c=0; c < fMapSize-PL+1; c=c+S) {
				poolList.add( MaxPool(r, c));
//				poolList.add( AvgPool(r, c));
			}
		}
		
		rMapSize = (fMapSize - PL) / S + 1;
		System.out.println("풀링연산 후 사이즈 = " + rMapSize);	
		resultMap = makeMap(poolList, rMapSize);
		
	}
	
	
	public static int[][] Padding(int[][] org, int N, int P) {
		int size = N+2*P;
		int[][] paddingMap = new int[size][size];

		for (int r=P; r<N+P; r++) {
			for (int c=P; c<N+P; c++) {
				paddingMap[r][c] = org[r-P][c-P]; 
			}
		}
		
		System.out.println("패딩한 데이터 사이즈 = " + size);
		for (int r=0; r < size; r++) {
			for (int c=0; c < size; c++) {
				System.out.print(paddingMap[r][c] + " ");
			}
			System.out.println();
		}
		System.out.println("===============");
		
		return paddingMap;
	}
	
	
	public static int Conv(int x, int y) {
		int ret = 0;
		
		for (int r=0; r<F; r++) {
			for (int c=0; c<F; c++) {
				ret = ret + (map[x+r][y+c] * filter[r][c]);
			}
		}
		return ret;
	}
	
	public static int MaxPool(int x, int y) {
		int max = Integer.MIN_VALUE;
		
		for (int r=0; r<PL; r++) {
			for (int c=0; c<PL; c++) {
				if (max < featureMap[x+r][y+c]) {
					max = featureMap[x+r][y+c];
				}
			}
		}		
		
		return max;
	}
	
	public static int AvgPool(int x, int y) {
		int sum = 0;
		
		for (int r=0; r<PL; r++) {
			for (int c=0; c<PL; c++) {
				sum = sum + featureMap[x+r][y+c];
			}
		}		
		
		int avg = sum / (PL*PL);
		return avg;
	}
	
	public static int[][] makeMap(ArrayList<Integer> list, int size) {
		int[][] newMap = new int[size][size];
		
		int idx = 0;
		for (int r=0; r < size; r++) {
			for (int c=0; c < size; c++) {
				newMap[r][c] = list.get(idx++);
				System.out.print(newMap[r][c] + " ");
			}
			System.out.println();
		}
		System.out.println("===============");
		
		return newMap;
	}

}
