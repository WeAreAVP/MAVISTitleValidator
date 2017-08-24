# MAVISTitleValidator
## MAVIS Title Validator and XML Generator

### User Guide

#### Java CLI tool
Built for AFC, custom, by AVPreserve  
2017-04-27

#### Requirements
* Java Runtime Environment (JRE) or Java Development Kit (JDK)
* MAVIS Title XML Validator dist folder
* Completed [AFC MAVIS Spreadsheet](https://github.com/avpreserve/MAVISTitleValidator/blob/master/AFC%20MAVIS%20Spreadsheet%20Template.xlsx) (this is the Excel template for generating batch MAVIS title xml according to AFC's specifications, and it is consumed by the MAVIS Title XML Validator in order to generate valid MAVIS XML for import)

#### Instructions
1. Unzip and copy the [MAVIS Title XML Validator zip file](https://github.com/avpreserve/MAVISTitleValidator/blob/master/MAVIS-Title-XML-Validator.zip) onto your desktop (or preferred location of your choice)
2. Open CMD (Command Prompt) on your Windows machine
3. Navigate to the dist folder within the MAVIS Title XML Validator folder
4. Initiate the validator with this command: java -Xmx1g -jar Mavis2.jar
5. The program will ask for the path to your completed and ready [AFC MAVIS Spreadsheet](https://github.com/avpreserve/MAVISTitleValidator/blob/master/AFC%20MAVIS%20Spreadsheet%20Template.xlsx) (please name the spreadsheet with no “spaces” in the filename)
6. Provide the path and press enter
7. The program will ask for a path for a folder where you want the results to end up (the tool generates a log report and a final XML file for each spreadsheet it processes). This needs to be in the ingest directory. For Maya, this is the Y: drive.
8. Provide the path and press enter
9. Review the results and take your final XML file to MAVIS for import
10. If errors occur, the errors will be displayed in the standard output of your Command Prompt and in the resulting log file. Some errors result in no XML creation, and some are only warnings.
