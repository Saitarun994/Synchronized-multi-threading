/**
 * A program to simulate a match factory using threads
 */

/**
 * @author Sai Tarun Sathyan
 * @author Aarti
 */

/**
 * A class that is used to store the matches and provides methods to  
 * produce and consume matches and boxes 
 */
class matchFactory
{
	int matches;
	int boxes;
	int matchBoxes;
	boolean boxCreated=false;
	int max;
	
	public void getMax(int n)
	{
		this.max =n;
	}
	
	
	public synchronized void putMatches(int n)
	{	
		if(this.matches>90)
		{
			try {wait();} catch (InterruptedException e) {}	
		}

		if(this.matches<90)
		{this.matches+=n;}
		notifyAll();
	}
	
	public synchronized void putBoxes(int n)
	{	
		
		if(this.boxCreated )
		{
			try {wait();} catch (InterruptedException e) {}	
		}

		if(this.boxes<9)
		{
			this.boxes+=n;
		}
		this.boxCreated=true;
		notifyAll();
	}
	
	public synchronized void getMatchBoxes()
	{	
		if(!this.boxCreated)
		{
			//System.err.println(this.matches);  //1st
			try {wait();} catch (InterruptedException e) {}
		}	
		
		if(this.matches>=50)
		{	
			System.out.println("["+this.matches+"]: "+"50 matches obtained "+" box number "+(++this.matchBoxes)+" created");
			this.matches-=50;
			this.boxes-=1;
			this.boxCreated=false;
		}
		if(this.matchBoxes==max) {System.out.println("\n\t\t<<< All boxes created >>>");System.exit(0);}
		notifyAll();
	}
	
}

//<<<<<<<< Start of Threads >>>>>>>>

/**
 * A class that create matches
 */
class matchProducer implements Runnable
{
	matchFactory M;
	
	public matchProducer(matchFactory M)
	{
		this.M=M;
		Thread t= new Thread(this,"matchProducer");
		t.start();
	}
	
	@Override
	public void run() 
	{	
		while(true)
		{
			M.putMatches(1);
			//if(M.boxes == 9) {System.exit(0);}
		}
	}
}

/**
 * A class that create matches boxes
 */
class boxProducer implements Runnable
{
	matchFactory M;

	public boxProducer(matchFactory M)
	{
		this.M=M;
		Thread t= new Thread(this,"boxProducer");
		t.start();
	}
	
	@Override
	public void run() 
	{	
		while(true)
		{
			if(M.matches>50)
			{
				M.putBoxes(1);
			}
		}
	}
}

/**
 * A class that consumes 50 matches and one match box
 */
class consumer implements Runnable
{
	matchFactory M;

	public consumer(matchFactory M)
	{
		this.M=M;
		Thread t= new Thread(this,"consumer");
		t.start();
	}
	@Override
	public void run() 
	{	
		while(true)
		{
			M.getMatchBoxes();
		}
	}
	
}
//<<<<<<<< END of Threads >>>>>>>>


public class matches {
	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		int MatchBoxCount=20;
		if(args.length>0)
		{
			MatchBoxCount=Integer.parseInt(args[0]);
		}
		
		matchFactory M= new matchFactory();
		M.getMax(MatchBoxCount);
		matchProducer P1=new matchProducer(M);
		boxProducer P2=new boxProducer(M);
		consumer C=new consumer(M);

	}

}
