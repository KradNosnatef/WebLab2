package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import assets.WordKit;
import pretreatment.Importer;
import pretreatment.KnowledgeLib;

public class Service {
    public Service() {

    }

    public void gotoMainMenu() throws IOException {
        String[] optionArray = {    "exit", 
                                    "import"};
        menuPrinter("Main Manu", optionArray);

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        char option = (char) bufferedReader.read();
        switch(option){
            case '0':{
                break;
            }
            case '1':{
                gotoImporter();
                break;
            }
            default:{
                System.out.println("invalid input");
                gotoMainMenu();
                break;
            }
        }
    }

    private void menuPrinter(String title, String[] optionArray) {
        System.out.println("----" + title + "----");
        for (int i = 0; i < optionArray.length; i++) {
            System.out.println(" --" + i + ":" + optionArray[i]);
        }
    }

    private void gotoImporter() throws IOException {
        Importer importer=new Importer("T:\\TEMP\\train.txt");
        importer.startImport();
        for(;;){
            String[] optionArray = {    "return to last menu", 
                                        "start guess"};
            menuPrinter("Main Manu", optionArray);
    
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
            char option = (char) bufferedReader.read();
            switch(option){
                case '0':{
                    importer=null;
                    gotoMainMenu();
                    return;
                }
                case '1':{
                    gotoGuess(importer.knowledgeLib);
                    break;
                }
                default:{
                    System.out.println("invalid input");
                    break;
                }
            }
        }
    }

    private void gotoGuess(KnowledgeLib knowledgeLib) throws IOException {
        System.out.println("input left word:");
        BufferedReader bufferedReader1= new BufferedReader(new InputStreamReader(System.in));
        String wordL=bufferedReader1.readLine();
        System.out.println("input right word:");
        BufferedReader bufferedReader2= new BufferedReader(new InputStreamReader(System.in));
        String wordR=bufferedReader2.readLine();

        String result=WordKit.toRelationShipWord(knowledgeLib.guess(wordL, wordR));
        System.out.println("i guess type is "+result);
    }
}
