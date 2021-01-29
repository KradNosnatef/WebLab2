package assets;

import java.util.ArrayList;

import pretreatment.RelationshipType;

public class WordKit {
    //脏方法传参区
    public static double guessJudge;//每次guess执行完后这里都会保留最近一次guess的评价
    public static String wordL,wordR;//句搜索里发现更佳猜测时把猜测词对保留在这里


    public static String stemming(String word){
        String stemmedWord=word.toLowerCase();
        return(stemmedWord);
    }
    public static RelationshipType toRelationshipType(String relationshipWord){
        if(relationshipWord.compareTo("Cause-Effect")==0)return(RelationshipType.CA_EF);
        if(relationshipWord.compareTo("Component-Whole")==0)return(RelationshipType.CO_WH);
        if(relationshipWord.compareTo("Entity-Destination")==0)return(RelationshipType.EN_DE);
        if(relationshipWord.compareTo("Product-Producer")==0)return(RelationshipType.PR_PR);
        if(relationshipWord.compareTo("Entity-Origin")==0)return(RelationshipType.EN_OR);
        if(relationshipWord.compareTo("Member-Collection")==0)return(RelationshipType.ME_CO);
        if(relationshipWord.compareTo("Message-Topic")==0)return(RelationshipType.ME_TO);
        if(relationshipWord.compareTo("Content-Container")==0)return(RelationshipType.CO_CO);
        if(relationshipWord.compareTo("Instrument-Agency")==0)return(RelationshipType.IN_AG);
        if(relationshipWord.compareTo("Other")==0)return(RelationshipType.OTHER);
        System.out.println("relationshipWord not match!");
        return(RelationshipType.OTHER);
    }

    public static String toRelationShipWord(RelationshipType type){
        String relationShipWord;
        switch(type){
            case CA_EF:{
                relationShipWord=new String("Cause-Effect");
                break;
            }
            case CO_CO:{
                relationShipWord=new String("Content-Container");
                break;
            }
            case CO_WH:{
                relationShipWord=new String("Component-Whole");
                break;
            }
            case EN_DE:{
                relationShipWord=new String("Entity-Destination");
                break;
            }
            case EN_OR:{
                relationShipWord=new String("Entity-Origin");
                break;
            }
            case IN_AG:{
                relationShipWord=new String("Instrument-Agency");
                break;
            }
            case ME_CO:{
                relationShipWord=new String("Member-Collection");
                break;
            }
            case ME_TO:{
                relationShipWord=new String("Message-Topic");
                break;
            }
            case OTHER:{
                relationShipWord=new String("Other");
                break;
            }
            case PR_PR:{
                relationShipWord=new String("Product-Producer");
                break;
            }
            default:{
                relationShipWord=new String("Other");
                break;
            }
        }
        return(relationShipWord);
    }

    //一元分隔符判断器
    public static boolean isSeparator(char c){
        if(((c>='A')&&(c<='Z'))||((c>='a')&&(c<='z'))||((c>='0')&&(c<='9')))return(false);
        else return(true);
    }
    //用于词内单个连接符的分隔符判断器
    public static boolean isSeparator(String string,int i){

        if(!isSeparator(string.charAt(i)))return(false);
        else if(i<=0||i>=string.length()-1)return(true);
        else if((!isSeparator(string.charAt(i-1)))&&(!isSeparator(string.charAt(i+1)))){
            String stringbuf=new String("|-|'|_|");
            if(stringbuf.indexOf("|"+string.charAt(i)+"|")==-1)return(true);
            return(false);
        }
        else return(true);
    }
    
    public static ArrayList<String> pretreat(String string){
        ArrayList<String> wordsArray=new ArrayList<String>();               //按需
        String word;

        int j=0,beginIndex=0;//分词-词根还原-去停用词
        for(int i=0;i<string.length();i++){
            if(WordKit.isSeparator(string, i)){
                if(beginIndex==i)beginIndex++;
                else{
                    word=WordKit.stemming(string.substring(beginIndex, i));
                    wordsArray.add(word);
                    j++;
                    beginIndex=i+1;
                }
            }
        }
        return(wordsArray);
    }
}
