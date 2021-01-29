package pretreatment;

import java.util.ArrayList;
import java.util.List;

import assets.WordKit;

public class KnowledgeLib {//用数组存储实体，用链式结构来存储关系
    public ArrayList<Entity> entities; 
    

    public class Entity{
        public String word;
        public ArrayList<Relationship> relationships;

        public Entity(String word){
            this.word=word;
            relationships=new ArrayList<Relationship>();
        }

        //找关系彼端
        public Entity gvtOtherSide(int index){
            Role role=gvtRole(index);
            if(role==Role.LEFT)return(this.relationships.get(index).entityR);
            else return(this.relationships.get(index).entityL);
        }

        //给关系节点或关系节点在本实体关系节点数组中的引用下标，返回本实体在这个关系中的地位
        public Role gvtRole(Relationship relationship){
            if(relationship.entityL==this)return(Role.LEFT);
            else if(relationship.entityR==this)return(Role.RIGHT);
            else return(Role.IRRELEVANT);
        }
        public Role gvtRole(int index){
            if(index>=relationships.size())System.out.println("invalid index");

            return(gvtRole(relationships.get(index)));
        }

        //返回和目标的所有相似关系对
        public DualIndexList gvtSimilarRelationshipPairs(Entity dstEntity){
            DualIndexList dualIndexList=new DualIndexList(this,dstEntity);

            for(int i=0;i<this.relationships.size();i++){
                for(int j=0;j<dstEntity.relationships.size();j++){
                    if((this.relationships.get(i).type==dstEntity.relationships.get(j).type)&&(this.gvtRole(i)==dstEntity.gvtRole(j))&&(this.relationships.get(i).type!=RelationshipType.OTHER))dualIndexList.insert(i,j);
                }
            }

            return(dualIndexList);
        }
        //返回相似度
        public double gvtSimilarity(Entity dstEntity){
            double result=gvtSimilarRelationshipPairs(dstEntity).indexes1.size();
            result=result/(double)(this.relationships.size()*dstEntity.relationships.size());
            return(result);
        }

        //返回同位距离，如果非同位体则会返回-1，limit是搜索深度，同位距离大于limit的将被视为非同位体，逐渐加深的深度受限优先搜索
        public int gvtParDistance(Entity dstEntity,int limit){
            if(this==dstEntity)return(0);
            if(limit==0)return(-1);
            DualIndexList dualIndexList=gvtSimilarRelationshipPairs(dstEntity);
            if(dualIndexList.indexes1.size()==0)return(-1);
            for(int i=0;i<=limit-1;i++){
                for(int j=0;j<dualIndexList.indexes1.size();j++){
                    int result=this.gvtOtherSide(dualIndexList.indexes1.get(j)).gvtParDistance(dstEntity.gvtOtherSide(dualIndexList.indexes2.get(j)),i);
                    if(result!=-1)return(result+1);
                }
            }
            return(-1);
        }

        //根据已有知识库，返回与目标实体的关系推测，每次运行都会把运行评价保存在wordkit的评价变量里
        public RelationshipType gvtRelationshipGuess(Entity dstEntity){

            boolean ifOther=false;
            for(int i=0;i<this.relationships.size();i++){
                if(this.gvtOtherSide(i)==dstEntity){
                    if(this.relationships.get(i).type==RelationshipType.OTHER)ifOther=true;
                    else{
                        WordKit.guessJudge=0;
                        return(this.relationships.get(i).type);
                    }
                }
            }
            if(ifOther){
                WordKit.guessJudge=0;
                return(RelationshipType.OTHER);
            }

            int minParDistance=100;
            RelationshipType relationshipType=RelationshipType.OTHER;
            for(int i=0;i<this.relationships.size();i++){
                int parDistance=this.gvtOtherSide(i).gvtParDistance(dstEntity,3);
                if((parDistance!=-1)&&(parDistance<minParDistance)){
                    minParDistance=parDistance;
                    relationshipType=this.relationships.get(i).type;
                }
            }
            for(int i=0;i<dstEntity.relationships.size();i++){
                int parDistance=dstEntity.gvtOtherSide(i).gvtParDistance(this,3);
                if((parDistance!=-1)&&(parDistance<minParDistance)){
                    minParDistance=parDistance;
                    relationshipType=dstEntity.relationships.get(i).type;
                }
            }
            if(minParDistance!=100){
                WordKit.guessJudge=minParDistance;
                return(relationshipType);
            }

            double maxSimularity=0;
            for(int i=0;i<this.relationships.size();i++){
                double simularity=this.gvtOtherSide(i).gvtSimilarity(dstEntity);
                if(simularity>maxSimularity){
                    maxSimularity=simularity;
                    relationshipType=this.relationships.get(i).type;
                }
            }
            for(int i=0;i<dstEntity.relationships.size();i++){
                double simularity=dstEntity.gvtOtherSide(i).gvtSimilarity(this);
                if(simularity>maxSimularity){
                    maxSimularity=simularity;
                    relationshipType=dstEntity.relationships.get(i).type;
                }
            }
            if(maxSimularity>0.01)WordKit.guessJudge=(1/maxSimularity)+3;
            else WordKit.guessJudge=300;
            return(relationshipType);
        }

        
        public class DualIndexList{//用来存相似关系对组的玩意
            public Entity entity1,entity2;
            public ArrayList<Integer> indexes1,indexes2;//entity1的下标为indexes1[i]的关系和entity2的下标为indexes2[i]的关系为一对相似关系对

