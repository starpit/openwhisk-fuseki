import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.util.function.Supplier;
import java.util.Base64;

import org.apache.jena.query.Dataset;
import org.apache.jena.query.DatasetFactory;
import org.apache.jena.sparql.core.DatasetGraph;
import org.apache.jena.sparql.core.DatasetGraphFactory;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.RDFLanguages;
import org.apache.jena.riot.system.StreamRDF;
import org.apache.jena.riot.system.StreamRDFLib;
import org.apache.jena.system.Txn;

public class FusekiWhisk {
    /*static public JsonObject main(JsonObject params) {
	JsonObject response = new JsonObject();
        response.addProperty("greeting", "Hello");
        return response;
	}*/

    static public JsonObject main(JsonObject params) {
	// e.g. "SELECT * { ?s  ?o}"
	String query = new String(Base64.getDecoder().decode(params.getAsJsonPrimitive("query").getAsString()));
	byte[] modelRaw = Base64.getDecoder().decode(params.getAsJsonPrimitive("model").getAsString());
	Lang lang = RDFLanguages.fileExtToLang(params.getAsJsonPrimitive("lang").getAsString());

	System.out.println("QUERY is '" + query + "'");
	System.out.println("Lang is " + lang);

	DatasetGraph dsg = DatasetGraphFactory.createGeneral() ;
	//Dataset ds = DatasetFactory.createMem(); 
	StreamRDF stream = StreamRDFLib.dataset(dsg) ;
	//Model model = ds.getDefaultModel() ;
	RDFDataMgr.parse(stream, new ByteArrayInputStream(modelRaw), lang);

	return Txn.calculateRead(dsg, new Supplier<JsonObject>() {
		public JsonObject get() {
		    Dataset ds = DatasetFactory.wrap(dsg) ;
		    try (QueryExecution qExec = QueryExecutionFactory.create(query, ds) ) {
			    ResultSet rs = qExec.execSelect() ;

			    ByteArrayOutputStream bas = new ByteArrayOutputStream();
			    ResultSetFormatter.outputAsJSON(bas, rs) ;

			    return new JsonParser().parse(bas.toString()).getAsJsonObject();
			}
		}
	    }) ;


	/*DatasetGraph dsg = ... ;
	DataService dataService = new DataService(dsg) ;
	dataService.addEndpoint(OperationName.Query, "");*/


	/*Dataset ds = ...
	    FusekiEmbeddedServer server = FusekiEmbeddedServer.create()
	    .add("/rdf", ds, true)
	    .build() ;
	    server.start() ;*/
    }
}