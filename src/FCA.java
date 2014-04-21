import java.io.ObjectInputStream.GetField;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;


public class FCA {
	static String tmpS;
	static int[][] adjMat;
	static Integer tmpI, numObj, numAttr;
	static HashMap<String, ArrayList<String>> dict = new HashMap<>();
	static ArrayList<Integer> hasSuccesor = new ArrayList<Integer>();
	static ArrayList<Integer> hasPredecessor= new ArrayList<Integer>();
	
	static ArrayList<String> obj = new ArrayList<String>();
	static ArrayList<String> attr = new ArrayList<String>();
	static ArrayList<Pair<ArrayList<String>, ArrayList<String>>> bpc;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		
		
		
		
		Scanner keyboard = new Scanner(System.in);
		System.out.println("Enter the number of Objects..");
		numObj = keyboard.nextInt();
		System.out.println("Enter the number of Attributes..");
		numAttr = keyboard.nextInt();
		
		
		System.out.println("Enter the Objects..");
		for(int i=0; i<numObj; i++){
			tmpS = keyboard.next();
			obj.add(tmpS);
		}
		
		System.out.println("Enter the Attributes..");
		for(int i=0; i<numObj; i++){
			tmpS = keyboard.next();
			attr.add(tmpS);
		}
		
		
		adjMat = new int[numObj][numAttr];
		System.out.println("Enter the values of matrix (0 or 1)..");
		for(int i=0; i<numObj; i++){
			for(int j=0; j<numAttr; j++){
				tmpI = keyboard.nextInt();
				adjMat[i][j]=tmpI;
			}
		}
		
		bpc = getBPClique(adjMat);
		System.out.println(bpc.size());
		
		
//		for(int i=0; i<obj.size(); i++){
//			System.out.println(obj.get(i));
//		}
//		
//		System.out.println(tmpS);
		
		
	}
	
	public static BPCliques getBPClique(int[][] mat){
		
		BPCliques tmpBpc = new BPCliques();
		Pair<ArrayList<String>, ArrayList<String>> tmpPair;
		ArrayList<String> tmpObj;
		ArrayList<String> tmpAttr;
		ArrayList<String> tmpList;
		
		for(int i=0; i<numObj; i++){
			tmpList = new ArrayList<String>();
			tmpObj = new ArrayList<String>();
			tmpObj.add(obj.get(i));
			
			for(int j=0; j<numAttr; j++){
				if(adjMat[i][j]==1){
					tmpList.add(attr.get(j));
				}
			}
			
			tmpPair = new Pair<ArrayList<String>, ArrayList<String>>(tmpObj, tmpList);
			tmpBpc.add(tmpPair);
			dict.put(obj.get(i), tmpList);
		}
		
		for(int i=0; i<numAttr; i++){
			tmpList = new ArrayList<String>();
			tmpAttr = new ArrayList<String>();
			tmpAttr.add(attr.get(i));
			
			for(int j=0; j<numObj; j++){
				if(adjMat[i][j]==1){
					tmpList.add(obj.get(j));
				}
			}
			
			tmpPair = new Pair<ArrayList<String>, ArrayList<String>>(tmpList, tmpAttr);
			tmpBpc.add(tmpPair);
			dict.put(attr.get(i), tmpList);
		}
		
		return tmpBpc;
	}
	
	public static BPCliques condenseList(BPCliques inputList){
		
		BPCliques tmpList = new BPCliques();
		ArrayList<Integer> toSkip = new ArrayList<Integer>();
		Boolean matched;
		
		for(int i=0; i<inputList.size(); i++){
			if(toSkip.contains(i)) continue;
			matched = false;
			
			for(int j=i+1; j<inputList.size(); j++){
				if(toSkip.contains(j)) continue;
				
				if(new HashSet<String>(inputList.get(i).getL()).equals(new HashSet<String>(inputList.get(j).getL()))){
					
				}
				
			}
		}
		
		return tmpList;
		
	}

}