            public DualIndexList(Entity entity1,Entity entity2){
                this.entity1=entity1;
                this.entity2=entity2;
                this.indexes1=new ArrayList<Integer>();
                this.indexes2=new ArrayList<Integer>();
            }

            public void insert(int i1,int i2){
                indexes1.add(i1);
                indexes2.add(i2);
            }
        }
    }
    public class Relationship{
        public RelationshipType type;
        public Entity entityL,entityR;

        public Relationship(RelationshipType type,Entity entityL,Entity entityR){
            this.type=type;
            this.entityL=entityL;
            this.entityR=entityR;
        }
    }

    public KnowledgeLib(){
        this.entities=new ArrayList<Entity>();
    }

    public void insert(String wordL,String wordR,RelationshipType type){
        Entity entityL=search(wordL,1);
        Entity entityR=search(wordR,1);
        Relationship relationship=new Relationship(type, entityL, entityR);
        entityL.relationships.add(relationship);
        entityR.relationships.add(relationship);
    }

    public Entity search(String word,int mode){//mode 0是标准搜索,mode 1是若搜不到则建立这个实体并返回
        switch(mode){
            case 0:{
                return(search(word));
            }
            case 1:{
                Entity entity=search(word);
                if(entity!=null)return(entity);
                else {
                    entity=new Entity(WordKit.stemming(word));
                    entities.add(entity);
                    return(entity);
                }
            }
            default:{
                System.out.println("invalid mode input");
                return(null);
            }
        }
    }

    public Entity search(String word){//标准搜索，搜不到返回null
        String wordBuf=WordKit.stemming(word);

        for(int i=0;i<entities.size();i++)if(wordBuf.compareTo(entities.get(i).word)==0)return(entities.get(i)); 
        
        return(null);
    }

    public RelationshipType guess(String wordL,String wordR){
        String wordLBuf=WordKit.stemming(wordL),wordRBuf=WordKit.stemming(wordR);

        //System.out.println("guessing "+wordLBuf+" and "+wordRBuf);

        Entity entityL=search(wordLBuf),entityR=search(wordRBuf);
        if(entityL==null||entityR==null){
            WordKit.guessJudge=300;
            return(RelationshipType.OTHER);
        }
        return(entityL.gvtRelationshipGuess(entityR));
    }

    public RelationshipType guess(String sentence){
        ArrayList<String> words=WordKit.pretreat(sentence);
        RelationshipType typeBuf=null,result=null;
        double minJudge=500;

        for(int i=0;i<words.size()-1;i++){
            for(int j=i+1;j<words.size();j++){
                typeBuf=guess(words.get(i),words.get(j));
                //System.out.println("Judge is "+WordKit.guessJudge);
                if(WordKit.guessJudge<minJudge){

                    //System.out.println("better guess !Judge is "+WordKit.guessJudge);

                    result=typeBuf;
                    minJudge=WordKit.guessJudge;
                    WordKit.wordL=words.get(i);
                    WordKit.wordR=words.get(j);
                }
            }
        }

        System.out.println("minJudge is "+minJudge);
        return(result);
    }
}
