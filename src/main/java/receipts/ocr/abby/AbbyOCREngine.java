package receipts.ocr.abby;

import global.exceptions.Bhagte2BandBajGaya;
import receipts.ocr.IOCREngine;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Vector;

public class AbbyOCREngine implements IOCREngine{

    private static Client restClient;

    static void init(){
        if(restClient == null)
            return;
        System.out.println("Process documents using ABBYY Cloud OCR SDK.\n");

        if (!checkAppId()) {
            //do something
        }

        ClientSettings.setupProxy();

        restClient = new Client();
        // replace with 'https://cloud.ocrsdk.com' to enable secure connection
        restClient.serverUrl = "http://cloud.ocrsdk.com";
        restClient.applicationId = ClientSettings.APPLICATION_ID;
        restClient.password = ClientSettings.PASSWORD;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {

        init();
        Vector<String> argList = new Vector<String>(Arrays.asList(args));

        // Select processing mode
        String mode = args[0];
        argList.remove(0);

        try {
            if (mode.equalsIgnoreCase("help")) {
                displayDetailedHelp(args[1]);
            } else if (mode.equalsIgnoreCase("recognize")) {
                performRecognition(argList);
            } else if (mode.equalsIgnoreCase("busCard")) {
                performBusinessCardRecognition(argList);
            } else if (mode.equalsIgnoreCase("textField")) {
                performTextFieldRecognition(argList);
            } else if (mode.equalsIgnoreCase("barcode")) {
                performBarcodeRecognition(argList);
            } else if (mode.equalsIgnoreCase("processFields")) {
                performFieldsRecognition(argList);
            } else if (mode.equalsIgnoreCase("MRZ")) {
                performMrzRecognition(argList);
            } else if (mode.equalsIgnoreCase("captureData")) {
                performCaptureData(argList);
            } else if( mode.equalsIgnoreCase("createTemplate")) {
                createTemplate(argList);
            } else {
                System.out.println("Unknown mode: " + mode);
                return;
            }
        } catch (Exception e) {
            System.out.println("Exception occured:" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Check that user specified application id and password.
     *
     * @return false if no application id or password
     */
    private static boolean checkAppId() {
        String appId = ClientSettings.APPLICATION_ID;
        String password = ClientSettings.PASSWORD;
        if (appId.isEmpty() || password.isEmpty()) {
            System.out
                    .println("Error: No application id and password are specified.");
            System.out.println("Please specify them in ClientSettings.java.");
            return false;
        }
        return true;
    }

    private static void displayHelp() {
        System.out
                .println("This program is able to recognize:\n"
                        + "\n"
                        + "1. Single- and multipage documents and convert them to txt, xml, pdf and other formats.\n"
                        + "  java TestApp recognize testImage.jpg result.xml\n"
                        + "  java TestApp recognize page1.jpg page2.jpg page3.jpg result.pdf --lang=French,Spanish\n"
                        + "\n"
                        + "2. Business cards to vCard, xml and csv\n"
                        + "  java TestApp busCard image.jpg result.xml\n"
                        + "\n"
                        + "3. Printed and handprinted text snippets\n"
                        + "  java TestApp textField image.jpg result.xml\n"
                        + "\n"
                        + "4. Barcodes\n"
                        + "  java TestApp barcode image.jpg result.xml\n"
                        + "\n"
                        + "5. Many different snippets on document\n"
                        + "  java TestApp processFields image1.jpg image2.jpg image3.tif settings.xml result.xml\n"
                        + "\n"
                        + "6. Machine-Readable Zones (MRZ) of Passports, ID cards, Visas and other official documents\n"
                        + "  java TestApp MRZ image.jpg result.xml\n"
                        + "\n"
                        + "For detailed help, call\n"
                        + "  java TestApp help <mode>\n"
                        + "where <mode> is one of: recognize, busCard, textField, barcode, checkmark, processFields, captureData, createTemplate");
    }

    /**
     * Display detailed help for each processing mode.
     */
    private static void displayDetailedHelp(String mode) {
        if (mode.equalsIgnoreCase("recognize")) {
            displayRecognizeHelp();
        } else if (mode.equalsIgnoreCase("busCard")) {
            displayBusCardHelp();
        } else if (mode.equalsIgnoreCase("textField")) {
            displayTextFieldHelp();
        } else if (mode.equalsIgnoreCase("barcode")) {
            displayBarcodeHelp();
        } else if (mode.equalsIgnoreCase("processFields")) {
            displayProcessFieldsHelp();
        } else if (mode.equalsIgnoreCase("MRZ")) {
            displayProcessMrzHelp();
        } else if (mode.equalsIgnoreCase("captureData")) {
            displayCaptureDataHelp();
        } else if (mode.equalsIgnoreCase("createTemplate")) {
            displayCreateTemplateHelp();
        } else {
            System.out.println("Unknown processing mode.");
        }
    }

    private static void displayRecognizeHelp() {
        System.out
                .println("Recognize single or multipage documents.\n"
                        + "\n"
                        + "Usage:\n"
                        + "java TestApp recognize [--lang=<languages>] <file> [<file2> ..] <output file>\n"
                        + "\n"
                        + "Output format is selected by output file extension. Possible values are:\n"
                        + ".txt, .xml, .pdf, .docx, .rtf\n"
                        + "\n"
                        + "Examples:\n"
                        + "java TestApp recognize image.tif result.txt\n"
                        + "java TestApp recognize --lang=French,Spanish page1.png page2.png page3.png result.pdf\n"
                        + "java TestApp recognize --lang=Japanese image.jpg output.rtf\n");
    }

    /**
     * Parse command line and recognize one or more documents.
     */
    private static boolean performRecognition(Vector<String> argList)
            throws Exception {
        String language = extractRecognitionLanguage(argList);
        String outputPath = argList.lastElement();
        argList.remove(argList.size() - 1);
        // argList now contains list of source images to process

        ProcessingSettings.OutputFormat outputFormat = outputFormatByFileExt(outputPath);

        ProcessingSettings settings = new ProcessingSettings();
        settings.setLanguage(language);
        settings.setOutputFormat(outputFormat);

        Task task = null;
        if (argList.size() == 1) {
            System.out.println("Uploading file..");
            task = restClient.processImage(argList.elementAt(0), settings);

        } else if (argList.size() > 1) {

            // Upload images via submitImage and start recognition with
            // processDocument
            for (int i = 0; i < argList.size(); i++) {
                System.out.println(String.format("Uploading image %d/%d..",
                        i + 1, argList.size()));
                String taskId = null;
                if (task != null) {
                    taskId = task.Id;
                }

                Task result = restClient.submitImage(argList.elementAt(i),
                        taskId);
                if (task == null) {
                    task = result;
                }
            }
            task = restClient.processDocument(task.Id, settings);

        } else {
            System.out.println("No files to process.");
            return false;
        }

        return waitAndDownloadResult(task, outputPath);
    }

    private static void displayBusCardHelp() {
        System.out
                .println("Recognize single business card.\n"
                        + "\n"
                        + "Usage:\n"
                        + "java TestApp busCard [--lang=<languages>] <file> <output file>\n"
                        + "\n"
                        + "Output format is selected by output file extension. Possible values are:\n"
                        + ".vcf, .xml, .csv\n"
                        + "\n"
                        + "Examples:\n"
                        + "java TestApp busCard image.tif result.vcf\n"
                        + "java TestApp busCard --lang=French,Spanish image.png result.xml\n");
    }

    /**
     * Perform recognition of single business card.
     *
     * Recognized result will be saved in special format for business cards:
     * vCard, csv or xml
     */
    private static void performBusinessCardRecognition(Vector<String> argList)
            throws Exception {
        String language = extractRecognitionLanguage(argList);
        String outputPath = argList.lastElement();
        argList.remove(argList.size() - 1);
        // argList now contains list of source images to process

        BusCardSettings.OutputFormat outputFormat = bcrOutputFormatByFileExt(outputPath);

        BusCardSettings settings = new BusCardSettings();
        settings.setLanguage(language);

        settings.setOutputFormat(outputFormat);

        if (argList.size() != 1) {
            System.out.println("Invalid number of files to process.");
            return;
        }

        System.out.println("Uploading..");
        Task task = restClient.processBusinessCard(argList.elementAt(0),
                settings);
        waitAndDownloadResult(task, outputPath);
    }

    private static void displayTextFieldHelp() {
        System.out
                .println("Recognize printed or handprinted text field.\n"
                        + "\n"
                        + "Usage:\n"
                        + "java TestApp textField [--lang=<languages>] [--options=<options] <file> <output file>\n"
                        + "\n"
                        + "<options> - options passed directly to processTextField RESTful call\n"
                        + "\n"
                        + "Examples:\n"
                        + "java TestApp textField image.tif result.xml\n"
                        + "java TestApp textField --options='letterSet=0123456789/&regExp=[0-9][0-9]' image.tif result.xml\n");
    }

    private static void performTextFieldRecognition(Vector<String> argList)
            throws Exception {
        String language = extractRecognitionLanguage(argList);
        String options = extractExtraOptions(argList);
        String outputPath = argList.lastElement();
        argList.remove(argList.size() - 1);
        // argList now contains list of source images to process

        TextFieldSettings settings = new TextFieldSettings();
        settings.setLanguage(language);
        if (options != null) {
            settings.setOptions(options);
        }

        // TODO - different processing options

        if (argList.size() != 1) {
            System.out.println("Invalid number of files to process.");
            return;
        }

        System.out.println("Uploading..");
        Task task = restClient.processTextField(argList.elementAt(0), settings);

        waitAndDownloadResult(task, outputPath);
    }

    private static void displayBarcodeHelp() {
        System.out
                .println("Recognize barcode.\n" + "\n" + "Usage:\n"
                        + "java TestApp barcode <file> <output file>\n" + "\n"
                        + "Examples:\n"
                        + "java TestApp barcode image.tif result.xml\n");
    }

    private static void performBarcodeRecognition(Vector<String> argList)
            throws Exception {
        String outputPath = argList.lastElement();
        argList.remove(argList.size() - 1);
        // argList now contains list of source images to process

        BarcodeSettings settings = new BarcodeSettings();

        // TODO: different barcode types

        if (argList.size() != 1) {
            System.out.println("Invalid number of files to process.");
            return;
        }

        System.out.println("Uploading..");
        Task task = restClient.processBarcodeField(argList.elementAt(0),
                settings);

        waitAndDownloadResult(task, outputPath);
    }

    private static void displayProcessFieldsHelp() {
        System.out
                .println("Process different snippets in one- or multipage document.\n"
                        + "\n"
                        + "Usage:\n"
                        + "java TestApp processFields <file1> [file2 ..] <settings.xml> <output file>\n"
                        + "\n"
                        + "For details how to create xml settings see\n"
                        + "http://ocrsdk.com/documentation/specifications/xml-scheme-field-settings/\n"
                        + "\n"
                        + "Examples:\n"
                        + "java TestApp processFields image1.tif settings.xml result.xml\n"
                        + "java TestApp processFields image1.tif image2.tif image3.tif settings.xml result.xml\n");
    }

    /**
     * Perform field-level recognition using processFields call.
     *
     * For details see
     * http://ocrsdk.com/documentation/apireference/processFields/
     */
    private static void performFieldsRecognition(Vector<String> argList)
            throws Exception {

        if (argList.size() < 3) {
            System.out.println("Invalid number of arguments");
            return;
        }

        String outputPath = argList.lastElement();
        argList.remove(argList.size() - 1);

        String settingsPath = argList.lastElement();
        argList.remove(argList.size() - 1);
        // argList now contains list of source images to process

        Task task = null;
        for (int i = 0; i < argList.size(); i++) {
            System.out.println(String.format("Uploading image %d/%d..\n",
                    i + 1, argList.size()));

            String taskId = null;
            if (task != null) {
                taskId = task.Id;
            }

            Task result = restClient.submitImage(argList.elementAt(i), taskId);
            if (task == null) {
                task = result;
            }
        }

        System.out.println("Processing..");
        task = restClient.processFields(task.Id, settingsPath);

        waitAndDownloadResult(task, outputPath);
    }

    private static void displayProcessMrzHelp() {
        System.out
                .println("Recognize Machine-Readable Zones of official documents\n"
                        + "Both 2 and 3-line MRZ are supported."
                        + "\n" + "Usage:\n"
                        + "java TestApp MRZ <file> <output file.xml>\n" + "\n");
    }

    private static void performMrzRecognition(Vector<String> argList)
            throws Exception {
        String outputPath = argList.lastElement();
        argList.remove(argList.size() - 1);
        // argList now contains list of source images to process

        if (argList.size() != 1) {
            System.out.println("Invalid number of files to process.");
            return;
        }

        System.out.println("Uploading..");
        Task task = restClient.processMrz(argList.elementAt(0));

        waitAndDownloadResult(task, outputPath);
    }

    private static void displayCaptureDataHelp() {
        System.out
                .println("Recognize Machine-Readable Zones of official documents\n"
                        + "Both 2 and 3-line MRZ are supported."
                        + "\n" + "Usage:\n"
                        + "java TestApp captureData <file> MRZ <output file>\n" + "\n"
                        + "Examples:\n"
                        + "java TestApp captureData image.tif MRZ result.xml\n");
    }

    private static void performCaptureData(Vector<String> argList)
            throws Exception {
        String outputPath = argList.lastElement();
        argList.remove(argList.size() - 1);

        String templateName = argList.lastElement();
        argList.remove(argList.size()-1 );

        // argList now contains list of source images to process

        if (argList.size() != 1) {
            System.out.println("Invalid number of files to process.");
            return;
        }

        System.out.println("Uploading..");
        Task task = restClient.captureData(argList.elementAt(0), templateName);

        waitAndDownloadResult(task, outputPath);
    }

    private static void displayCreateTemplateHelp() {
        System.out
                .println("TODO\n");

    }

    private static void createTemplate(Vector<String> argList) throws Exception {
        if( argList.size() != 3) {
            System.out.println( "Invalid number of arguments.");
            return;
        }

        String imagePath = argList.elementAt(0);
        String templateName = argList.elementAt(1);
        String settingsPath = argList.elementAt(2);

        System.out.println("Uploading image..");
        Task task = restClient.submitImage(imagePath, null);

        System.out.println("Creating template..");
        task = restClient.createTemplate(task.Id, templateName, settingsPath);
        System.out.println("Waiting..");
        task = waitForCompletion(task);

        if( task.Status == Task.TaskStatus.Completed) {
            System.out.println( "Done.");
        } else {
            System.out.println("Error creating template.");
        }
    }

    /**
     * Wait until task processing finishes
     */
    private static Task waitForCompletion(Task task) throws Exception {
        while (task.isTaskActive()) {
            Thread.sleep(2000);

            System.out.println("Waiting..");
            task = restClient.getTaskStatus(task.Id);
        }
        return task;
    }

    /**
     * Wait until task processing finishes and download result.
     */
    private static boolean waitAndDownloadResult(Task task, String outputPath)
            throws Exception {
        task = waitForCompletion(task);

        if (task.Status == Task.TaskStatus.Completed) {
            System.out.println("Downloading..");
            restClient.downloadResult(task, outputPath);
            System.out.println("Ready");
            return true;
        } else if (task.Status == Task.TaskStatus.NotEnoughCredits) {
            System.out.println("Not enough credits to process document. "
                    + "Please add more pages to your application's account.");
        } else {
            System.out.println("Task failed");
        }
        return false;

    }

    /**
     * Extract value of given parameter from command-line parameters. Parameter
     * is removed after extraction
     *
     * @return value of parameter or null
     */
    private static String extractParameterValue(String parameterName,
                                                Vector<String> args) {
        String prefix = "--" + parameterName + "=";

        for (int i = 0; i < args.size(); i++) {
            String arg = args.elementAt(i);
            if (arg.startsWith(prefix)) {
                String value = arg.substring(prefix.length());
                args.remove(i);
                return value;
            }
        }

        return null;
    }

    /**
     * Extract recognition language from command-line parameters. After
     * extraction parameter is removed
     *
     * @return recognition language
     */
    private static String extractRecognitionLanguage(Vector<String> args) {
        // Recognition language parameter has form --lang=<languges>

        String lang = extractParameterValue("lang", args);
        if (lang != null) {
            return lang;
        }

        System.out
                .println("Warning! The document will be processed with English language.\n"
                        + "To change this, specify --lang=<languages> option.\n");

        return "English";
    }

    /**
     * Extract extra RESTful options from command-line parameters. Parameter is
     * removed after extraction
     *
     * @return extra options string or null
     */
    private static String extractExtraOptions(Vector<String> args) {
        // Extra options parameter has from --options=<options>
        return extractParameterValue("options", args);
    }

    /**
     * Extract output format from extension of output file.
     */
    private static ProcessingSettings.OutputFormat outputFormatByFileExt(
            String filePath) {
        int extIndex = filePath.lastIndexOf('.');
        if (extIndex < 0) {
            System.out
                    .println("No file extension specified. Plain text will be used as output format.");
            return ProcessingSettings.OutputFormat.txt;
        }
        String ext = filePath.substring(extIndex).toLowerCase();
        if (ext.equals(".txt")) {
            return ProcessingSettings.OutputFormat.txt;
        } else if (ext.equals(".xml")) {
            return ProcessingSettings.OutputFormat.xml;
        } else if (ext.equals(".pdf")) {
            return ProcessingSettings.OutputFormat.pdfSearchable;
        } else if (ext.equals(".docx")) {
            return ProcessingSettings.OutputFormat.docx;
        } else if (ext.equals(".rtf")) {
            return ProcessingSettings.OutputFormat.rtf;
        } else {
            System.out
                    .println("Unknown output extension. Plain text will be used.");
            return ProcessingSettings.OutputFormat.txt;
        }
    }

    /**
     * Extract output format for business card from extension of output file.
     */
    private static BusCardSettings.OutputFormat bcrOutputFormatByFileExt(
            String filePath) {
        int extIndex = filePath.lastIndexOf('.');
        if (extIndex < 0) {
            System.out
                    .println("No file extension specified. vCard will be used as output format.");
            return BusCardSettings.OutputFormat.vCard;
        }

        String ext = filePath.substring(extIndex).toLowerCase();
        if (ext.equals(".vcf")) {
            return BusCardSettings.OutputFormat.vCard;
        } else if (ext.equals(".xml")) {
            return BusCardSettings.OutputFormat.xml;
        } else if (ext.equals(".csv")) {
            return BusCardSettings.OutputFormat.csv;
        }

        System.out
                .println("Invalid file extension. vCard will be used as output format.");
        return BusCardSettings.OutputFormat.vCard;
    }


    @Override
    public Path scanAndConvert(Path inFile, Path outFile) {
        try {
            init();
            //check if input file exists
            if(Files.exists(inFile)){
                Vector<String> argList = new Vector<String>();
                argList.add(inFile.toString());
                argList.add(outFile.toString());
                if(performRecognition(argList)){
                    //success
                    if(Files.exists(outFile)){
                        //file created with contents recognized
                        return outFile;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();;
            throw new Bhagte2BandBajGaya(e);
        }
        return null;
    }
}
