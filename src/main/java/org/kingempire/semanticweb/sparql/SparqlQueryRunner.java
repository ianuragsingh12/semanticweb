package org.kingempire.semanticweb.sparql;

import java.util.ArrayList;
import java.util.List;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFactory;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 *
 * @author Anurag Singh
 */
@Component
public class SparqlQueryRunner {

    private final Logger LOG = LoggerFactory.getLogger(getClass());

    public <T extends Object> T runSparqlQuery(Model model, String queryString, String queryType, Class<T> type) {
        Query query = QueryFactory.create(queryString);
        try (QueryExecution qe = QueryExecutionFactory.create(query, model)) {
            switch (queryType.trim().toUpperCase()) {
                case "SELECT": {
                    ResultSet r = qe.execSelect();
                    r = ResultSetFactory.copyResults(r);
                    LOG.info(queryType.trim().toUpperCase() + " Query Executed Successfully");
                    return type.cast(r);
                }
                case "CONSTRUCT": {
                    Model m = qe.execConstruct();
                    LOG.info(queryType.trim().toUpperCase() + " Query Executed Successfully");
                    return type.cast(m);
                }
                case "DESCRIBE": {
                    Model m = qe.execDescribe();
                    LOG.info(queryType.trim().toUpperCase() + " Query Executed Successfully");
                    return type.cast(m);
                }
                case "ASK": {
                    Boolean r = qe.execAsk();
                    LOG.info(queryType.trim().toUpperCase() + " Query Executed Successfully");
                    return type.cast(r);
                }
                default: {
                    LOG.info("Invalid Query Type");
                    return null;
                }
            }
        }
    }

    public ResultSet runOneSelectQuery(Model model, String queryString) {
        Query query = QueryFactory.create(queryString);
        ResultSet results;
        try (QueryExecution qe = QueryExecutionFactory.create(query, model)) {
            results = qe.execSelect();
            results = ResultSetFactory.copyResults(results);
        }
        LOG.info("SELECT Query Executed Successfully");
        return results;
    }

    public List<ResultSet> runMultiSelectQuery(Model model, String prefixHeaders, List<String> queries) {
        List<ResultSet> results = new ArrayList<>();
        queries.forEach((q) -> {
            results.add(runOneSelectQuery(model, (prefixHeaders + q)));
        });
        LOG.info(queries.size() + " SELECT Query Executed Successfully");
        return results;
    }

    public Model runOneConstructQuery(Model model, String queryString) {
        Query query = QueryFactory.create(queryString);
        Model results;
        try (QueryExecution qe = QueryExecutionFactory.create(query, model)) {
            results = qe.execConstruct();
        }
        LOG.info("CONSTRUCT Query Executed Successfully");
        return results;
    }

    public Model runMultiConstructQuery(Model model, String prefixHeaders, List<String> queries) {
        Model finalModel = ModelFactory.createDefaultModel();
        for (String q : queries) {
            Model temp = runOneConstructQuery(model, (prefixHeaders + q));
            finalModel = ModelFactory.createUnion(finalModel, temp);
        }
        LOG.info(queries.size() + " CONSTRUCT Query Executed Successfully");
        return finalModel;
    }

    public String readQueryFromFile(String queryFile) {
        return QueryFactory.read(queryFile).toString();
    }
}
