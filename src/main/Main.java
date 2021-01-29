package main;

import java.io.IOException;

import pretreatment.Importer;
import pretreatment.KnowledgeLib;
import pretreatment.RelationshipType;

public class Main {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		/*KnowledgeLib knowledgeLib=new KnowledgeLib();
		knowledgeLib.insert("123","456",RelationshipType.CA_EF);
		knowledgeLib.insert("135","456",RelationshipType.CA_EF);
		System.out.println(knowledgeLib.search("123").gvtSimilarity(knowledgeLib.search("456")));*/

		Service service=new Service();
		service.gotoMainMenu();
	}

}
