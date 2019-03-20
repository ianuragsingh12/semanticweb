package org.kingempire.semanticweb;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.ModelFactory;

/**
 *
 * @author Anurag Singh
 */
public class OntologyTraverserAPI {

    public void readOntology(String file, String readingFormat, OntModel model) {
        InputStream in = null;
        try {
            in = new FileInputStream(file);
            model.read(in, readingFormat);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Traverse the Ontology to find all given concepts
     */
    public void traverseStart(OntModel model, OntClass ontClass) {
        // if ontClass is specified we only traverse down that branch
        if (ontClass != null) {
            traverse(ontClass, new ArrayList<OntClass>(), 0);
            return;
        }

        // create an iterator over the root classes
        Iterator<OntClass> i = model.listHierarchyRootClasses();

        // traverse through all roots
        while (i.hasNext()) {
            OntClass tmp = i.next();
            traverse(tmp, new ArrayList<OntClass>(), 0);
        }
    }

    /**
     * Start from a class, then recurse down to the sub-classes. Use occurs
     * check to prevent getting stuck in a loop
     *
     * @param oc OntClass to traverse from
     * @param occurs stores visited nodes
     * @param depth indicates the graph "depth"
     * @return list of concepts / entities which were visited when recursing
     * through the hierarchy (avoid loops)
     */
    private void traverse(OntClass oc, List<OntClass> occurs, int depth) {
        if (oc == null) {
            return;
        }

        // if end reached abort (Thing == root, Nothing == deadlock)
        if (oc.getLocalName() == null || oc.getLocalName().equals("Nothing")) {
            return;
        }

        // print depth times "\t" to retrieve a explorer tree like output
        for (int i = 0; i < depth; i++) {
            System.out.print("\t");
        }

        // print out the OntClass
        System.out.println(oc.toString());

        // check if we already visited this OntClass (avoid loops in graphs)
        if (oc.canAs(OntClass.class) && !occurs.contains(oc)) {
            // for every subClass, traverse down
            for (Iterator<OntClass> i = oc.listSubClasses(true); i.hasNext();) {
                OntClass subClass = i.next();

                // push this expression on the occurs list before we recurse to avoid loops
                occurs.add(oc);
                // traverse down and increase depth (used for logging tabs)
                traverse(subClass, occurs, depth + 1);
                // after traversing the path, remove from occurs list
                occurs.remove(oc);
            }
        }

    }

    public static void main(String[] args) {

        OntologyTraverserAPI o1 = new OntologyTraverserAPI();

        // create OntModel
        OntModel model = ModelFactory.createOntologyModel();
        // read camera ontology
        o1.readOntology("src/test/resources/camera.owl", "RDF/XML", model);
        // start traverse
        o1.traverseStart(model, null);
    }
}
