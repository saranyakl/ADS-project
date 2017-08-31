
import java.io.*;
import java.util.*;



class huffmanNode{
	int data;
	long freq;
	//boolean isLeaf;
	huffmanNode leftchild;
	huffmanNode rightchild;
	huffmanNode(){
		this.data = 0;
		this.freq=0;
		leftchild = null;
		rightchild = null;
	}
	huffmanNode(int data, long freq) {
		this.data = data;
		this.freq = freq;
		leftchild = null;
		rightchild = null;
	}
	int getData(){
		return this.data;
	}
	long getFreq(){
		return this.freq;
	}
}
class PairNode
{
    int data;
    long freq;
    PairNode leftChild;
    PairNode nextSibling;
    PairNode prev;
 
    /* Constructor */
    PairNode(int x,long y)
    {
        data = x;
        freq = y;
        leftChild = null;
        nextSibling = null;
        prev = null;
    }
    PairNode()
    {
        data = 0;
        freq = 0;
        leftChild = null;
        nextSibling = null;
        prev = null;
    }
    
}
class mininode{
	int data;
	long freq;
	mininode(){
		this.data=0;
		this.freq=0;
	}
	mininode(int data, long freq){
		this.data = data;
		this.freq=freq;
	}
	int getKey(){
		return this.data;
	}
	long getFreq(){
		return this.freq;
	}
}
class binaryHeap{
	int getparent(int index){
		int i = index/2;
		return i-1;
	}
	int getLeftchild(int i){
		return 2*i+1;
	}
	int getRightchild(int i){
		return 2*(i+1);
	}
	void minHeapify(ArrayList<mininode> A,int i){
		int l = this.getLeftchild(i);
		int r = this.getRightchild(i);
		int min = i;
		if(A.size()>l && A.get(l).getFreq()<A.get(i).getFreq())
			min=l;
		if(A.size()>r && A.get(r).getFreq()<A.get(min).getFreq())
			min=r;
		if(min!=i){
			Collections.swap(A,min,i);
			this.minHeapify(A, min);
		}
	}
	void buildMinHeap(ArrayList<mininode> A){
		for(int a=(A.size()/2)-1;a>=0;a--){
			this.minHeapify(A, a);
		}
		//for(int b=0;b<A.size()-1;b++){
		//	System.out.println(A.get(b).getKey()+": "+A.get(b).getFreq());
		//}
	}
	mininode extractMin(ArrayList<mininode> A){
		mininode min = A.get(0);
		Collections.swap(A,0,A.size()-1);
		A.remove(A.size()-1);
		this.minHeapify(A, 0);
		return min;
	}
	void buildHuffmanUsingBinaryHeap(ArrayList<mininode> A){
		// extract 2 nodes and add them to the binary heap
		ArrayList<mininode> demo = new ArrayList<mininode>(A);
		this.buildMinHeap(demo);
		
		while(demo.size()>=2){
			mininode n1 = this.extractMin(demo);
			mininode n2 = this.extractMin(demo);
			mininode n3 = new mininode();
			n3.data = 0;
			n3.freq = n1.getFreq()+n2.getFreq();
			demo.add(n3);
			int i = demo.size();
			while(i>1 && demo.get(this.getparent(i)).getFreq() > demo.get(i-1).getFreq() ){
				Collections.swap(demo, i-1, this.getparent(i));
				i=this.getparent(i);
			}
		}
	}
}

