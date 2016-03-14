/*
 * Formal concept analysis lattice generation and query
 * Author: Pankaj Kumar (me@panks.me)
 * 
 */


import java.awt.Dimension;
import java.io.ObjectInputStream.GetField;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

import javax.swing.JFrame;

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;


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

		Scanner keyboard = new Scanner(System.in);
		String input, key;
		List<String> inputA;
		
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
		for(int i=0; i<numAttr; i++){
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


		int size1, size2;
		size1 = bpc.size();
		size2 = -1;
		while(size1 != size2){
			size1 = bpc.size();
			bpc = condenseList(bpc);
			size2 = bpc.size();
		}

		bpc = removeUnclosed(bpc);

		generateLattice(bpc);
		
		System.out.println("Node of current lattice:");
		for(int i=0; i<bpc.size(); i++){
			System.out.println(AtoS(bpc.get(i).getL()) + ", " + AtoS(bpc.get(i).getR()) );
		}

		
		HashMap<String, ArrayList<String>> conceptDict = new HashMap<String, ArrayList<String>>();
		for(int i=0; i<bpc.size(); i++){
			Collections.sort(bpc.get(i).getL());
			Collections.sort(bpc.get(i).getR());
			String objS = AtoS( bpc.get(i).getL());
			String attrS = AtoS(bpc.get(i).getR());
			
			conceptDict.put(objS, bpc.get(i).getR());
			conceptDict.put(attrS, bpc.get(i).getL());
		}
		keyboard.nextLine();
		while(true){
			System.out.println("Enter the query. Intent or Extent seperated by space, Ex- 2 3 4 OR a b\nEnter 'Q' to exit.");
			input = keyboard.nextLine();
			if(input.equals("Q")) break;
			else{
				inputA = Arrays.asList(input.split(" "));
				Collections.sort(inputA);
				key = AtoS(inputA);
				if(conceptDict.containsKey(key)){
					System.out.println(conceptDict.get(key));
				}else{
					System.out.println("Not in concept lattice");
				}
			}		
		}
		
		return;
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
		HashSet<String> tmpO;
		HashSet<String> tmpA;


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

	public static void generateLattice(BPCliques inputList){

		GraphView sgv = new GraphView();
		ArrayList<Integer> hasP = new ArrayList<Integer>();
		ArrayList<Integer> hasS = new ArrayList<Integer>();
		String src, dest, firstNode, lastNode;
		ArrayList<String> sA; 
		ArrayList<String> sO;
		HashSet<String> tmpO = new HashSet<String>();
		HashSet<String> tmpA = new HashSet<String>();

		for(int i=0; i<inputList.size(); i++){
			sgv.addVertex(AtoS(inputList.get(i).getL()) + ", " +  AtoS(inputList.get(i).getR()));
		}


		for(int i=0; i<inputList.size(); i++){
			for(int j=i+1; j<inputList.size(); j++){
				if(new HashSet<String>(inputList.get(j).getL()).containsAll(inputList.get(i).getL())){
					src = AtoS(inputList.get(i).getL()) + ", " +  AtoS(inputList.get(i).getR());
					dest = AtoS(inputList.get(j).getL()) + ", " +  AtoS(inputList.get(j).getR());
					sgv.addEdge(src+dest, src, dest);
					hasP.add(j);
					hasS.add(i);
				}

			}
		}


		for(int i=0; i<attr.size(); i++){
			if(i == 0) {
				tmpO = new HashSet<String>(dict.get(attr.get(i)));
			}
			else{ 
				tmpO.retainAll(new HashSet<String>(dict.get(attr.get(i))));

			}
		}

		for(int i=0; i<obj.size(); i++){
			if(i == 0) tmpA = new HashSet<String>(dict.get(obj.get(i)));
			else tmpA.retainAll(new HashSet<String>(dict.get(obj.get(i))));
		}

		sA = new ArrayList<String>(tmpA);
		sO = new ArrayList<String>(tmpO);
		
		
		if (sA.size() == 0) sA.add("null");
		if (sO.size() == 0) sO.add("null");

		firstNode = AtoS(sO)+ ", " +AtoS(attr);
		lastNode = AtoS(obj) + ", " + AtoS(sA);
		sgv.addVertex(firstNode);
		sgv.addVertex(lastNode);

		for(int i=0; i<inputList.size(); i++){
			if(!hasS.contains(i)){
				src = AtoS(inputList.get(i).getL()) + ", " +  AtoS(inputList.get(i).getR());
				sgv.addEdge(src+lastNode, src, lastNode);
			}
		}
		for(int i=0; i<inputList.size(); i++){
			if(!hasP.contains(i)){
				src = AtoS(inputList.get(i).getL()) + ", " +  AtoS(inputList.get(i).getR());
				sgv.addEdge(src+firstNode, src, firstNode);
			}
		}

		Layout<Integer, String> layout = new CircleLayout(sgv.g);
		layout.setSize(new Dimension(300,300));
		BasicVisualizationServer<Integer,String> vv = new BasicVisualizationServer<Integer,String>(layout);
		vv.setPreferredSize(new Dimension(350,350)); 
		vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());

		JFrame frame = new JFrame("Concept Lattice");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(vv); 
		frame.pack();
		frame.setVisible(true);
		
	}


	public static String AtoS(ArrayList<String> input){
		String listString = "";

		for (String s : input)
		{
			listString += s + " ";
		}

		return listString;
	}
	
	public static String AtoS(List<String> input){
		String listString = "";

		for (String s : input)
		{
			listString += s + " ";
		}

		return listString;
	}

}
