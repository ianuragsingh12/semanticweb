package org.kingempire.semanticweb;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.RDFFormat;
import org.kingempire.semanticweb.rdf.RDFOperations;
import org.kingempire.semanticweb.reasoning.JenaReasoningWithRules;
import org.kingempire.semanticweb.sparql.SparqlQueryRunner;

/**
 *
 * @author Anurag Singh
 */
public class NewClass {

    RDFOperations o1 = new RDFOperations();
    SparqlQueryRunner o2 = new SparqlQueryRunner();
    JenaReasoningWithRules o3 = new JenaReasoningWithRules();

    public void main2() throws IOException {
        System.out.println(getClass() + ".main2");
    }

    public static void main(String[] args) throws IOException {

        NewClass o = new NewClass();
        o.f12();
    }

    //working
    public void f12() throws IOException {
        String data = "C:\\Users\\Anurag Singh\\OneDrive\\cfiles\\rdf\\t2.n3";
        Model m1 = o1.readRDF(data, "N3");

        String qf = "C:\\Users\\Anurag Singh\\OneDrive\\cfiles\\rdf\\qr.rq";
        String q = o2.readQueryFromFile(qf);
        System.out.println(q);

//        ResultSet rs1 = o2.runOneSelectQuery(m1, q);
        ResultSet rs1 = o2.runSparqlQuery(m1, q, "SELECT", ResultSet.class);

        String o = "C:\\Users\\Anurag Singh\\OneDrive\\cfiles\\rdf\\f12.csv";
        o1.writeResultSetAsCSV(rs1, o);
        o1.printResultSet(rs1);
    }

    //working
    public void f13() throws IOException {
        String data = "C:\\Users\\Anurag Singh\\OneDrive\\cfiles\\rdf\\t2.n3";
        Model m1 = o1.readRDF(data, "N3");

        List<String> qs = new ArrayList<>();
        qs.add(queries.sq1);
        qs.add(queries.sq2);

        List<ResultSet> rs1 = o2.runMultiSelectQuery(m1, queries.h, qs);

        String o = "C:\\Users\\Anurag Singh\\OneDrive\\cfiles\\rdf\\";
        for (int i = 0; i < rs1.size(); i++) {

            o1.writeResultSetAsCSV(rs1.get(i), o + "file_" + i + ".csv");
            o1.printResultSet(rs1.get(i));
        }
    }

    //working
    public void f14() throws IOException {
        String data = "C:\\Users\\Anurag Singh\\OneDrive\\cfiles\\rdf\\t2.n3";
        Model m1 = o1.readRDF(data, "N3");

        String qf = "C:\\Users\\Anurag Singh\\OneDrive\\cfiles\\rdf\\qr.rq";
        String q = o2.readQueryFromFile(qf);
        System.out.println(q);

//        Model rs1 = o2.runOneConstructQuery(m1, q);
        Model rs1 = o2.runSparqlQuery(m1, q, "CONSTRUCT", Model.class);

        o1.printRDF(rs1, "N3");

        String o = "C:\\Users\\Anurag Singh\\OneDrive\\cfiles\\rdf\\f14.n3";
        o1.writeRDF(rs1, o);

        Model m4 = o1.mergeTwoModel(rs1, m1);
        String mmm = "C:\\Users\\Anurag Singh\\OneDrive\\cfiles\\rdf\\mmm.n3";
        o1.writeRDF(m4, mmm);
        o1.printRDF(m4, "N3");
    }

    //working
    public void f15() throws IOException {
        String data = "C:\\Users\\Anurag Singh\\OneDrive\\cfiles\\rdf\\t2.n3";
        Model m1 = o1.readRDF(data, "N3");

        List<String> qs = new ArrayList<>();
        qs.add(queries.cq1);
        qs.add(queries.cq2);

        Model res = o2.runMultiConstructQuery(m1, queries.h, qs);

        String o = "C:\\Users\\Anurag Singh\\OneDrive\\cfiles\\rdf\\f15.n3";
        o1.printRDF(res, "N3");
        o1.writeRDF(res, o);

        Model m4 = o1.mergeTwoModel(res, m1);
        String mmm = "C:\\Users\\Anurag Singh\\OneDrive\\cfiles\\rdf\\newres.n3";
        o1.writeRDF(m4, mmm);
        o1.printRDF(m4, "N3");
    }

    public void f11() {
        String f = "C:\\Users\\Anurag Singh\\OneDrive\\cfiles\\rdf\\t1.rdf";
        String ff = "C:\\Users\\Anurag Singh\\OneDrive\\cfiles\\rdf\\t1.n3";
        String fff = "C:\\Users\\Anurag Singh\\OneDrive\\cfiles\\rdf\\t1.ttl";
        try {
            Model m = o1.readRDF(f, "RDF/XML");
            o1.printRDF(m, "TTL");
//            o1.writeRDF(m, "N3", ff);
            o1.writeRDF(m, fff);
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public void f1() throws IOException {
        String file = "C:\\Users\\Anurag Singh\\OneDrive\\cfiles\\d2rq\\db_dump.nt";
        String of = "C:\\Users\\Anurag Singh\\OneDrive\\cfiles\\d2rq\\";

        Model m = o1.readRDF(file, "N-TRIPLES");
//        o.printRDF(m, "RDF/XML");
//        o.writeRDF(m, of);

        OutputStream out = new FileOutputStream(of + "f1.ttl");
        RDFDataMgr.write(out, m, RDFFormat.NQUADS);

//        out = new FileOutputStream(of+"f2.ttl");
//        RDFDataMgr.write(out, m, RDFFormat.NQ);
////        
//        out = new FileOutputStream(of+"f3.ttl");
//        RDFDataMgr.write(out, m, RDFFormat.NQUADS_ASCII);
////        
//        out = new FileOutputStream(of+"f4.ttl");
//        RDFDataMgr.write(out, m, RDFFormat.NQUADS_UTF8);
//        
//        out = new FileOutputStream(of+"f5.ttl");
//        RDFDataMgr.write(out, m, RDFFormat.TTL);
//        
//        out = new FileOutputStream(of+"f6.ttl");
//        RDFDataMgr.write(out, m, RDFFormat.JSONLD);
//        
//        out = new FileOutputStream(of+"f7.ttl");
//        RDFDataMgr.write(out, m, RDFFormat.NTRIPLES);
//        
//        out = new FileOutputStream(of+"f8.ttl");
//        RDFDataMgr.write(out, m, RDFFormat.NT);
//        
//        out = new FileOutputStream(of+"f9.ttl");
//        RDFDataMgr.write(out, m, RDFFormat.TRIG);
        out.close();

    }

    public void f2() {
        String dataset = "src/test/resources/dataset.rdf";
        String rules = "src/test/resources/rules.txt";

        JenaReasoningWithRules obj = new JenaReasoningWithRules();
        Model mm = obj.doReasoning(dataset, rules);
        new RDFOperations().printRDF(mm, "N3");
    }
}
