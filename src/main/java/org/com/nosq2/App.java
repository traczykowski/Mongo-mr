package org.com.nosq2;

import com.mongodb.*;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;

import java.net.UnknownHostException;

/**
 * Hello world!
 *
 */
public class App 
{
	private static int port = 27017;
	private static String database = "default";
	private static String collection = "default";

    private static String map = "function(){emit(this.artist_name, 1);}";

    private static String reduce = "function(key, values){var value=0;values.forEach(function(count){value+=count;});return value;}";




	
    public static void main( String[] args )
    {
    	
    	try{

            resolveOptions(prepareOptions(), args);
            
            System.out.println(port + " " + database + " " + collection);

            Mongo m = new Mongo( "localhost" , port);
            DB db = m.getDB(database);
            DBCollection coll = db.getCollection(collection);

            MapReduceOutput output = coll.mapReduce(map, reduce, null, MapReduceCommand.OutputType.INLINE, null);

            System.out.println(output.results());
            
            for ( DBObject obj : output.results() ) {
                System.out.println( obj );
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    private static Options prepareOptions(){
    	Options options = new Options();
    	Option portOpt = new Option("p","port", true, "MongoDB port");
    	Option databaseOpt = new Option("d","database", true, "MongoDB database");
    	Option collectionOpt = new Option("c", "collection", true, "MongoDB collection");
    	options.addOption(portOpt);
    	options.addOption(databaseOpt);
    	options.addOption(collectionOpt);
    	return options;
    	
    }
    
    private static void usage(Options options){
    	HelpFormatter formatter = new HelpFormatter();
    	formatter.printHelp( "mongo-mr", options );
    }
    
    private static void resolveOptions(Options options, String[] args) throws org.apache.commons.cli.ParseException{
	    	CommandLineParser cmdParser = new PosixParser();
	    	CommandLine cmd = cmdParser.parse( options, args);
	    	
	    	if(cmd.hasOption("p")) port = Integer.parseInt(cmd.getOptionValue("p"));
	    	if(cmd.hasOption("d")) database = cmd.getOptionValue("d");
	    	if(cmd.hasOption("c")) collection = cmd.getOptionValue("c");

    }
}
