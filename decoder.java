import java.util.*;
import java.io.*;

class Node{
	int data;
	String codeword;
	Node leftchild;
	Node rightchild;
	Node(){
		data = -1;
		codeword="";
		leftchild=null;
		rightchild=null;
	}
	Node(int d, String s){
		this.data = d;
		this.codeword = s;
		this.leftchild = null;
		this.rightchild = null;
	}
	int getData(){
		return this.data;
	}
	String getCodeword(){
		return this.codeword;
	}
}
class decoderTree{
	Node root;
	private static final String OTFILENAME = "decoded.txt";
	decoderTree(){
		root = new Node();
	}
	boolean isEmpty(){
		return root.data == -1;
	}
	void generateTree(ArrayList<Node> A){
		for(int a=0;a<A.size();a++){
			Node r = this.root;
			String code = A.get(a).getCodeword();
			//traverse through the codeword
			for(int i=0;i<code.length();i++){
				if(code.charAt(i)=='1'){
					if(r.rightchild==null)
						r.rightchild = new Node();
					r=r.rightchild;
				}
				else {
					if(r.leftchild==null)
						r.leftchild = new Node();
					r=r.leftchild;
				}
			}
			r.data = A.get(a).data;
		}
	}
	void decodeMessage(String FILE) throws IOException{
		FileInputStream fis=new FileInputStream(FILE);
		byte[] inbuffer=null;
		inbuffer=new byte[fis.available()];
		fis.read(inbuffer);
		fis.close();
		Node n = this.root;
		DataOutputStream out = new DataOutputStream(new FileOutputStream(OTFILENAME));
		for(int i=0;i<inbuffer.length;i++){
			byte b1 = inbuffer[i];
			String s1 = String.format("%8s", Integer.toBinaryString(b1 & 0xFF)).replace(' ', '0');
			for(int j=0;j<s1.length();j++){
				if(s1.charAt(j)=='0') n=n.leftchild;
				else n=n.rightchild;
				if(n.leftchild==null && n.rightchild==null){
					StringBuffer tem = new StringBuffer();
					tem.append(Integer.toString(n.data)+'\n');
					System.out.print(Integer.toString(n.data)+'\n');
					out.writeBytes(tem.toString());
					n=this.root;
				}
			}
		}
		out.close();
	}
//	Node printoFile(String s, Node n)throws IOException{
//		
//		ArrayList<String> arr = new ArrayList<String>();
//		int j=0;
//		while(j<s.length()){
//			if(n.leftchild==null && n.rightchild==null){
//				StringBuffer tem = new StringBuffer();
//				tem.append(Integer.toString(n.data)+'\n');
//				System.out.print(Integer.toString(n.data)+'\n');
//				out.writeBytes(tem.toString());
//				n=this.root;
//			}
//			else{
//				arr.add(String.valueOf(s.charAt(j)));
//				if(s.charAt(j)=='0') n=n.leftchild;
//				else n=n.rightchild;
//				j++;
//			}
//		}
//		
//		return n;
//	}
}
public class decoder {
	public static void main(String[] args) throws IOException{
//		String enmessage = "C:/UFL/Sem 2/Advanced Data Structures/project/enco.bin";
//		String codetable = "C:/UFL/Sem 2/Advanced Data Structures/project/sample_codetable.txt";
		String enmessage = args[0];
		String codetable = args[1];
		FileReader fr = null;
		BufferedReader br = null;
		fr = new FileReader(codetable);
		br = new BufferedReader(fr);
		String sCurrentLine;
		ArrayList<Node> in = new ArrayList<Node>();
		while ((sCurrentLine = br.readLine()) != null) {
			if(!sCurrentLine.equals("")){
				String[] str = sCurrentLine.split(" ");
				Node n = new Node(Integer.parseInt(str[0]),str[1]);
				in.add(n);
			}
		}	
		br.close();
		decoderTree dt = new decoderTree();
		dt.generateTree(in);
		dt.decodeMessage(enmessage);
	}
}