//Pairing Heap
class pairingHeap{
	PairNode root;
	PairNode [ ] treeArray = new PairNode[ 5 ];
	pairingHeap( )
    {
        root = null;
    }
	boolean isEmpty(){
		return root==null;
	}
	void insert(int x, long y)
    {
        PairNode newNode = new PairNode(x,y);
        if (root == null)
            root = newNode;
        else
            root = compareAndLink(root, newNode);
        //return newNode;
    }
	private PairNode compareAndLink(PairNode first, PairNode second)
    {
        if (second == null)
            return first;
 
        if (second.freq < first.freq)
        {
            /* Attach first as leftmost child of second */
            second.prev = first.prev;
            first.prev = second;
            first.nextSibling = second.leftChild;
            if (first.nextSibling != null)
                first.nextSibling.prev = first;
            second.leftChild = first;
            return second;
        }
        else
        {
            /* Attach second as leftmost child of first */
            second.prev = first;
            first.nextSibling = second.nextSibling;
            if (first.nextSibling != null)
                first.nextSibling.prev = first;
            second.nextSibling = first.leftChild;
            if (second.nextSibling != null)
                second.nextSibling.prev = second;
            first.leftChild = second;
            return first;
        }
    }
	private PairNode combineSiblings(PairNode firstSibling)
    {
        if( firstSibling.nextSibling == null )
            return firstSibling;
        /* Store the subtrees in an array */
        int numSiblings = 0;
        for ( ; firstSibling != null; numSiblings++)
        {
            treeArray = doubleIfFull( treeArray, numSiblings );
            treeArray[ numSiblings ] = firstSibling;
            /* break links */
            firstSibling.prev.nextSibling = null;  
            firstSibling = firstSibling.nextSibling;
        }
        treeArray = doubleIfFull( treeArray, numSiblings );
        treeArray[ numSiblings ] = null;
        /* Combine subtrees two at a time, going left to right */
        int i = 0;
        for ( ; i + 1 < numSiblings; i += 2)
            treeArray[ i ] = compareAndLink(treeArray[i], treeArray[i + 1]);
        int j = i - 2;
        /* j has the result of last compareAndLink */
        /* If an odd number of trees, get the last one */
        if (j == numSiblings - 3)
            treeArray[ j ] = compareAndLink( treeArray[ j ], treeArray[ j + 2 ] );
        /* Now go right to left, merging last tree with */
        /* next to last. The result becomes the new last */
        for ( ; j >= 2; j -= 2)
            treeArray[j - 2] = compareAndLink(treeArray[j-2], treeArray[j]);
        return treeArray[0];
    }
    private PairNode[] doubleIfFull(PairNode[ ] array, int index)
    {
        if (index == array.length)
        {
            PairNode [ ] oldArray = array;
            array = new PairNode[index * 2];
            for( int i = 0; i < index; i++ )
                array[i] = oldArray[i];
        }
        return array;
    }
    /* Delete min element */
    public long deleteMin( )
    {
        if (isEmpty( ) )
            return -1;
        long x = root.freq;
        if (root.leftChild == null)
            root = null;
        else
            root = combineSiblings( root.leftChild );
        return x;
    }
    private void inorder(PairNode r)
    {
        if (r != null)
        {
            inorder(r.leftChild);
            System.out.print(r.freq +" ");
            inorder(r.nextSibling);
        }
        
    }
    void buildHuffmanusingPairingheap(ArrayList<mininode> A){
    	for(int i=0;i<A.size();i++){
    		this.insert(A.get(i).getKey(), A.get(i).getFreq());
    	}
    	
    	while(root.leftChild != null){
    		long x1 = this.deleteMin();
    		long x2 = this.deleteMin();
    		
    		this.insert(0, x1+x2);
    		
    	}
    	
    }
}


