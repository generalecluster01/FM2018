
public class Account
{
	public String name;
	public String screen_name;
	public String id;
	public int nofposts;
	public int nofmentions;
	
	public void set(String name, String screen_name, String id){
		this.name=name;
		this.screen_name=screen_name;
		this.id=id;
		this.nofposts=0;
		this.nofmentions=0;
	}
	public void addpost(){
		nofposts++;
	}
	public int getposts(){
		return nofposts;
	}
	public void addmention(){
		nofmentions++;
	}
	public int getmentions(){
		return nofmentions;
	}
}
