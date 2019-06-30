package org.kingempire.semanticweb.ontology;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import org.eclipse.rdf4j.model.BNode;
import org.eclipse.rdf4j.model.Literal;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.URI;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.GraphQuery;
import org.eclipse.rdf4j.query.MalformedQueryException;
import org.eclipse.rdf4j.query.QueryEvaluationException;
import org.eclipse.rdf4j.query.QueryLanguage;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.RepositoryException;
import org.eclipse.rdf4j.repository.RepositoryResult;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.RDFHandlerException;
import org.eclipse.rdf4j.rio.RDFParseException;
import org.eclipse.rdf4j.rio.RDFWriter;
import org.eclipse.rdf4j.rio.Rio;
import org.eclipse.rdf4j.rio.UnsupportedRDFormatException;
import org.eclipse.rdf4j.sail.inferencer.fc.ForwardChainingRDFSInferencer;
import org.eclipse.rdf4j.sail.memory.MemoryStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Anurag Singh
 */
public class SimpleGraph {

    private final Logger LOG = LoggerFactory.getLogger(getClass());
    Repository therepository = null;

    /**
     * In memory Sesame repository without type inferencing
     */
    public SimpleGraph() {
        this(false);
    }

    /**
     * In memory Sesame Repository with optional inferencing
     *
     * @param inferencing
     */
    public SimpleGraph(boolean inferencing) {
        try {
            if (inferencing) {
                therepository = new SailRepository(new ForwardChainingRDFSInferencer(new MemoryStore()));

            } else {
                therepository = new SailRepository(new MemoryStore());
            }
            therepository.initialize();
        } catch (RepositoryException e) {
            LOG.error(e.toString());
        }
    }

    /**
     * Literal factory
     *
     * @param s the literal value
     * @param typeuri uri representing the type (generally xsd)
     * @return
     */
    public Literal Literal(String s, URI typeuri) {
        try {
            try (RepositoryConnection con = therepository.getConnection()) {
                ValueFactory vf = con.getValueFactory();
                if (typeuri == null) {
                    return vf.createLiteral(s);
                } else {
                    return vf.createLiteral(s, typeuri);
                }
            }
        } catch (RepositoryException e) {
            LOG.error(e.toString());
            return null;
        }
    }

    /**
     * Untyped Literal factory
     *
     * @param s the literal
     * @return
     */
    public Literal Literal(String s) {
        return Literal(s, null);
    }

    /**
     * URIref factory
     *
     * @param uri
     * @return
     */
    public URI URIref(String uri) {
        try {
            try (RepositoryConnection con = therepository.getConnection()) {
                ValueFactory vf = con.getValueFactory();
                return vf.createURI(uri);
            }
        } catch (RepositoryException e) {
            LOG.error(e.toString());
            return null;
        }
    }

    /**
     * BlankNode factory
     *
     * @return
     */
    public BNode bnode() {
        try {
            try (RepositoryConnection con = therepository.getConnection()) {
                ValueFactory vf = con.getValueFactory();
                return vf.createBNode();
            }
        } catch (RepositoryException e) {
            LOG.error(e.toString());
            return null;
        }
    }

    /**
     * dump RDF graph
     *
     * @param out output stream for the serialization
     * @param outform the RDF serialization format for the dump
     */
    public void dumpRDF(OutputStream out, RDFFormat outform) {
        try {
            try (RepositoryConnection con = therepository.getConnection()) {
                RDFWriter w = Rio.createWriter(outform, out);
                con.export(w, new Resource[0]);
            }
        } catch (RepositoryException | RDFHandlerException | UnsupportedRDFormatException e) {
            LOG.error(e.toString());
        }
    }

    /**
     * Convenience URI import for RDF/XML sources
     *
     * @param urlstring absolute URI of the data source
     */
    public void addURI(String urlstring) {
        addURI(urlstring, RDFFormat.RDFXML);
    }

    /**
     * Import data from URI source Request is made with proper HTTP ACCEPT
     * header and will follow redirects for proper LOD source negotiation
     *
     * @param urlstring absolute URI of the data source
     * @param format RDF format to request/parse from data source
     */
    public void addURI(String urlstring, RDFFormat format) {
        try {
            try (RepositoryConnection con = therepository.getConnection()) {
                URL url = new URL(urlstring);
                URLConnection uricon = (URLConnection) url.openConnection();
                uricon.addRequestProperty("accept", format.getDefaultMIMEType());
                InputStream instream = uricon.getInputStream();
                con.add(instream, urlstring, format, new Resource[0]);
            }
        } catch (IOException | RepositoryException | RDFParseException e) {
            LOG.error(e.toString());
        }
    }

