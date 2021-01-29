package assets;

import pretreatment.RelationshipType;

public class WordKit {
    public static double guessJudge;//每次guess执行完后这里都会保留最近一次guess的评价

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
}
