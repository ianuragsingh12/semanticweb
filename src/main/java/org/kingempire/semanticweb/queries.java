package org.kingempire.semanticweb;

/**
 *
 * @author Anurag Singh
 */
public class queries {

    public static String h = "PREFIX foaf: <http://xmlns.com/foaf/0.1/>";

    public static String sq1 = "SELECT ?name ?depiction\n"
            + "WHERE { \n"
            + "        ?person foaf:name ?depiction .\n"
            + "      }";

    public static String sq2 = "SELECT ?name ?depiction\n"
            + "WHERE { \n"
            + "        ?person foaf:name ?name .\n"
            + "        OPTIONAL {\n"
            + "            ?person foaf:depiction ?depiction .\n"
            + "        } .\n"
            + "      }";

    public static String cq1 = "CONSTRUCT{\n"
            + "    ?person foaf:age 18.\n"
            + "}\n"
            + "WHERE { \n"
            + "        ?person foaf:name ?name .\n"
            + "      }";

    public static String cq2 = "CONSTRUCT{\n"
            + "    ?person foaf:salary 10000000.\n"
            + "}\n"
            + "WHERE { \n"
            + "        ?person foaf:name ?name .\n"
            + "      }";
}
