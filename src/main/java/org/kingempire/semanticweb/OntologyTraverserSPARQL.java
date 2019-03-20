package org.kingempire.semanticweb;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.RDFNode;

/**
 *
 * @author Anurag Singh
 */
public class OntologyTraverserSPARQL {

    public static void readOntology(String file, OntModel model) {
        InputStream in = null;
        try {
            in = new FileInputStream(file);
            model.read(in, "RDF/XML");
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<String> getRoots(OntModel model) {
        List<String> roots = new ArrayList<String>();

        // find all owl:Class entities and filter these which do not have a parent
        String getRootsQuery
                = "SELECT DISTINCT ?s WHERE "
                + "{"
                + "  ?s <http://www.w3.org/2000/01/rdf-schema#subClassOf> <http://www.w3.org/2002/07/owl#Thing> . "
                + "  FILTER ( ?s != <http://www.w3.org/2002/07/owl#Thing> && ?s != <http://www.w3.org/2002/07/owl#Nothing> ) . "
                + "  OPTIONAL { ?s <http://www.w3.org/2000/01/rdf-schema#subClassOf> ?super . "
                + "  FILTER ( ?super != <http://www.w3.org/2002/07/owl#Thing> && ?super != ?s ) } . "
                + "}";

        Query query = QueryFactory.create(getRootsQuery);

        try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
            ResultSet results = qexec.execSelect();
            while (results.hasNext()) {
                QuerySolution soln = results.nextSolution();
                RDFNode sub = soln.get("s");

                if (!sub.isURIResource()) {
                    continue;
                }

                roots.add(sub.toString());
            }
        }

        return roots;
    }

    public static void traverseStart(OntModel model, String entity) {
        // if starting class available
        if (entity != null) {
            traverse(model, entity, new ArrayList<String>(), 0);
        } // get roots and traverse each root
        else {
            List<String> roots = getRoots(model);

            for (int i = 0; i < roots.size(); i++) {
                traverse(model, roots.get(i), new ArrayList<String>(), 0);
            }
        }
    }

    public static void traverse(OntModel model, String entity, List<String> occurs, int depth) {
        if (entity == null) {
            return;
        }

        String queryString = "SELECT ?s WHERE { "
                + "?s <http://www.w3.org/2000/01/rdf-schema#subClassOf> <" + entity + "> . }";

        Query query = QueryFactory.create(queryString);

        if (!occurs.contains(entity)) {
            // print depth times "\t" to retrieve an explorer tree like output
            for (int i = 0; i < depth; i++) {
                System.out.print("\t");
            }
            // print out the URI
            System.out.println(entity);

            try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
                ResultSet results = qexec.execSelect();
                while (results.hasNext()) {
                    QuerySolution soln = results.nextSolution();
                    RDFNode sub = soln.get("s");

                    if (!sub.isURIResource()) {
                        continue;
                    }

                    String str = sub.toString();

                    // push this expression on the occurs list before we recurse to avoid loops
                    occurs.add(entity);
                    // traverse down and increase depth (used for logging tabs)
                    traverse(model, str, occurs, depth + 1);
                    // after traversing the path, remove from occurs list
                    occurs.remove(entity);
                }
            }
        }

    }

    public static void main(String[] args) {
        // create OntModel
        OntModel model = ModelFactory.createOntologyModel();
        // read camera ontology
        readOntology("src/test/resources/camera.owl", model);
        // start traverse
        traverseStart(model, null);
    }

}
