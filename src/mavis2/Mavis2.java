/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mavis2;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FilenameUtils;

/**
 *
 * @author rimsha@geeks
 */
public class Mavis2 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.print("MAVIS XML Title Validation Tool\n\n");
        System.out.print("By AVPreserve https://www.avpreserve.com\n\n");
        // create a scanner so we can read the command-line input
        Scanner scanner = new Scanner(System.in);

       //  prompt for the user's name
        System.out.print("File/folder to process: ");

        // get their input as a String
        String inputPath = scanner.nextLine();

        // prompt for their age
        System.out.print("Ouput folder path: ");

       
        String outputPath = scanner.nextLine();
        
        File inputFile = new File(inputPath);
        File outputFile = new File(outputPath);
        String[] extensions = new String[]{"xlsx", "xls"};
        if (!inputFile.exists()) {
            System.out.print("File/folder to process does not exists");
            return;
        } else if (inputFile.isFile() && !Arrays.asList(extensions).contains(FilenameUtils.getExtension(inputPath))) {
            System.out.print("Invalid file extension. Please select excel only." + FilenameUtils.getExtension(inputPath));
            return;
        }

        if (!outputFile.exists() || !outputFile.isDirectory()) {
            System.out.print("Ouput folder does not exists.");
            return;
        }

        StringBuilder str = new StringBuilder();
        if (inputFile.isDirectory()) {
            File[] directoryListing = inputFile.listFiles();
            if (directoryListing != null) {
                for (File child : directoryListing) {
                    if (FilenameUtils.getExtension(child.getAbsolutePath()).equals("xlsx") || FilenameUtils.getExtension(child.getAbsolutePath()).equals("xls")) {
                        str.append(child.getName());
                        str.append(System.getProperty("line.separator"));
                        System.out.println("Processing " + child.getName());
                        Helper h = new Helper();
                        String read = h.readXLSXFile(child.getPath());
                        if (read.equals("done")) {
                            String create = h.createXML(outputPath, child);
                            if (create.equals("done")) {
                                File f = new File(h.xmlPaths);
                                str.append(f.getName() + " file is created");
                                h.getCounts();
                                str.append("Total Titles: " + h.totalTitles);
                                str.append(System.getProperty("line.separator"));
                                str.append("Total Components: " + h.totalComponents);
                                str.append(System.getProperty("line.separator"));
                                str.append("Total Carriers: " + h.totalCarriers);
                                str.append(System.getProperty("line.separator"));
                                String xmlValidation = h.validateXML();
                                if (xmlValidation.equals("valid")) {
                                    str.append(f.getName() + ": XML is valid");
                                    System.out.println(f.getName() + ": XML is valid\n");
                                    System.out.println("Process succesfully completed.\n");
                                } else {
                                    System.out.println("XSD validation failed. Error: " + xmlValidation);
                                    str.append("XSD validation failed. Error: " + xmlValidation);
                                }
                            } else {
                                System.out.println("Create XML failed. Error: " + create);
                                str.append("Create XML failed. Error: " + create);
                            }
                        } else {
                            System.out.println("Reading Excel failed. Error: " + read);
                            str.append("Reading Excel failed. Error: " + read);
                        }
                    }
                }
            }
        } else {
            str.append(inputFile.getName());
            str.append(System.getProperty("line.separator"));
            System.out.println("Processing " + inputFile.getName());
            Helper h = new Helper();
            String read = h.readXLSXFile(inputPath);
            if (read.equals("done")) {
                String create = h.createXML(outputPath, inputFile);
                if (create.equals("done")) {
                    File f = new File(h.xmlPaths);
                    str.append(f.getName() + " file is created");
                    h.getCounts();
                    str.append("Total Titles: " + h.totalTitles);
                    str.append(System.getProperty("line.separator"));
                    str.append("Total Components: " + h.totalComponents);
                    str.append(System.getProperty("line.separator"));
                    str.append("Total Carriers: " + h.totalCarriers);
                    str.append(System.getProperty("line.separator"));
                    String xmlValidation = h.validateXML();
                    if (xmlValidation.equals("valid")) {
                        str.append(f.getName() + ": XML is valid");
                        System.out.println(f.getName() + ": XML is valid\n");
                        System.out.println("Process succesfully completed.\n");
                    } else {
                        System.out.println("XSD validation failed. Error: " + xmlValidation);
                        str.append("XSD validation failed. Error: " + xmlValidation);
                    }
                } else {
                    System.out.println("Create XML failed. Error: " + create);
                    str.append("Create XML failed. Error: " + create);
                }
            } else {
                System.out.println("Reading Excel failed. Error: " + read);
                str.append("Reading Excel failed. Error: " + read);
            }
        }

        System.out.println("Generating report.. ");
        try {
            FileWriter fileWritter = new FileWriter(outputFile.getPath() + File.separator + "Report_" + System.currentTimeMillis() + ".txt", true);
            BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
            String fileSystem = str.toString();
            bufferWritter.write(fileSystem);
            bufferWritter.close();
            System.out.println("Report generated at: " + outputFile.getPath() + File.separator + "Report_" + System.currentTimeMillis() + ".txt");
        } catch (IOException e) {
            System.out.println("Issue while writing report: " + e.getMessage());
        }
    }

}
