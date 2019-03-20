package org.kingempire.semanticweb.reasoning;

import java.util.List;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.reasoner.Reasoner;
import org.apache.jena.reasoner.rulesys.GenericRuleReasoner;
import org.apache.jena.reasoner.rulesys.Rule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 *
 * @author Anurag Singh
 */
@Component
public class JenaReasoningWithRules {

    private final Logger LOG = LoggerFactory.getLogger(getClass());

    public Model doReasoning(String datasetFIle, String rulesFile) {
        Model model = ModelFactory.createDefaultModel();
        model.read(datasetFIle);
        LOG.info("Dataset Reading Done From: " + datasetFIle);

        List<Rule> rules = Rule.rulesFromURL(rulesFile);
        LOG.info("Rules Reading Done From: " + rulesFile);

        return doReasoning(model, rules);
    }

    public Model doReasoning(Model model, List<Rule> rules) {

        Reasoner reasoner = new GenericRuleReasoner(rules);

        return ModelFactory.createInfModel(reasoner, model);
    }
}
