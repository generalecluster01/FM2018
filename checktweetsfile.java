import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;
import java.util.Vector;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class checktweetsfile {

  
  public static int UpdateAccountPosts(String name, String screen_name, String id, Vector<Account> usersfitname){
	  for(int i=0;i<usersfitname.size();i++){
		  if(usersfitname.get(i).id.equals(id)){
			  System.out.println("Name found in DB");
			  usersfitname.get(i).addpost();
			return 1;			  
		  }
	  }
	  System.out.println("Not found, add it");
	  Account LocAccount= new Account();
	  LocAccount.set(name, screen_name, id);
	  LocAccount.addpost();
	  usersfitname.add(LocAccount);
	  return 0;	
  } 
  public static int UpdateAccountMentions(String name, String screen_name, String id, Vector<Account> usersfitname){
	  for(int i=0;i<usersfitname.size();i++){
		  if(usersfitname.get(i).id.equals(id)){
			  System.out.println("	Name found in DB, add mention!");
			  usersfitname.get(i).addmention();
			return 1;			  
		  }
	  }
	  System.out.println("	Not found, add it and mention!");
	  Account LocAccount= new Account();
	  LocAccount.set(name, screen_name, id);
	  LocAccount.addmention();
	  usersfitname.add(LocAccount);
	  return 0;	
  } 

  
	@SuppressWarnings("unchecked")
  public static void main(String[] argv) throws IOException {
	  // ----------- Internal variables ---------------------------------
	  int nargin = argv.length;				// number of input arguments
	  int status;
	  Vector<String> names = new Vector(); 	// Names to looking for
	  //int nnames;							// Number of names to find
	  String FILENAME;
	  JSONObject obj;
	  String line = null;
	  Vector<Integer> ids = new Vector();
	  Vector<Integer> noftweets = new Vector();
	  Vector<Integer> nofmentions = new Vector();
	  Vector<Account> usersfitname = new Vector<Account>();
	  System.out.println(usersfitname.size());

	  // ----------------------------------------------------------------
	  
	  // ----------------------------------------------------------------
	System.out.println("----------------------------------------------------------------------------");
	System.out.println("----------------- Welcome to program call FM 2018 --------------------------");
	System.out.println("----------------------------------------------------------------------------");

	  // ----- Check if the file name is an input -----------------------
	  if(nargin>0){
		FILENAME= argv[0];
		System.out.println("You pass file as argument:	"+FILENAME);
	  }else{
		FILENAME= "tweets.gz"; 
		System.out.println("No input file name, use default one:	"+FILENAME);		
	  }
      // ----- Check the name input -----------------------	  
	  if(nargin>1){
		 for(int ii=1; ii<nargin; ii++){
			names.add(argv[ii].toLowerCase()); // Convert input to lower case
		 }			 
	  }else{ 
		names.add("alex");
	  };
	  System.out.println("You are looking for " + names.size() +" names :"+names);
	  System.out.println("Total number of Arguments: " + nargin);
	  
	System.out.println("----------------------------------------------------------------------------");
	System.out.println("----------------- LOG  OF THE READING PROCESS ------------------------------");
	System.out.println("----------------------------------------------------------------------------");

	try{
			// Unzip the file
			FileInputStream fin = new FileInputStream(FILENAME);
			GZIPInputStream gzis = new GZIPInputStream(fin);
			InputStreamReader xover = new InputStreamReader(gzis);
			BufferedReader bufferedReader = new BufferedReader(xover);

			int NumberOfData=0;
				while((line = bufferedReader.readLine()) != null) {
					obj = (JSONObject) new JSONParser().parse(line);
					JSONObject user= (JSONObject)obj.get("user");
					JSONObject entities= (JSONObject)obj.get("entities");

					JSONArray user_mentions= (JSONArray)entities.get("user_mentions");
					for(int i=0; i<user_mentions.size(); ++i){
						JSONObject mention = (JSONObject)user_mentions.get(i);
						String locmentname =(String) mention.get("name");
						if(names.get(0).equals(locmentname.toLowerCase())){
							System.out.println("	Mention Found! 	Name="+locmentname+" id= "+mention.get("id"));	
							status=UpdateAccountMentions((String) mention.get("name"),(String) mention.get("screen_name"),(String) mention.get("id_str"),usersfitname);
						}
					}
					String locname =(String) user.get("name");
					if(names.get(0).equals(locname.toLowerCase())){
						System.out.println("Found! 	Ind=" + NumberOfData + "	| name="+locname+"	| id="+user.get("id_str")+"	| id="+user.get("screen_name"));
						status=UpdateAccountPosts(locname,(String) user.get("screen_name"),(String) user.get("id_str"),usersfitname);
					}
					NumberOfData++;
					// For debug only reduce the number of json data
					//if(NumberOfData>10){
						//System.out.println("Locname="+locname+" lc "+locname.toLowerCase()+ " Compare with "+names.get(0));
						//System.out.println(names.get(0).equals(locname.toLowerCase()));
						//break;
					//}
			}
			bufferedReader.close();
			System.out.println("Number of data: " + NumberOfData);
			// ----------------------------------------------------------------------------------------  
			// ----------------------------------------------------------------------------------------  
			// Final prints of the results
			System.out.println("----------------------------------------------------------------------------");
			System.out.println("-----------------ACCOUNTS WITH AT LEAST ONE TWEET --------------------------");
			System.out.println("----------------------------------------------------------------------------");
			System.out.println("Name                 Id                  Screen_Name    #tweets   #mentions");
			for(int i=0;i<usersfitname.size();i++){
				if(usersfitname.get(i).nofposts>0){
					System.out.println(usersfitname.get(i).name+"   "+String.format("%1$20s",usersfitname.get(i).id)+"	@"+String.format("%1$20s",usersfitname.get(i).screen_name)+"	   "+usersfitname.get(i).nofposts+"           "+usersfitname.get(i).nofmentions);
				}
			}
			// Print also accounts that are mentioned but they don't tweet anything in the file		
			System.out.println("----------------------------------------------------------------------------");
			System.out.println("----------------- MENTIONED ACCOUNTS WITH NO TWEETS ------------------------");	
			System.out.println("----------------------------------------------------------------------------");			
			System.out.println("Name                 Id                  Screen_Name    #tweets   #mentions");
			for(int i=0;i<usersfitname.size();i++){
				if(usersfitname.get(i).nofposts==0){
					System.out.println(usersfitname.get(i).name+"   "+String.format("%1$20s",usersfitname.get(i).id)+"	@"+String.format("%1$20s",usersfitname.get(i).screen_name)+"	   "+usersfitname.get(i).nofposts+"           "+usersfitname.get(i).nofmentions);
				}
			}				
} catch (Exception e) {
    e.printStackTrace();
}

}
}