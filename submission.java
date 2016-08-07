
import java.io.FileReader;
import java.io.File;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class submission {

	private static String[] words = null;

	private static double[] prob = null;

	private static double[][] cost = null;

	private static int rootMatrix[][] = null;

	private static BufferedWriter bw = null;

	private static String file2;


	public static void main(String[] args) throws Exception {
		List<String> lines = readFile(args[0]);
		words = new String[lines.size()];

		prob = new double[lines.size()];

		file2 = args[1];
		getWordsAndProb(lines);

		optimalSearchTree(words.length);

		printRootMatrix(rootMatrix);

		printCostMatrix(cost);

		constructOptimalBST(0, words.length, "is the root of tree.");
	}

	private static void filewrite(String data, String filename)throws FileNotFoundException {
		try{
			bw = new BufferedWriter(new FileWriter(filename,true));
			bw.write(data + "\r\n");
		}catch(IOException e){
	
		}finally {
			if(bw!= null){
				try{
					bw.close();
				}catch(IOException e){
					System.err.println(e);
				}
			}
		}
	}


	private static List<String> readFile(String inputFile) {
		List<String> lines = new ArrayList<String>();
		try {
			Scanner scanner = new Scanner(new File(inputFile));
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				lines.add(line);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return lines;
	}

	 private static void getWordsAndProb(List<String> lines) throws Exception {
		int i = 0;
		Double temp1;
		String temp;

		for (String line : lines) {
			words[i] = line.split(" ")[0];
			prob[i] = Double.valueOf(line.split(" ")[1]);
			i++;
		}
		
		for (i = 0; i < words.length; i++) 
        	{
            		for (int j = i + 1; j < words.length; j++) 
            		{
                		if (words[i].compareTo(words[j])>0) 
                		{
                			temp = words[i];
					temp1 = prob[i];
                 			words[i] = words[j];
					prob[i] = prob[j];
                			words[j] = temp;
					prob[j] = temp1;
                		}
            		}
        	}

	}

	private static void printRootMatrix(int[][] a) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append("Root Matrix is:\n");
		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < a[0].length; j++) {
				sb.append(rootMatrix[i][j] + "\t");
			}
			
			sb.append("\n");
		}
		filewrite(sb.toString(), file2);
	}

	private static void printCostMatrix(double[][] a) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append("Cost Matrix is:\n");
		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < a[0].length; j++) {
				sb.append(Math.round(cost[i][j]*100)/100.0d + "\t");
			}
			sb.append("\n");
		}
		filewrite(sb.toString(), file2);
	}

	private static double sum(int i, int j) {
		double sum = 0;
		for (int k = i; k <= j; k++) {
			sum += prob[k - 1];
		}
		return sum;
	}

	private static double minimumCost(int i, int j) {
		double min = Double.MAX_VALUE;
		for (int k = i; k <= j; k++) {
			if ((cost[i - 1][k - 1] + cost[k][j]) < min) {
				min = cost[i - 1][k - 1] + cost[k][j];
				rootMatrix[i - 1][j] = k;
			}
		}
		return min;
	}

	private static void optimalSearchTree(int length) throws Exception {
		// initialize cost matrix and root matrix with size + 1
		
		cost = new double[length + 1][length + 1];
		rootMatrix = new int[length + 1][length + 1];
		for (int i = 1; i <= length; i++) {
			cost[i][i] = 0.0;
			rootMatrix[i][i] = 0;
			cost[i - 1][i] = prob[i - 1];
			rootMatrix[i - 1][i] = i;
		}
		for (int diagonal = 1; diagonal <= length - 1; diagonal++) {
			for (int i = 1; i <= length - diagonal; i++) {
				int j = i + diagonal;
				
				double minAvg = minimumCost(i, j);
				cost[i - 1][j] = minAvg + sum(i, j);
			}
		}
	
	}

	private static void constructOptimalBST(int i, int j, String message) throws Exception {
		if (i != j) {
			filewrite(words[rootMatrix[i][j] - 1] + " " + message,file2);
			constructOptimalBST(i, rootMatrix[i][j] - 1,"is the left child of " + words[rootMatrix[i][j] - 1] + ".");
			constructOptimalBST(rootMatrix[i][j], j, "is the right child of " + words[rootMatrix[i][j] - 1] + ".");
		}
	}

}

