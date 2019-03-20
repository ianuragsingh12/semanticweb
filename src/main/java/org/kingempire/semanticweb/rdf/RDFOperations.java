package org.kingempire.semanticweb.rdf;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.RDFFormat;
import org.apache.jena.util.FileManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 *
 * @author Anurag Singh
 */
@Component
public class RDFOperations {

    private final Logger LOG = LoggerFactory.getLogger(getClass());

    public Model mergeTwoModel(Model m1, Model m2) {
        return ModelFactory.createUnion(m1, m2);
    }

    public Model readRDF(String inputFile, String format) throws IOException {
        Model model;
        try (InputStream in = FileManager.get().open(inputFile)) {
            // create an empty model
            model = ModelFactory.createDefaultModel();

            // read the RDF/XML file
            model.read(in, null, format);
            LOG.info("File Reading Done From: " + inputFile);
        }
        return model;
    }

    public void writeRDF(Model model, String format, String outputFile) throws FileNotFoundException, IOException {
        try (OutputStream out = new FileOutputStream(outputFile)) {
            model.write(out, format);
            LOG.info("File Writing Done In " + format + " Format At: " + outputFile);
        }
    }

    public void writeRDF(Model model, String outputFile) throws FileNotFoundException, IOException {
        try (OutputStream out = new FileOutputStream(outputFile)) {
            RDFDataMgr.write(out, model, RDFFormat.TURTLE_PRETTY);
            LOG.info("File Writing Done In TURTLE Format At: " + outputFile);
        }
    }

    public void printRDF(Model model, String format) {
        LOG.info("\n------------< Result Displaying In " + format + " Format >-------------");
        // write it to standard out
        model.write(System.out, format);
        LOG.info("\n-----------------------------------------------------------------------");
    }

    public void IterateRDFModel(Model model) {
        LOG.info("\n---------------------< Result Displaying >-----------------------------");
        StmtIterator it = model.listStatements();

        while (it.hasNext()) {
            Statement stmt = it.nextStatement();

            Resource subject = stmt.getSubject();
            Property predicate = stmt.getPredicate();
            RDFNode object = stmt.getObject();

            System.out.println(subject.toString() + "  " + predicate.toString() + "  " + object.toString());
        }
        LOG.info("\n-----------------------------------------------------------------------");
    }

    public void printResultSet(ResultSet resultset) {
        LOG.info("\n---------------------< Result Displaying >-----------------------------");
        // Output query results    
        ResultSetFormatter.out(System.out, resultset);
        LOG.info("\n-----------------------------------------------------------------------");
    }

    public void writeResultSetAsCSV(ResultSet resultset, String outputFile) throws FileNotFoundException, IOException {
        try (OutputStream out = new FileOutputStream(outputFile)) {
            ResultSetFormatter.outputAsCSV(out, resultset);
            LOG.info("File Writing Done At: " + outputFile);
        }
    }
}
