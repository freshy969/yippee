package com.yippee.pagerank;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ParseTest {

	private static String DEL = "', '";


	/**
	 * @param args
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws FileNotFoundException {
		// TODO Auto-generated method stub


		Scanner inScanner = new Scanner(new File("pr-input.hadoop"));
		while(inScanner.hasNext()){
			String line = inScanner.nextLine();
			String[] parts = line.substring(1,line.length()-1).split(DEL);
			if (parts.length>1) {
				String fromPage = parts[0].trim();
				String fromRank = parts[1].trim();
				String toPage = parts[2].trim();
				String toOutNum = parts[3].trim();
				// This is in order to calculate the page rank of the 'to' page "C. Imbriano"

				System.out.print("key: " + toPage + "\tvalue: ");
				System.out.println("'IN" + DEL + fromPage + DEL + fromRank + DEL + toOutNum + "'");

				System.out.print("key: ");
				System.out.print("'OUT" + DEL + fromPage + "'");
				System.out.println("\tvalue: " + toPage);
			}
		}

	}

}