class fourWayHeap{
	int getchild1(int i){
		return 4*(i-2); // using cache 4(i-3)+4
	}
	int getchild2(int i){
		return 4*(i-2)+1; // using cache 4(i-3)+5
	}
	int getchild3(int i){
		return 4*(i-2)+2; // using cache 4(i-3)+6
	}
	int getchild4(int i){
		return 4*(i-2)+3; // using cache 4(i-3)+7
	}
	int getparent(int i){
		return ((i-1)/4)+2;
	}
	void minheapifyFourway(ArrayList<huffmanNode> A, int i){
		int c1 = 4*(i-2);
		int c2 = 4*(i-2)+1;
		int c3 = 4*(i-2)+2;
		int c4 = 4*(i-2)+3;
		int min = i;
		if(A.size()>c1 && A.get(c1).getFreq() < A.get(i).getFreq()){
			min=c1;
		}
		if(A.size()>c2 && A.get(c2).getFreq()<A.get(min).getFreq())
			min=c2;
		if(A.size()>c3 && A.get(c3).getFreq()<A.get(min).getFreq())
			min=c3;
		if(A.size()>c4 && A.get(c4).getFreq()<A.get(min).getFreq())
			min=c4;
		if(min!=i){
			Collections.swap(A,min,i);
			this.minheapifyFourway(A,min);
		}
	}
	huffmanNode extractminFourway(ArrayList<huffmanNode> A){
		huffmanNode min = A.get(3);
		Collections.swap(A,3,A.size()-1);
		A.remove(A.size()-1);
		this.minheapifyFourway(A, 3);
		return min;
		
	}
	void buildFourwayheap(ArrayList<huffmanNode> A){
		for(int a=((A.size()-1)/4)+2;a>=3;a--){
			this.minheapifyFourway(A, a);
		}
	}
	ArrayList<huffmanNode> buildHuffmanusingFourway(ArrayList<huffmanNode> A){
		ArrayList<huffmanNode> demo = new ArrayList<huffmanNode>(A);
		this.buildFourwayheap(demo);
		
		while(demo.size()>=5){
			huffmanNode n1 = this.extractminFourway(demo);
			huffmanNode n2 = this.extractminFourway(demo);
			//System.out.println(n1.getFreq()+", "+n2.getFreq());
			huffmanNode n3 = new huffmanNode(0,n1.getFreq()+n2.getFreq());
			n3.leftchild = n1;
			n3.rightchild = n2;
			demo.add(n3);
			int i = demo.size();
			while(i>1 && demo.get(this.getparent(i)).getFreq() > demo.get(i-1).getFreq() ){
				Collections.swap(demo, i-1, this.getparent(i));
				i=this.getparent(i);
			}
		}
		return demo;
	}
	void getcodeTable(huffmanNode node, int array[], int size, int lr){
		if(node==null)
			return;
		if(lr!=-1){
			array[size]=lr;
			size++;
		}
		if(node.leftchild==null && node.rightchild==null){
			this.storeinHash(node,array,size);
		}
		else{
			getcodeTable(node.leftchild, array, size, 0);
			getcodeTable(node.rightchild, array, size, 1);
		}
	}
	void storeinHash(huffmanNode node, int[] arr, int size){
		StringBuffer sBuffer = new StringBuffer("");
		for (int i = 0; i < size; i++) 
        {
			sBuffer.append(Integer.toString(arr[i]));
        }
		Adsproject.codetable.put(Integer.valueOf(node.getData()), sBuffer.toString());
	}
}
public class Adsproject {
	public static HashMap<Integer,String> codetable = new HashMap<Integer,String>();
//	private static final String FILENAME = "C:/UFL/Sem 2/Advanced Data Structures/project/sample_input_large.txt";
//	private static final String OTFILENAME = "C:/UFL/Sem 2/Advanced Data Structures/project/sample_codetable.txt";
//	private static final String EncodeOFILENAME = "C:/UFL/Sem 2/Advanced Data Structures/project/enco.bin";
//	private static final String DecodeOFILENAME = "C:/UFL/Sem 2/Advanced Data Structures/project/deco.txt";
	public static void main(String args[]) throws IOException {
		String FILENAME = args[0];
		FileReader fr = null;
		BufferedReader br = null;
		BufferedReader br2 = null;
		HashMap<Integer,Long> hm = new HashMap<Integer,Long>();
		ArrayList<mininode> arr = new ArrayList<mininode>();
		ArrayList<mininode> ph;
		
		try{
			fr = new FileReader(FILENAME);
			br = new BufferedReader(fr);
			br2 = new BufferedReader(fr);
			String sCurrentLine;

			br = new BufferedReader(new FileReader(FILENAME));
			while ((sCurrentLine = br.readLine()) != null) {
				if(!sCurrentLine.equals("")){
					if(!hm.containsKey(Integer.parseInt(sCurrentLine))){
						hm.put(Integer.parseInt(sCurrentLine),(long)1);
					}
					else {
						hm.put(Integer.parseInt(sCurrentLine), (long)hm.get(Integer.parseInt(sCurrentLine))+1);
					}
				}
			}	
			
			for (Integer key : hm.keySet()) {
				   mininode m = new mininode();
				   m.data = key;
				   m.freq = hm.get(key);
				   arr.add(m);
				}
				ArrayList<mininode> bh;
				//TODO clear hashmap from memory
				bh=arr;
				binaryHeap binh = new binaryHeap();
				fourWayHeap fourh = new fourWayHeap();
				pairingHeap pairh = new pairingHeap();
				
				ArrayList<huffmanNode> fwh = new ArrayList<huffmanNode>();
				for(int i=0;i<arr.size();i++){
					huffmanNode hn = new huffmanNode(arr.get(i).getKey(),arr.get(i).getFreq());
					fwh.add(hn);
				}
				huffmanNode dummy = new huffmanNode();
				fwh.add(0, dummy);
				fwh.add(0, dummy);
				fwh.add(0, dummy);
				
				ph=arr;
				
				
				long startTime;
				long stopTime;
				
				
				startTime = System.currentTimeMillis();
				for(int i = 0; i < 10; i++){    //run 10 times on given data set 
					ArrayList<huffmanNode> tree = fourh.buildHuffmanusingFourway(fwh);
				}
				stopTime = System.currentTimeMillis();
				System.out.println(stopTime-startTime+" MilliSec using fourway heap");
				
				
				startTime = System.currentTimeMillis();
				for(int i = 0; i < 10; i++){    //run 10 times on given data set 
					pairh.buildHuffmanusingPairingheap(ph);
				}
				stopTime = System.currentTimeMillis();
				System.out.println(stopTime-startTime+" MilliSec using pairing heap");
				
				startTime = System.currentTimeMillis();
				for(int i = 0; i < 10; i++){    //run 10 times on given data set 
					binh.buildHuffmanUsingBinaryHeap(bh);
				}
				stopTime = System.currentTimeMillis();
				System.out.println(stopTime-startTime+" MilliSec using binary heap");
		}
		catch(IOException e){
			e.printStackTrace();
		}
		finally{
			try {

				if (br != null)
					br.close();
				if (br2 != null)
					br2.close();
				if (fr != null)
					fr.close();
				

			} catch (IOException ex) {

				ex.printStackTrace();

			}
		}
		
		
	}
	
}
