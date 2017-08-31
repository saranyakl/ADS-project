import java.util.*;
import java.io.*;

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
		
		//System.out.print(demo.get(0).getFreq()+" ");
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
		//System.out.println(demo.get(0).getFreq());
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
		encoder.codetable.put(Integer.valueOf(node.getData()), sBuffer.toString());
//		System.out.println(node.getData()+":"+Adsproject.codetable.get(Integer.valueOf(node.getData())));
	}
}
public class encoder {
	public static HashMap<Integer,String> codetable = new HashMap<Integer,String>();
//	private static final String FILENAME = "C:/UFL/Sem 2/Advanced Data Structures/project/sample_input_small.txt";
	private static final String OTFILENAME = "code_table.txt";
	private static final String EncodeOFILENAME = "encoded.bin";
	
	public static void main(String[] args){
		String FILENAME = args[0];
		FileReader fr = null;
		BufferedReader br = null;
		BufferedReader br2 = null;
		HashMap<Integer,Long> hm = new HashMap<Integer,Long>();
		ArrayList<mininode> arr = new ArrayList<mininode>();
		long startTime;
		long stopTime;
		startTime = System.currentTimeMillis();
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
				   //System.out.println(key+": "+hm.get(key));
				   mininode m = new mininode();
				   m.data = key;
				   m.freq = hm.get(key);
				   arr.add(m);
				}
		fourWayHeap fourh = new fourWayHeap();
		ArrayList<huffmanNode> fwh = new ArrayList<huffmanNode>();
		for(int i=0;i<arr.size();i++){
			huffmanNode hn = new huffmanNode(arr.get(i).getKey(),arr.get(i).getFreq());
			fwh.add(hn);
		}
		huffmanNode dummy = new huffmanNode();
		fwh.add(0, dummy);
		fwh.add(0, dummy);
		fwh.add(0, dummy);
		
		ArrayList<huffmanNode> tree = fourh.buildHuffmanusingFourway(fwh);
		int[] array=new int[999999];
		fourh.getcodeTable(tree.get(3),array,0,-1);
		
		//Encoder
		
		DataOutputStream otpt = new DataOutputStream(new FileOutputStream(OTFILENAME));
		for(Integer key : codetable.keySet()) {
			   StringBuffer tem = new StringBuffer();
			   tem.append(Integer.toString(key));
			   tem.append(" " + codetable.get(key));
			   tem.append('\n');
//			   String nl = System.getProperty("line.separator");
			   otpt.writeBytes(tem.toString());
//			   otpt.writeChars(nl);
		}
		stopTime = System.currentTimeMillis();
		DataOutputStream ot = new DataOutputStream(new FileOutputStream(EncodeOFILENAME));
		StringBuffer out = new StringBuffer();
		br2 = new BufferedReader(new FileReader(FILENAME));
		while ((sCurrentLine = br2.readLine()) != null) {
			if(!sCurrentLine.equals("")){
				String st = codetable.get(Integer.parseInt(sCurrentLine));
				out.append(st);
			}
		}
		for(int i=0;i<out.length();i=i+8){
			String tempS=out.substring(i, i+8);
			int tempInt= Integer.parseInt(tempS, 2);
			//System.out.println(tempInt);
			ot.write(tempInt);
		}
		System.out.println(stopTime-startTime+" MilliSec using fourway heap for encoding");
		ot.close();
		otpt.close();
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
