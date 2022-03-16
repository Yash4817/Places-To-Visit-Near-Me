import java.io.*;
import java.util.*;
class Pair<A, B>{
	public A First;
	public B Second;
	public Pair()
	{


	}
	public Pair(A _first, B _second) {
        this.First = _first;
        this.Second = _second;
    }
    public A get_first() {
        return First;
    }
    public B get_second() {
        return Second;
    }
}
class Node{
	Node parent;
	Node left;
	Node right;
	int numpoints;
	int X_median;
	int Y_median;	
	boolean isLeaf;
	int xmax;
	int xmin;
	int ymax;
	int ymin;
}

public class kdtree
{
	
	
	
 	public List<Pair<Integer,Integer>> median(List<Pair<Integer,Integer>> p,int k)
	{
		int l = p.size();
		if(l==1){
			return p;
		}
		int i=0;
		if(k%2==0)
		{
			for(i=0;i<l;i++)
			{
				for(int j=0;j<l-i-1;j++)
				{
					if(p.get(j).get_first() > p.get(j+1).get_first())
					{
						Pair<Integer,Integer> temp = p.get(j);
						p.set(j,p.get(j+1));
						p.set(j+1,temp);
					}
				}
			}
		}
		else if(k%2==1)
		{
			for(i=0;i<l;i++)
			{
				for(int j=0;j<l-i-1;j++)
				{
					if(p.get(j).get_second() > p.get(j+1).get_second())
					{
						Pair<Integer,Integer> temp = p.get(j);
						p.set(j,p.get(j+1));
						p.set(j+1,temp);
					}
				}
			}
		}
		return p;
	}
	
	
	
	
	public Node build(List<Pair<Integer,Integer>> p,int k)
	{
		
		Node curr = new Node();
		if(p.size()==0)
		{
			return null;
		}
		if(p.size() == 1)
		{	
			
			curr.numpoints = 1;
			curr.left = null;
			curr.right = null;
			curr.parent = null;
			curr.X_median = p.get(0).get_first();
			curr.Y_median = p.get(0).get_second();
			curr.xmin = curr.X_median;
			curr.xmax = curr.X_median;
			curr.ymin = curr.Y_median;
			curr.ymax = curr.Y_median;
			curr.isLeaf = true;
			return curr;
	
		}
		p = median(p,k);
		int i = 0;
		List<Pair<Integer,Integer>> p1 = new ArrayList<>();
		List<Pair<Integer,Integer>> p2= new ArrayList<>();
		if(k%2==0)
		{
			for(i=0;i<p.size() && p.get(i).get_first() <= p.get((p.size()-1)/2).get_first() ;i++)
			{
				p1.add(p.get(i));
			}
			for(;i<p.size();i++)
			{
				p2.add(p.get(i));
			}
			curr.X_median = p.get((p.size()-1)/2).get_first();
			curr.Y_median = 0;
		}
		else if(k%2==1)
		{
			for(i=0;i<p.size() && p.get(i).get_second() <= p.get((p.size()-1)/2).get_second() ;i++)
			{
				p1.add(p.get(i));
			}
			for(;i<p.size();i++)
			{
				p2.add(p.get(i));
			}
			curr.X_median = 0;
			curr.Y_median = p.get((p.size()-1)/2).get_second();
		}
		
		Node l = new Node();
		Node r = new Node();
		curr.numpoints = p.size();
		l = build(p1,k+1);
		r = build(p2,k+1);
		curr.left = l;
		curr.right = r;
		curr.xmin = Integer.MIN_VALUE;
		curr.xmax = Integer.MAX_VALUE;
		curr.ymin = Integer.MIN_VALUE;
		curr.ymax = Integer.MAX_VALUE;
		curr.isLeaf = false;
		
		if(l!=null)
		{
					l.parent = curr;
					curr.xmin = Math.min(curr.xmin,l.xmin);
					curr.xmax = Math.max(curr.xmax,l.xmax);
					curr.ymin = Math.min(curr.ymin,l.ymin);
					curr.ymax = Math.max(curr.ymax,l.ymax);
		}
		
		if(r!=null)
		{
				r.parent = curr;
				curr.xmin = Math.min(curr.xmin,r.xmin);
				curr.xmax = Math.max(curr.xmax,r.xmax);
				curr.xmin = Math.min(curr.ymin,r.ymin);
				curr.xmax = Math.max(curr.ymax,r.ymax);	
					
	
		}
		
		return curr;
	
	}	

