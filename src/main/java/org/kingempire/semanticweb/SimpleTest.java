package org.kingempire.semanticweb;

import java.util.List;
import org.eclipse.rdf4j.model.URI;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.kingempire.semanticweb.ontology.SimpleGraph;
//import org.openrdf.model.URI;
//import org.openrdf.model.Value;

public class SimpleTest {

    public static void main(String[] args) {

        //a test of graph operations
        SimpleGraph g = new SimpleGraph();

        //get LOD from a URI -  Jamie's FOAF profile from Hi5
//        g.addURI("http://api.hi5.com/rest/profile/foaf/241087912");
//        g.addURI("https://www.w3.org/2000/01/rdf-schema");
        String data = "C:\\Users\\Anurag Singh\\OneDrive\\cfiles\\rdf\\data.ttl";
        g.addFile(data, RDFFormat.TURTLE);

        //manually add a triple/statement with a URIref object
        URI s1 = g.URIref("http://semprog.com/people/toby");
        URI p1 = g.URIref(RDF.TYPE.toString());
        URI o1 = g.URIref("http://xmlns.com/foaf/0.1/person");
        g.addTriple(s1, p1, o1);

        //manually add with an object literal
        URI s2 = g.URIref("http://semprog.com/people/toby");
        URI p2 = g.URIref("http://xmlns.com/foaf/0.1/nick");
        Value o2 = g.Literal("kiwitobes");
        g.addTriple(s2, p2, o2);

        //parse a string of RDF and add to the graph
//        String rdfstring = "<http://semprog.com/people/jamie> <http://xmlns.com/foaf/0.1/nick> \"jt\" .";
//        g.addString(rdfstring, RDFFormat.NTRIPLES);
//        System.out.println("\n==TUPLE QUERY==\n");
//        List rlist = g.findTriple(null, g.URIref("http://xmlns.com/foaf/0.1/nick"), null);
//        rlist.forEach(System.out::println);
        //dump the graph in the specified format
        System.out.println("\n==GRAPH DUMP==\n");
        g.dumpRDF(System.out, RDFFormat.NTRIPLES);

        System.out.println("\n==SPARQL SELECT 22 ==\n");
        String q = "SELECT ?x\n"
                + " WHERE {\n"
                + " ?x <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://example.com/profession#Professor>\n"
                + " }";
        List s11 = g.runSPARQL(q);
        s11.forEach(System.out::println);

        //run a SPARQL query - get back solution bindings
        System.out.println("\n==SPARQL SELECT==\n");
        List solutions = g.runSPARQL("SELECT *"
                + "WHERE { "
                + "?s <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> ?o . "
                //                + "?x <http://xmlns.com/foaf/0.1/nick> ?who ."
                //                + "?y <http://xmlns.com/foaf/0.1/nick> ?nick ."
                + "}");
        System.out.println("SPARQL solutions: " + solutions.toString());

        //run a CONSTUCT SPARQL query 
//        System.out.println("\n==SPARQL CONSTRUCT==\n");
//        String newgraphxml = g.runSPARQL("CONSTRUCT { ?x <http://semprog.com/simple#friend> ?nick . } "
//                + "WHERE { "
//                + "?x <http://xmlns.com/foaf/0.1/knows> ?y . "
//                + "?x <http://xmlns.com/foaf/0.1/nick> ?who ."
//                + "?y <http://xmlns.com/foaf/0.1/nick> ?nick ."
//                + "}", RDFFormat.RDFXML);
//        System.out.println("SPARQL solutions: \n" + newgraphxml);
        //run a CONSTUCT SPARQL query 
//        System.out.println("\n==SPARQL DESCRIBE==\n");
//        String describexml = g.runSPARQL("DESCRIBE ?x  "
//                + "WHERE { "
//                + "?x <http://xmlns.com/foaf/0.1/knows> ?y . "
//                + "?x <http://xmlns.com/foaf/0.1/nick> ?who ."
//                + "?y <http://xmlns.com/foaf/0.1/nick> ?nick ."
//                + "}", RDFFormat.N3);
//        System.out.println("SPARQL solutions: \n" + describexml);
    }

}