    /**
     * Import RDF data from a string
     *
     * @param rdfstring string with RDF data
     * @param format RDF format of the string (used to select parser)
     */
    public void addString(String rdfstring, RDFFormat format) {
        try {
            try (RepositoryConnection con = therepository.getConnection()) {
                StringReader sr = new StringReader(rdfstring);
                con.add(sr, "", format, new Resource[0]);
            }
        } catch (IOException | RepositoryException | RDFParseException e) {
            LOG.error(e.toString());
        }
    }

    /**
     * Import RDF data from a file
     *
     * @param filepath of file (/path/file) with RDF data
     * @param format RDF format of the string (used to select parser)
     */
    public void addFile(String filepath, RDFFormat format) {
        try {
            try (RepositoryConnection con = therepository.getConnection()) {
                con.add(new File(filepath), "", format, new Resource[0]);
            }
        } catch (IOException | RepositoryException | RDFParseException e) {
            LOG.error(e.toString());
        }
    }

    /**
     * Insert Triple/Statement into graph
     *
     * @param s subject uriref
     * @param p predicate uriref
     * @param o value object (URIref or Literal)
     */
    public void addTriple(URI s, URI p, Value o) {
        try {
            try (RepositoryConnection con = therepository.getConnection()) {
                ValueFactory myFactory = con.getValueFactory();
                Statement st = myFactory.createStatement((Resource) s, p, (Value) o);
                con.add(st, new Resource[0]);
            }
        } catch (RepositoryException e) {
            LOG.error(e.toString());
        }
    }

    /**
     * Triple pattern query - find all statements with the pattern, where null
     * is a wild card
     *
     * @param s subject (null for wildcard)
     * @param p predicate (null for wildcard)
     * @param o object (null for wildcard)
     * @return serialized graph of results
     */
    public List findTriple(URI s, URI p, Value o) {
        try {
            try (RepositoryConnection con = therepository.getConnection()) {
                RepositoryResult repres = con.getStatements(s, p, o, true, new Resource[0]);
                ArrayList reslist = new ArrayList();
                while (repres.hasNext()) {
                    reslist.add(repres.next());
                }
                return reslist;
            }
        } catch (RepositoryException e) {
            LOG.error(e.toString());
        }
        return null;
    }

    /**
     * Execute a CONSTRUCT/DESCRIBE SPARQL query against the graph
     *
     * @param qs CONSTRUCT or DESCRIBE SPARQL query
     * @param format the serialization format for the returned graph
     * @return serialized graph of results
     */
    public String runSPARQL(String qs, RDFFormat format) {
        try {
            try (RepositoryConnection con = therepository.getConnection()) {
                GraphQuery query = con.prepareGraphQuery(QueryLanguage.SPARQL, qs);
                StringWriter stringout = new StringWriter();
                RDFWriter w = Rio.createWriter(format, stringout);
                query.evaluate(w);
                return stringout.toString();
            }
        } catch (MalformedQueryException | QueryEvaluationException | RepositoryException | RDFHandlerException | UnsupportedRDFormatException e) {
            LOG.error(e.toString());
        }
        return null;
    }

    /**
     * Execute a SELECT SPARQL query against the graph
     *
     * @param qs SELECT SPARQL query
     * @return list of solutions, each containing a hashmap of bindings
     */
    public List runSPARQL(String qs) {
        try {
            try (RepositoryConnection con = therepository.getConnection()) {
                TupleQuery query = con.prepareTupleQuery(QueryLanguage.SPARQL, qs);
                TupleQueryResult qres = query.evaluate();
                ArrayList reslist = new ArrayList();
                while (qres.hasNext()) {
                    BindingSet b = qres.next();
                    Set names = b.getBindingNames();
                    HashMap hm = new HashMap();
                    names.forEach((n) -> {
                        hm.put((String) n, b.getValue((String) n));
                    });
                    reslist.add(hm);
                }
                return reslist;
            }
        } catch (MalformedQueryException | QueryEvaluationException | RepositoryException e) {
            LOG.error(e.toString());
        }
        return null;
    }
}