	public int query(Node rootnode,Pair<Integer,Integer> p)
	{
		int count=0;
		if(rootnode == null)
		{
			return 0;
		}
		if(rootnode.isLeaf)
		{
			if(	rootnode.xmin >= p.get_first()-100
				&&		
				rootnode.xmax <= p.get_first()+100		
		  	  	&&
				rootnode.ymin >= p.get_first()-100
				&&
				rootnode.ymax <= p.get_second()+100
				)
				{
					count += 1;
					return count;
				}
			else
			{
				return 0;
			}
		}
		int right=0,left=0;
		if(rootnode.right!=null)
		{
			if(	rootnode.right.xmin >= p.get_first()-100 
		 	&&
			rootnode.right.xmax <= p.get_first()+100
			&&
			rootnode.right.ymin >= p.get_second()-100
			&&
			rootnode.right.ymax <= p.get_second()+100
			
			){	
				right = 1;
				count += rootnode.right.numpoints;  
			}
		}
		if(rootnode.left!=null)
		{
			if(	rootnode.left.xmin>= p.get_first()-100
                        	&&
            	rootnode.left.xmax<=p.get_first()+100
                        	&&
            	rootnode.left.ymin>=p.get_second()-100
            	            &&
            	rootnode.left.ymax<=p.get_second()+100
                        
				){
				left=1;
		       		count += rootnode.left.numpoints;
				}
		}
	   if(rootnode.right!=null)
		{
			if(	rootnode.right.xmin > p.get_first()+100
			||
			rootnode.right.xmax < p.get_first()-100
			||
			rootnode.right.ymin > p.get_second()+100
            ||
            rootnode.right.ymax < p.get_second()-100	
				){
				right = 2;
				count = count;
				}
		}	
		if(rootnode.left !=null)
		{ 
			if( rootnode.left.xmin > p.get_first()+100
                        ||
                rootnode.left.xmax < p.get_first()-100
                        ||
                rootnode.left.ymin > p.get_second()+100
                        ||
                rootnode.left.ymax < p.get_second()-100
                ){
			 			left=2;
                        count = count;
                }
		}
		 if(right == 0){
			count += query(rootnode.right,p);
		  }
		 if(left==0){
		 	count+= query(rootnode.left,p);
		 }
		 return count;
	}
	public List<Pair<Integer,Integer>> read(String f)
    {
        try
        {
            FileInputStream restraunts = new FileInputStream(f);
            Scanner r = new Scanner(restraunts);
            r.nextLine();
            
            List <Pair<Integer,Integer>> l = new ArrayList<>();
            while(r.hasNextLine())
            {
                String[] a = r.nextLine().split(",");
                Pair<Integer,Integer> p = new Pair<Integer,Integer>(Integer.valueOf(a[0]),Integer.valueOf(a[1]));
                l.add(p);
            }
            return l;
            
        }
        catch(Exception e)
        {
            System.out.println("File not found");
            return null;
        }
    }
	public static void main(String[] args)
	{
				kdtree ob = new kdtree();
				Node rootnode = ob.build(ob.read("restaurants.txt"),0);
				int i = 0,count =0;	
				List<Pair<Integer,Integer>> q = new ArrayList<>();
        		q = ob.read("queries.txt");
				try 
				{
					FileOutputStream fs = new FileOutputStream ("output.txt" );
					PrintStream p = new PrintStream ( fs );
					while(i<q.size())
					{
						count = ob.query(rootnode,q.get(i));
						System.out.println(count);
						i++;
						p.println(count);
					 }
					
				} 
				catch ( FileNotFoundException e1 ) 
				{
					System . out . println (" File not found ");
				}
	
	}
}	
	


