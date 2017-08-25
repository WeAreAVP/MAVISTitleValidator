/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mavis2;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author rimsha@geeks
 */
public class Helper {

    private String sheetName = "MAVIS XML Result";
    private List<String> colValues;
    public String xmlPaths;
    public int totalTitles = 0;
    public int totalComponents = 0;
    public int totalCarriers = 0;

    public String readXLSXFile(String filename) {
        System.out.println("Reading excel file...");
        this.colValues = new ArrayList<String>();
        InputStream ExcelFileToRead;
        try {
            ExcelFileToRead = new FileInputStream(filename);
            @SuppressWarnings("resource")
            XSSFWorkbook wb = new XSSFWorkbook(ExcelFileToRead);
            XSSFSheet sheet = wb.getSheet(sheetName);
            if (sheet != null) {
                for (int j = 0; j < sheet.getLastRowNum() + 1; j++) {
                    Row row = sheet.getRow(j);
                    Cell cell = row.getCell(0, org.apache.poi.ss.usermodel.Row.CREATE_NULL_AS_BLANK); //get first cell
                    if (cell.getCellType() == XSSFCell.CELL_TYPE_BLANK) {
                        continue;
                    } else if (cell.getCellType() == XSSFCell.CELL_TYPE_STRING) {
                        this.colValues.add(cell.getStringCellValue());
                    } else if (cell.getCellType() == XSSFCell.CELL_TYPE_FORMULA) {
                        if (cell.getStringCellValue() != null && !cell.getStringCellValue().equals("")) {
                            this.colValues.add(cell.getStringCellValue());
                        }
                    }
                }
            } else {
                return "No tab existed with name 'MAVIS XML Result' in " + filename;
            }

        } catch (FileNotFoundException e) {
            return e.getMessage();
        } catch (IOException e) {
            return e.getMessage();
        }
        return "done";
    }

    public String createXML(String path, File filename) {
        System.out.println("Creaing xml file...");
        this.xmlPaths = "";//this.getClass().getClassLoader().getResourceAsStream("SomeTextFile.txt");
        String name = FilenameUtils.getBaseName(filename.getPath()) + "_" + System.currentTimeMillis() + ".xml";
        String xmlString = "";
        for (Integer index = 0; index < this.colValues.size(); index++) {
            xmlString += this.colValues.get(index);
        }
        xmlString += "</mavis>";

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            org.w3c.dom.Document document = builder.parse(new InputSource(new StringReader(xmlString)));
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(new File(path + File.separator + name));
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(source, result);
            this.xmlPaths = path + File.separator + name;
            System.out.println("XML successfully created for " + filename.getName());
        } catch (ParserConfigurationException | SAXException | IOException | IllegalArgumentException | TransformerException e) {
            return e.getMessage();
        }
        return "done";
    }

    public String validateXML() {
        try {
            System.out.println("Validating xml against xsd...");
            Source xmlFile = new StreamSource(this.xmlPaths);
            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            File sr = new File("mavis_schema.xsd");
            Source schemaFile = new StreamSource(sr);
            Schema schema = schemaFactory.newSchema(schemaFile);
            Validator validator = schema.newValidator();
            try {
                validator.validate(xmlFile);
                return "valid";
            } catch (SAXException e) {
                return e.getMessage();
            } catch (IOException ex) {
                return ex.getMessage();
            }
        } catch (SAXException ex) {
            return ex.getMessage();
        }
    }

    public void getCounts() {
        File inputFile = new File(this.xmlPaths);
        XMLParser parser = new XMLParser(inputFile);
        Map xmlMap = parser.parseXML();
        this.totalTitles = parser.getTotalTitles(xmlMap);
        this.totalComponents = parser.getTotalComponents(xmlMap);
        this.totalCarriers = parser.getTotalCarriers(xmlMap);
    }

    public void myTesting() {
        try {
            System.out.println("Validating xml against xsd...");
            Source xmlFile = new StreamSource("viaa.xml");
            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            File sr = new File("viaa.xsd");
            Source schemaFile = new StreamSource(sr);
            Schema schema = schemaFactory.newSchema(schemaFile);
            Validator validator = schema.newValidator();
            try {
                validator.validate(xmlFile);
                System.out.println("valid");
            } catch (SAXException e) {
                System.out.println(e.getMessage());
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        } catch (SAXException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
