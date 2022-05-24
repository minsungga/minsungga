package java_sp;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class ConvExec1 {
	
	// ����������
	static int N;			// ���� ������
	static int padMapSize; 			// �е� �� ������
	static int[][] map;
	
	// CNN �Ķ����
	static int P = 0;		// �е� ũ�� (0���� �е�)
	static int S = 1;		// ��Ʈ���̵� ũ�� (Sĭ�� �ǳʶٱ�)
	static int F = 3;		// ���� ũ�� (3x3)
	static int[][] filter = {{1,0,1}, {0,1,0}, {1,0,1}};		// input.txt, P=0, S=1
//	static int[][] filter = {{1,0,-1}, {1,0,-1}, {1,0,-1}};		// input2.txt, P=0, Ǯ�� �� S=2�� ����
//	static int[][] filter = {{2,0,1}, {0,1,2}, {1,0,2}};		// input3.txt, P=1, Ǯ�� �� S=2�� ����
	static int PL = 2;		// Ǯ�� ũ�� (2x2)
	
	// Ư¡ ����
	static int fMapSize;
	static int[][] featureMap;	
	
	// CNN ��� 
	static int rMapSize;
	static int[][] resultMap;
	
	
	public static void main(String[] args) throws IOException, NumberFormatException {
			
		// ���� �б�
		FileReader fr = new FileReader("./INFILE/input.txt");
		BufferedReader br = new BufferedReader(fr);
		
		// 1. ���������� 2���� �迭 ����
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
		
		// 2. �е�
		if ( P > 0) {
			padMapSize = N+2*P;	
			map = Padding(map, N, P);
		} else {
			padMapSize = N;
		}	
		
		// 3. �� ��ġ���� Conv ���� �����Ͽ� Ư¡ ����
		ArrayList<Integer> convList = new ArrayList<>();	
		S=1;	// 1ĭ�� �̵�
		for (int r=0; r < padMapSize-F+1; r=r+S) {	
			for (int c=0; c < padMapSize-F+1; c=c+S) {
				convList.add( Conv(r, c) );
			}
		}

		fMapSize = (N - F + 2*P) / S + 1;
		System.out.println("Ư¡����� ������ = " + fMapSize);
		featureMap = makeMap(convList, fMapSize);
				
		
		
		// 4. ���� ���� ���� (Ǯ�� ������, ��Ʈ���̵�)
		ArrayList<Integer> poolList = new ArrayList<>();			
		S = 2;	// 2ĭ�� �̵�			
		for (int r=0; r < fMapSize-PL+1; r=r+S) {		
			for (int c=0; c < fMapSize-PL+1; c=c+S) {
				poolList.add( MaxPool(r, c));
//				poolList.add( AvgPool(r, c));
			}
		}
		
		rMapSize = (fMapSize - PL) / S + 1;
		System.out.println("Ǯ������ �� ������ = " + rMapSize);	
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
		
		System.out.println("�е��� ������ ������ = " + size);
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
