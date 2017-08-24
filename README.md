# MAVISTitleValidator
## MAVIS Title Validator and XML Generator

### User Guide

#### Java CLI tool
Built for AFC, custom, by AVPreserve  
2017-04-27

#### Requirements
* Java Runtime Environment (JRE) or Java Development Kit (JDK)
* MAVIS Title XML Validator dist folder

#### Instructions
1. Unzip and copy the MAVIS Title XML Validator zip file onto your desktop (or preferred location of your choice)
2. Open CMD (Command Prompt) on your Windows machine
3. Navigate to the dist folder within the MAVIS Title XML Validator folder
4. Initiate the validator with this command: java -Xmx1g -jar Mavis2.jar
5. The program will ask for the path to your completed and ready MAVIS XML Template spreadsheet (please name the spreadsheet with no “spaces” in the filename)
6. Provide the path and press enter
7. The program will ask for a path for a folder where you want the results to end up (the tool generates a log report and a final XML file for each spreadsheet it processes). This needs to be in the ingest directory. For Maya, this is the Y: drive.
8. Provide the path and press enter
9. Review the results and take your final XML file to MAVIS for import
10. If errors occur, the errors will be displayed in the standard output of your Command Prompt and in the resulting log file. Some errors result in no XML creation, and some are only warnings.


#### Tips for doing clean-up on resulting XML file:

* Open in Oxygen
* Do find and replace (with nothing) for the following sets of values:  
> &#8212;
> &#8220;
> &#8221;
> &#8211;
> &#8230;
> &#8217;
* And to be safe, do a search for any values in the document that start with &#
* If you find others like that, then do find and replace (with nothing) for each one you find
