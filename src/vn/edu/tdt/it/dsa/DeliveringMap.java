package vn.edu.tdt.it.dsa;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class DeliveringMap {

	private static String xuatPhat, dichDen;

	private static Map<String, Integer> mapDinhDI = new HashMap<>();
	private static Map<Integer, String> mapDinhID = new HashMap<>();

	private static ArrayList<String> duongDiStr = new ArrayList<String>();

	private static int[] duongDi, daDuyet;

	private static int n;
	private static Set<String> cacDinh;

	private static int[][] maTranke;

	private static boolean coNuocGiaiKhat, coLoCot;
	private static String[] doanDuongCoNuocGiaiKhat;

	private static ArrayList<String[]> nhungDoanDuongCoLoCot = new ArrayList<>();

	private static ArrayList<ArrayList<Integer>> tatCaDuongDi = new ArrayList<>();


	public DeliveringMap(File file) throws IOException {

		BufferedReader br = new BufferedReader(new FileReader(file));
		String st;
		String[] strings;
		while ((st = br.readLine()) != null) {
			st = st.replaceAll("\\s+", " ").trim();
			String[] strArr;
			strArr = st.split(" ");
			for (String s : strArr) {
				duongDiStr.add(s);
			}
		}

		coNuocGiaiKhat = false;
		coLoCot = false;
		String[] doanDuongLoCo = new String[2];
		for (String s : duongDiStr) {
			if (s.substring(2, 5).equals("000")){
				coNuocGiaiKhat = true;
				doanDuongCoNuocGiaiKhat = new String[2];
				doanDuongCoNuocGiaiKhat[0] = s.substring(0, 2);
				doanDuongCoNuocGiaiKhat[1] = s.substring(5, 7);
				//break;
			}
			if (s.substring(2, 5).equals("099")){
				coLoCot = true;
				doanDuongLoCo[0] = s.substring(0, 2);
				doanDuongLoCo[1] = s.substring(5, 7);
				nhungDoanDuongCoLoCot.add(doanDuongLoCo);
				//System.out.println(doanDuongLoCo[0]+" "+doanDuongLoCo[1]);
			}
		}


		xuatPhat = duongDiStr.get(0).substring(0, 2);
		dichDen = duongDiStr.get(duongDiStr.size() - 1).substring(5, 7);

		cacDinh = new HashSet<String>();
		for (String s : duongDiStr) {
			cacDinh.add(s.substring(0, 2));
			cacDinh.add(s.substring(5, 7));
		}
		n = cacDinh.size();

		int k = 0;
		for (String s : cacDinh){
			mapDinhDI.put(s, k);
			mapDinhID.put(k, s);
			k++;
		}

		duongDi = new int[n];
		daDuyet = new int[n];

		for (int i = 0; i<n; i++) {
			duongDi[i] = 0;
			daDuyet[i] = 0;
		}

		daDuyet[mapDinhDI.get(xuatPhat)] = 1;
		duongDi[0] = mapDinhDI.get(xuatPhat);

	}


	static int tongTrongSo(ArrayList<String> duongDiStr) {
		int sum = 0;
		for (String s : duongDiStr) {
			sum += Integer.parseInt(s.substring(2, 5));
		}
		return sum;
	}

	private static int doDaiDuongDi(ArrayList<Integer> duongDi){
		int ketQua = 0;
		for (int i = 0; i < duongDi.size()-1; i++){
			ketQua += maTranke[duongDi.get(i)][duongDi.get(i+1)];
		}
		return ketQua;
	}

	public int calculate(int level, boolean rushHour){

		int res = 0;

		maTranke = taoMaTranKe(rushHour);

		timKiem(1);

//		for (ArrayList<Integer> a : tatCaDuongDi){
//			for (Integer b : a){
//				System.out.print(mapDinhID.get(b)+" ");
//			}
//			System.out.println("TS: "+doDaiDuongDi(a));
//		}

		switch (level){
			case 0:
			case 1:
				res = level + n + tongTrongSo(duongDiStr);
				break;
			case 2:
			case 3:
			case 4:
				if (!coLoCot){
					int trongSoNhoNhat = doDaiDuongDi(duongDiNganNhat(tatCaDuongDi));
					if (trongSoNhoNhat > 100*level){
						res = 50*level - trongSoNhoNhat;
					}else {
						res = trongSoNhoNhat;
					}
				}else {
					if (diQuaLoCot(duongDiNganNhat(tatCaDuongDi))){
						res = 99 - 70*level;
					}else {
						int trongSoNhoNhat = doDaiDuongDi(duongDiNganNhat(tatCaDuongDi));
						if (trongSoNhoNhat > 100 * level) {
							res = 50 * level - trongSoNhoNhat;
						} else {
							res = trongSoNhoNhat;
						}
					}
				}
				break;
			case 5:
			case 6:
				if(!coLoCot) {
					int trongSoLonNhat = doDaiDuongDi(duongDiDaiNhat(tatCaDuongDi));
					if (trongSoLonNhat > 100 * level) {
						res = -level;
					} else {
						res = trongSoLonNhat;
					}
				}else {
					if (diQuaLoCot(duongDiDaiNhat(tatCaDuongDi))){
						res = 99 - 70*level;
					}else {
						int trongSoLonNhat = doDaiDuongDi(duongDiDaiNhat(tatCaDuongDi));
						if (trongSoLonNhat > 100 * level) {
							res = -level;
						} else {
							res = trongSoLonNhat;
						}
					}
				}
				break;
			case 7:
				int trongSoNhoNhat7 = doDaiDuongDi(duongDiNganNhat(tatCaDuongDi));
				int trongSoLonNhat7 = doDaiDuongDi(duongDiDaiNhat(tatCaDuongDi));
				float TBC = (trongSoNhoNhat7 + trongSoLonNhat7)/2;
				if (TBC < 30*level){
					res = (int)TBC;
				}else {
					res = -21;
				}
		}
		return res;
	}


	private static int[][] taoMaTranKe(boolean rush){

		int[][] result = new int[n][n];

		for (int i = 0; i < n; i++){
			for (int j = 0; j < n; j++){
				result[i][j] = -1;
			}
		}

		String a, b;
		if (rush){
			for (String s : duongDiStr) {
				a = s.substring(0, 2);
				b = s.substring(5, 7);
				result[mapDinhDI.get(a)][mapDinhDI.get(b)] = Integer.parseInt(s.substring(2, 5));
			}
		}else {
			for (String s : duongDiStr) {
				a = s.substring(0, 2);
				b = s.substring(5, 7);
				result[mapDinhDI.get(a)][mapDinhDI.get(b)] = Integer.parseInt(s.substring(2, 5));
				result[mapDinhDI.get(b)][mapDinhDI.get(a)] = Integer.parseInt(s.substring(2, 5));
			}
		}
//		System.out.print("\t");
//		for (int i = 0; i < n; i++){
//			System.out.print(mapDinhID.get(i) + "\t");
//		}
//		System.out.println("\n------------------");
//		for (int i = 0; i < n; i++){
//			System.out.print(mapDinhID.get(i)+"|\t");
//			for (int j = 0; j < n; j++){
//				System.out.print(result[i][j]+"\t");
//			}
//			System.out.println();
//		}

		return result;
	}

	static void InDuongDi(int SoCanh) {
		ArrayList<Integer> a = new ArrayList<>();
		a.clear();
		for (int i = 0; i<SoCanh; i++) {
			//System.out.print(mapDinhID.get(duongDi[i]) + "->");
			a.add(duongDi[i]);
		}
		tatCaDuongDi.add(a);
		//System.out.println();
	}

	static void timKiem(int SoCanh) {
		if(duongDi[SoCanh-1] == mapDinhDI.get(dichDen))
			InDuongDi(SoCanh);
		else {
			for(int i = 0; i<n; i++)
				if( maTranke[duongDi[SoCanh-1]][i]>-1 && daDuyet[i] == 0 ){
					duongDi[SoCanh] = i;
					daDuyet[i] = 1;
					timKiem(SoCanh+1);
					duongDi[SoCanh] = 0;
					daDuyet[i] = 0;
				}
		}
	}

	private static ArrayList<Integer> duongDiNganNhat(ArrayList<ArrayList<Integer>> duongDi){
		if (coNuocGiaiKhat && !coLoCot){
			ArrayList<ArrayList<Integer>> nhungDoanDuongGiaiKhat = new ArrayList<>();
			for (ArrayList<Integer> a : duongDi){
				for (int i = 0; i < a.size()-2; i++){
					if(mapDinhID.get(a.get(i)).equals(doanDuongCoNuocGiaiKhat[0]) &&
							mapDinhID.get(a.get(i+1)).equals(doanDuongCoNuocGiaiKhat[1])){
						nhungDoanDuongGiaiKhat.add(a);
					}
				}
			}
			if (!nhungDoanDuongGiaiKhat.isEmpty())
				duongDi = nhungDoanDuongGiaiKhat;
		}

		ArrayList<Integer> ketQua = duongDi.get(0);
		for (ArrayList<Integer> a : duongDi){
			if (doDaiDuongDi(ketQua) > doDaiDuongDi(a)){
				ketQua = a;
			}
		}
		return ketQua;
	}

	private static ArrayList<Integer> duongDiDaiNhat(ArrayList<ArrayList<Integer>> duongDi){
		if (coNuocGiaiKhat && !coLoCot){
			ArrayList<ArrayList<Integer>> nhungDoanDuongGiaiKhat = new ArrayList<>();
			nhungDoanDuongGiaiKhat.clear();
			for (ArrayList<Integer> a : duongDi){
				for (int i = 0; i < a.size()-1; i++){
					//System.out.println(mapDinhID.get(a.get(i)) + " " +mapDinhID.get(a.get(i+1)));
					if(mapDinhID.get(a.get(i)).equals(doanDuongCoNuocGiaiKhat[0]) &&
							mapDinhID.get(a.get(i+1)).equals(doanDuongCoNuocGiaiKhat[1])){
						nhungDoanDuongGiaiKhat.add(a);
						//System.out.println("Doan duong cos nuoc giai khat: "+a.toString());
					}
				}
			}
			if (!nhungDoanDuongGiaiKhat.isEmpty())
				duongDi = nhungDoanDuongGiaiKhat;
		}
		ArrayList<Integer> ketQua = duongDi.get(0);
		for (ArrayList<Integer> a : duongDi){
			if (doDaiDuongDi(ketQua) < doDaiDuongDi(a)){
				ketQua = a;
			}
		}
		return ketQua;
	}

	private static boolean diQuaLoCot(ArrayList<Integer> duongDi){
		boolean ketQua = false;
		for (int i = 0; i < duongDi.size()-1; i++ ){
			for (String[] loCot : nhungDoanDuongCoLoCot){
				if(mapDinhID.get(duongDi.get(i)).equals(loCot[0]) &&
						mapDinhID.get(duongDi.get(i+1)).equals(loCot[1])){
					ketQua = true;
					return ketQua;
				}
			}
		}
		return ketQua;
	}

	
	public static void main (String[] args){
		try{
			DeliveringMap map = new DeliveringMap(new File("map.txt"));
			System.out.println(map.calculate(7, false));
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
}
