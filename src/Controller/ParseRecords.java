package Controller;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class ParseRecords {
	/*
	 * ParseRecords retrieves information on the previous session from a fixed
	 * location and file.
	 */
	String directoryInUse;
	HashMap<String, ArrayList<Integer>> records; /* Key=Folder Path; Value = list of records */
	ArrayList<Integer> singleRecordIndices; /*
											 * List of records that will be repeatedly reinitialized and placed into the
											 * above hashmap
											 */

	public ParseRecords() throws FileNotFoundException {
		try {
			// this.records = new ArrayList<Record>();
			this.records = new HashMap<String, ArrayList<Integer>>();

			FileReader fr = new FileReader("records.txt");
			Scanner sc = new Scanner(fr);
			Scanner scline = new Scanner("");

			while (sc.hasNext()) {
				scline = new Scanner(sc.nextLine());
				scline.useDelimiter(";");
				String directory = scline.next();
				singleRecordIndices = new ArrayList<Integer>();
				while (scline.hasNext()) {
					singleRecordIndices.add(Integer.parseInt(scline.next()));
				}
				records.put(directory, singleRecordIndices);
				scline.close();
			}
			sc.close();
		} catch (FileNotFoundException fnfe) {
			// should never happen
		}
	}

	public void updateAndWriteRecord(int[] files, ArrayList<Integer> indices) {
		// update the record
		records.put(directoryInUse, indices);

		// write all records to the text file
		try {
			FileWriter fstream = new FileWriter("records.txt");
			BufferedWriter out = new BufferedWriter(fstream);

			for (String directory : records.keySet()) {
				StringBuilder sb = new StringBuilder();
				sb.append(directory);

				for (Integer i : records.get(directory)) {
					if (i == -1) {
						//sb.append(";" + i);
					} else {
						if (directory.equals(this.directoryInUse)) {
							sb.append((";" + (i - files[i])));
						} else {
							sb.append((";" + i));
						}

					}
				}
				out.write(sb.toString() + "\r\n");
			}
			out.close();
		} catch (Throwable t) {
			System.out.println("something went horrible wrong");
			t.printStackTrace();
		}
	}

	public ArrayList<Integer> getRecords(String wantedDirectory) {
		// return records;
		directoryInUse = wantedDirectory;
		return records.get(wantedDirectory);
	}
}
