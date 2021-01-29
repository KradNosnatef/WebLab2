package pretreatment;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import assets.WordKit;

public class Importer {
    String path;
    public KnowledgeLib knowledgeLib;

    public Importer(String path) {
        this.path = path;
        knowledgeLib=new KnowledgeLib();
    }

    public void startImport() throws IOException {
        ArrayList<String> strings;
        strings=segger(loadFile(),1);
        for(int i=0;i<strings.size();i++)indentifyRelationship(strings.get(i));
    }

    private String loadFile() throws IOException {
        File file=new File(path);
        FileReader fileReader=new FileReader(file);

        char[] cbuf=new char[1048576];
        int len=fileReader.read(cbuf);
        String string=new String(cbuf);
        string=string.substring(0,len);
        //System.out.println(string.length()+"\n"+len+"\n");
        fileReader.close();

        return(string);
    }

    private ArrayList<String> segger(String originString){//标准切分，把原始文件中的字符串按条目切分成不带序列号的分段
        ArrayList<String> strings=new ArrayList<String>();
        String stringSeg;

        int segStart=0;
        int counter=0;
        for(int i=0;i<originString.length();i++){
            if(originString.charAt(i)=='\n')counter++;
            if(counter==2){
                counter=0;
                stringSeg=originString.substring(segStart,i+1);
                segStart=i+1;
                strings.add(stringSeg);
            }
        }

        return(strings);
    }

    private ArrayList<String> segger(String originString,int mode){//mode 0是标准切分，mode 1只切分关系行；
        switch(mode){
            case 0:{
                return(segger(originString));
            }
            case 1:{
                ArrayList<String> strings=new ArrayList<String>();
                String stringSeg;
        
                int segStart=0;
                int counter=0;
                for(int i=0;i<originString.length();i++){
                    if(originString.charAt(i)=='\n')counter++;
                    if(counter==1&&segStart==0)segStart=i+1;
                    if(counter==2){
                        counter=0;
                        stringSeg=originString.substring(segStart,i+1);
                        segStart=0;
                        strings.add(stringSeg);
                    }
                }
        
                return(strings);
            }
            default:{
                System.out.println("invalid mode");
                return(null);
            }
        }
    }

    private void indentifyRelationship(String stringSeg){
        RelationshipType relationshipType;
        String wordL,wordR,relTypeWord;

        int seg1=0,seg2=0,seg3=0;
        for(int i=0;i<stringSeg.length();i++){
            if(stringSeg.charAt(i)=='(')seg1=i;
            if(stringSeg.charAt(i)==',')seg2=i;
            if(stringSeg.charAt(i)==')')seg3=i;
        }

        relTypeWord=stringSeg.substring(0,seg1);
        wordL=stringSeg.substring(seg1+1,seg2);
        wordR=stringSeg.substring(seg2+1,seg3);

        relationshipType=WordKit.toRelationshipType(relTypeWord);

        knowledgeLib.insert(wordL, wordR,relationshipType);
    }
}
