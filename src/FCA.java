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
	static BPCliques bpc;
	
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
//		System.out.println("Printing matrix");
//		
//		for(int i=0; i<numObj; i++){
//			for(int j=0; j<numAttr; j++){
//				System.out.print(adjMat[i][j]);
//			}
//			System.out.println();
//			
//		}
		
		bpc = getBPClique(adjMat);
		System.out.println(bpc.size());
		

		
		int size1, size2;
		size1 = bpc.size();
		size2 = -1;
		while(size1 != size2){
			size1 = bpc.size();
			bpc = condenseList(bpc);
			size2 = bpc.size();
		}
		
		bpc = removeUnclosed(bpc);
		
		//////////////////////////////////////////////////////
		System.out.println("Printing elements");
		for(int i =0; i< bpc.size(); i++){
			printArrayList(bpc.get(i).getL());
			printArrayList(bpc.get(i).getR());
			System.out.println();
		}
		//////////////////////////////////////////////////////

		
		
	}
	
	
	
	
	
	
	
	
	
	
	public static void printArrayList (ArrayList inputList){
		for(int i=0; i<inputList.size(); i++){
			System.out.print(inputList.get(i));
			
		}
		System.out.println();
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
			
//			System.out.print("What i'm pushing: ");
//			printArrayList(tmpObj);
//			printArrayList(tmpList);
			
			tmpPair = new Pair<ArrayList<String>, ArrayList<String>>(tmpObj, tmpList);
			tmpBpc.add(tmpPair);
			dict.put(obj.get(i), tmpList);
		}
		
		
		for(int i=0; i<numAttr; i++){
			tmpList = new ArrayList<String>();
			tmpAttr = new ArrayList<String>();
			tmpAttr.add(attr.get(i));
			
			for(int j=0; j<numObj; j++){
				if(adjMat[j][i]==1){
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
		
		BPCliques tmpBCList = new BPCliques();
		ArrayList<Integer> toSkip = new ArrayList<Integer>();
		ArrayList<String> tmpAList;
		HashSet<String> tmpHSet;
		Pair<ArrayList<String>, ArrayList<String>> tmpPair;
		Boolean matched;
		
		for(int i=0; i<inputList.size(); i++){
			if(toSkip.contains(i)) continue;
			matched = false;
			
			for(int j=i+1; j<inputList.size(); j++){
				if(toSkip.contains(j)) continue;
				
				if(new HashSet<String>(inputList.get(i).getL()).equals( new HashSet<String>(inputList.get(j).getL()) )){
					tmpHSet = new HashSet<String>(inputList.get(i).getR());
					tmpHSet.addAll(new HashSet<String>(inputList.get(j).getR()));
					tmpAList = new ArrayList<>(tmpHSet);
					tmpPair = new Pair<ArrayList<String>, ArrayList<String>>(inputList.get(i).getL(), tmpAList);
					
					tmpBCList.add(tmpPair);
					toSkip.add(j);
					matched = true;
					break;
				}
				else if(new HashSet<String>(inputList.get(i).getR()).equals( new HashSet<String>(inputList.get(j).getR()) )){
					tmpHSet = new HashSet<String>(inputList.get(i).getL());
					tmpHSet.addAll(new HashSet<String>(inputList.get(j).getL()));
					tmpAList = new ArrayList<>(tmpHSet);
					tmpPair = new Pair<ArrayList<String>, ArrayList<String>>(tmpAList, inputList.get(i).getR());
					
					tmpBCList.add(tmpPair);
					toSkip.add(j);
					matched = true;
					break;
				}
				
			}
			if (matched == false){
				tmpBCList.add(inputList.get(i));
			}
		}
		
		return tmpBCList;
		
	}
	
	public static BPCliques removeUnclosed(BPCliques inputList){
		BPCliques tmpBCList = new BPCliques();
		ArrayList<String> listO;
		ArrayList<String> listA;
		HashSet<String> tmpH;
		HashSet<String> tmpO;
		HashSet<String> tmpA;
		
//		for(int i=0; i<inputList.size(); i++){
//			listO = new ArrayList<String>();
//			listA = new ArrayList<String>();
//			
//			for(int j=0; j<inputList.get(i).getL().size(); j++){
//				if(listA.size() == 0){
//					listA = new ArrayList<String>(dict.get(inputList.get(i).getL().get(j)));
//				}else{
//					tmpH = new HashSet<String>(listA);
//					tmpH.retainAll(new HashSet<String>(dict.get(inputList.get(i).getL().get(j))));
//					listA = new ArrayList<String>(tmpH);
//					
//				}
//				
//			}
//			
//			for(int j=0; j<inputList.get(i).getR().size(); j++){
//				if(listO.size() == 0){
//					listO = new ArrayList<String>(dict.get(inputList.get(i).getR().get(j)));
//				}else{
//					tmpH = new HashSet<String>(listA);
//					tmpH.retainAll(new HashSet<String>(dict.get(inputList.get(i).getR().get(j))));
//					listA = new ArrayList<String>(tmpH);
//					
//				}
//				
//			}
		
		
		
		for(int i=0; i<inputList.size(); i++){
			tmpO = new HashSet<String>();
			tmpA = new HashSet<String>();
			
			for(int j=0; j<inputList.get(i).getL().size(); j++){
				if(tmpA.size() == 0) tmpA = new HashSet<String>(dict.get(inputList.get(i).getL().get(j)));
				else{
					tmpA.retainAll(new HashSet<String>(dict.get(inputList.get(i).getL().get(j))));
				}
			}
			
			
			for(int j=0; j<inputList.get(i).getR().size(); j++){
				if(tmpO.size() == 0) tmpO = new HashSet<String>(dict.get(inputList.get(i).getR().get(j)));
				else{
					tmpO.retainAll(new HashSet<String>(dict.get(inputList.get(i).getR().get(j))));
				}
			}
			
			
			if (tmpA.equals(new HashSet<String>(inputList.get(i).getR())) && tmpO.equals(new HashSet<String>(inputList.get(i).getL()))){
				tmpBCList.add(inputList.get(i));
			}
			
		}
		return tmpBCList;
		
	}

}
