package org.kingempire.semanticweb;

/*
 *  Creates an RDFS inferencing repository
 *  Loads the film ontology which declares Actor and Director as rdfs:subTypeOf Person
 *  Runs a query to see what individuals are typed Person
 */
import java.util.List;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.kingempire.semanticweb.ontology.SimpleGraph;

//import org.openrdf.model.URI;
//import org.openrdf.model.Value;
public class TypeTest {

    public static void main(String[] args) {

        //create a graph with type inferencing
        SimpleGraph g = new SimpleGraph(true);

        String d1 = "C:\\Users\\Anurag Singh\\OneDrive\\cfiles\\rdf\\data.ttl";
        String d2 = "D:\\9780596153823-master\\Programming-the-Semantic-Web\\chapter6\\film-ontology.owl";

        //load the film schema and the example data
        g.addFile(d1, RDFFormat.RDFXML);

        String q1 = "SELECT ?who WHERE  { "
                + "?who <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://semprog.com/Person> ."
                + "}";
        
        String q2 = "SELECT ?who,?y WHERE  { "
                + "?who <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> ?y ."
                + "}";

        List solutions = g.runSPARQL(q1);
        System.out.println("SPARQL solutions: " + solutions.toString());
    }
}
