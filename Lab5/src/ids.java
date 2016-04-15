
import javax.swing.JOptionPane;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.jnetpcap.Pcap;
import org.jnetpcap.packet.JPacket;
import org.jnetpcap.packet.JPacketHandler;
import org.jnetpcap.packet.Payload;
import org.jnetpcap.protocol.network.Ip4;
import org.jnetpcap.protocol.tcpip.Tcp;


import java.util.regex.*;


public class ids {
	static int idx ;
	static ArrayList<Boolean> alarm = new ArrayList<Boolean>();
	static String bl,bl2 ;
	
	public static void main(String[] args) {
		alarm.add(false);
		alarm.add(false);
		alarm.add(false);
		alarm.add(false);
		alarm.add(false);
		
		
		
		boolean con = true;
	
		while(con)
		{
			 String n= JOptionPane.showInputDialog("Select Policy:\n1. Blame Attack 1\n2. Blame Attack 2\n3. General Buffer Overflow\n4. Plaintext POP\n5. Simulated  ");
			 
			 if(n == null || n.equals(""))//input != null && input.equals(""))   
             {
				 con = false;
				 break;

             }
			 String n2= JOptionPane.showInputDialog("Select file:\n1.trace1.pcap \n2.trace2.pcap \n3.trace3.pcap \n4.trace4.pcap \n5.trace5.pcap ");
			 if(n2 == null || n2.equals(""))//input != null && input.equals(""))   
             {
				 con = false;
				 break;

             }	
				
		
//	IDS("trace" + n2 + ".pcap", Integer.parseInt(n));
		
		
			IDS("trace"+n2 +".pcap", Integer.parseInt(n));
			//IDS_Statless("trace4.pcap", 4);
		}
		
	
		
		}
			
		
		
		
	
	
	public static void IDS(String n1, int policy)	
	{
		
		//clean Data
		bl="";
		bl2="";
		 alarm.set(0, false);
		 alarm.set(1, false);
		 alarm.set(2, false);
		 alarm.set(3, false);
		 alarm.set(4, false);	
		policy p = new policy(policy)  ;
		
		
		
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		/////read file
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		final String FILENAME = n1;
		final StringBuilder errbuf = new StringBuilder();

		
		final Pcap pcap = Pcap.openOffline(FILENAME, errbuf);
		if (pcap == null) {
			//System.err.println(errbuf); 
			return;
		}
		Map<String, Integer> ScrMap = new HashMap<String, Integer>();
		Map<String, Integer> DesMap = new HashMap<String, Integer>();

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///loop packets
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

		pcap.loop(Pcap.LOOP_INFINATE, new JPacketHandler<StringBuilder>() {
			
			
			final Tcp tcp = new Tcp();
			final Ip4 ip = new Ip4();
				
			public void nextPacket(JPacket packet, StringBuilder errbuf) {
					
				//clean object from  previous Packet data 
				p.CleanP();

				
				
				///content
				StringBuilder str = new StringBuilder();
		        /* convert the whole packet (headers + payloads) to a string. Adjust the first and last parameter if you don't want to look at the whole packet */
		        packet.getUTF8String(0, str, packet.getTotalSize());
		        String rawStringData = str.toString();
		        
		        //add to object 
		        p.payload= rawStringData;
		       bl+= rawStringData;
		        
		 //   System.out.println(bl +"\n\n\n\n");
				/////////////////////
		        
		        
		        
					Payload payload = new Payload();
					
					
					packet.getHeader(tcp);
					
					//System.out.printf("tcp.dst_port=%d%n", tcp.destination());
					//System.out.printf("tcp.src_port=%d%n", tcp.source());
					//System.out.printf("tcp.ack=%x%n", tcp.ack());
					
					
				//System.out.println(packet);
		     
					
		             packet.getHeader(ip);
		             byte[] dIP = new byte[4], sIP = new byte[4];
		             dIP = packet.getHeader(ip).destination();
		             sIP = packet.getHeader(ip).source();
		             String sourceIP = org.jnetpcap.packet.format.FormatUtils.ip(sIP);
		             String destinationIP = org.jnetpcap.packet.format.FormatUtils.ip(dIP);
		          //   System.out.println(sourceIP);
		          //   System.out.println(destinationIP);
		            
		             
		             //add to obj
		             p.cfrom_ip = sourceIP;
		             p.cto_ip = destinationIP;
		           //add to obj
					 p.cfrom_port = tcp.source() + "";
		             p.cto_port = tcp.destination() + "";
		         
/*
 * 
 */////////////
		          
		             
		         //    System.out.println("From: ip: " + p.cattacker + " Port:" + p.cattacker_port + " \nTo :  ip " + p.chost  + " port:"+p.chost_port + "" );
		            
		             if (packet.hasHeader(payload)) {
		            //	 System.out.printf("payload length=%d\n", payload.getLength());
		            	 byte[] payloadContent = payload.getByteArray(0, payload.size());
		            	 String strPayloadContent = new String(payloadContent);
		    	// System.out.println("payload content = [" + strPayloadContent + "] end \n\n");
		            	 
		            	//add payload
		           	 bl2 +=  strPayloadContent;
		            
		             }
		            	 
		            	
			             
		            
		             
		             //**************************************88
		             /////////check policy 
		             //***********************************************
		            
		               switch(policy)
		             {
		             case 1://policy 1
		            	 if (  p.payload.contains(p.to_host) && p.host.equals(p.cto_ip)  && p.host_port.equals( p.cto_port))
			             {
		            		//   System.out.println("p.host == p.chost && p.host_port == p.chost_port :" + p.host + "  " +  p.chost + "  " + p.host_port +  "  " + p.chost_port);
		       		        
			            	 JOptionPane.showMessageDialog(null, p.name); 
			            	bl="";
			            	// System.out.println( p.to_host + "      check         "+strPayloadContent + "\n endddddddddddddddddddddddddddddddddddd \n\n");
			             }
		            	 break;
		             case 2://policy 1
		            	 if (  bl2.contains(p.to_host)&& p.host.equals(p.cto_ip)  && p.host_port.equals( p.cto_port))
			             {
		            		  // System.out.println("p.host.equals(p.cto_ip)  && p.host_port.equals( p.cfrom_port) :" + p.host + "  " +  p.cto_ip + "  " + p.host_port +  "  " + p.cfrom_port);
		       		        
			            	 JOptionPane.showMessageDialog(null, p.name); 
			            	 bl2 = "";
			            	return;
			            	// System.out.println( p.to_host + "      check         "+strPayloadContent + "\n endddddddddddddddddddddddddddddddddddd \n\n");
			             }
		            	 break;
		             case 3://policy 1
		            	 
		            	 Pattern pt3 = Pattern.compile(p.to_host);
		               	 Matcher m3 = pt3.matcher(bl);
		            	 
		            	 if ( m3.find()&& p.host.equals(p.cto_ip)  && p.host_port.equals( p.cto_port))
			             {
		            		//   System.out.println("p.host == p.chost && p.host_port == p.chost_port :" + p.host + "  " +  p.chost + "  " + p.host_port +  "  " + p.chost_port);
		       		        
			            	 JOptionPane.showMessageDialog(null, p.name); 
			            	 bl = "";
			            	return;
			            	// System.out.println( p.to_host + "      check         "+strPayloadContent + "\n endddddddddddddddddddddddddddddddddddd \n\n");
			             }
		            	 break;
		             case 4://policy 4
		            	
		            	// System.out.println("p.host == p.chost && p.host_port == p.chost_port :" + p.host + "  " +  p.chost + "  " + p.host_port +  "  " + p.chost_port);
		            	 String Search = "";
		            	 
		            	 //''search first string  -- from_host="\+OK.*\r\n"
		            	 if (!alarm.get(0))
		            	 {
		            		 //from host - port
		            		// System.out.println("1"+p.cfrom_ip+ p.from_host + "  " + p.cfrom_port);
		            		 Search= p.from_host ;
		            	 }
		            	 else if ( !alarm.get(1))
		            	 {
		            		
		            		 Search= p.to_host ;
		            		 
		            	 }
		            	 else if ( !alarm.get(2))//&& p.host.equals(p.cto_ip)  && p.host_port.equals( p.cto_port)) //first second found search 3rd -- from_host="\+OK.*\r\n"
		            	 {
		            		 
		            		 Search= p.from_host2 ;
		            	 }
		            	 else if ( !alarm.get(3))//&& p.host.equals(p.cto_ip)  && p.host_port.equals( p.cto_port))//first second found  3rd found search 4th --to_host="PASS.*\r\n"
		            	 {
		            		 
		            		 Search= p.to_host2 ;
		            	 }
		            	 else if ( !alarm.get(4))//&& p.host.equals(p.cto_ip)  && p.host_port.equals( p.cto_port)) //search last  -- from_host="\+OK.*\r\n"
		            	 {
		            		 Search= p.from_host3 ;
		            		 
		            	 }
		            	 else
		            	 {
		            		 
		            		
		            	 }
		            	
		            	 
		            	 Pattern pt = Pattern.compile(Search);
		               	 Matcher m = pt.matcher(p.payload);
		            	 
		            	 boolean isFound = m.find();
		            	//System.out.println(m.find());
		            	 if (isFound && ! alarm.get(0)&& p.host.equals(p.cfrom_ip)  && p.host_port.equals( p.cfrom_port))
		            	 {
		            		 alarm.set(0, true);
		            		  
		            	 }
		            	// System.out.println(m.find() &&  alarm.get(0) && !alarm.get(1));
		            	 else if (isFound &&  alarm.get(0) && !alarm.get(1) && p.host.equals(p.cto_ip)  && p.host_port.equals( p.cto_port))
		            	 {
		            		
		            		 alarm.set(1, true);
		            		 
		            	 }
		            	 else if (isFound &&  alarm.get(0) && !alarm.get(2) && p.host.equals(p.cfrom_ip)  && p.host_port.equals( p.cfrom_port))
		            	 {
		            		 alarm.set(2, true);
		            		 
		            	 }
		            	 else if (isFound &&  alarm.get(0) && !alarm.get(3) && p.host.equals(p.cto_ip)  && p.host_port.equals( p.cto_port))
		            	 {
		            		 alarm.set(3, true);
		            		  
		            	 }
		            	 else if (isFound &&  alarm.get(0) &&  alarm.get(1)&&  alarm.get(2)&&  alarm.get(3) &&  !alarm.get(4) && p.host.equals(p.cfrom_ip)  && p.host_port.equals( p.cfrom_port))
		            	 {
		            		 alarm.set(4, true);
		            		 JOptionPane.showMessageDialog(null, p.name);  
		            	 }
		            	 else{}
		            	 break;
		             case 5://policy 4
		            	// System.out.println("p.host == p.chost && p.host_port == p.chost_port :" + p.host + "  " +  p.chost + "  " + p.host_port +  "  " + p.chost_port);
		            	 String Search1 = "";
		            	// System.out.println("p.host.equals(p.cto_ip)  && p.host_port.equals( p.cfrom_port))" + p.host + "  " +  p.cfrom_ip + "  " + p.attacker_port +  "  " + p.cto_port+  "  " + p.cfrom_port);
			            	
		            	 //''search first string  -- from_host="vmlinuz"
		            	 if (!alarm.get(0))//&& p.host.equals(p.cto_ip) )// && p.host_port.equals( p.cfrom_port))
		            	 {
		            		// System.out.println("p.host.equals(p.cto_ip)  && p.host_port.equals( p.cfrom_port))" + p.host + "  " +  p.cfrom_ip + "  " + p.attacker_port +  "  " + p.cto_port+  "  " + p.cfrom_port);
		            		 Search1= p.from_host ;
		            	 }
		            	 else if ( !alarm.get(1))//&& p.host.equals(p.cfrom_ip) ) //first string found search second -- "\x00\x03\x00\x01"
		            	 {
		            		 Search1= p.to_host ;
		            		
		            	 }
		            	
		            	 else
		            	 {
		            		 
		            		
		            	 }
		            	
		            	 
		            	 Pattern pt1 = Pattern.compile(Search1);
		               	 Matcher m1 = pt1.matcher(p.payload);
		            	 
		            	 boolean isFound1 = m1.find();
		            	//System.out.println(m.find());
		            	 if (isFound1 && ! alarm.get(0) && p.host.equals(p.cfrom_ip)&& p.attacker_port.equals( p.attacker_port))
		            	 {
		            		// System.out.println("p.host.equals(p.chost)  && p.attacker_port.equals( p.cattacker_port)" +p.host + " " +p.chost +" " + p.attacker_port+ " " + p.cattacker_port);
		            		 alarm.set(0, true);
		            		  
		            	 }
		            	// System.out.println(m.find() &&  alarm.get(0) && !alarm.get(1));
		            	 else if (isFound1 &&  alarm.get(0) && !alarm.get(1)&& p.host.equals(p.cto_ip)&& p.attacker_port.equals( p.attacker_port))
		            	 {
		            		 alarm.set(1, true);
		            		 JOptionPane.showMessageDialog(null, p.name); 
		            	 }
		            	
		            	 else{}
		            	 break;
		            	 
		            	
		             }
		            	 
		            	
		           		             
		          	
             }

		//	}

		}, errbuf);
	
	

	}
}
 class  policy
{
	 
	 //for policies 
	int id;
	String host;
	String name;
	String type;
	String proto;
	String host_port;
	String attacker_port;
	String attacker;
	String from_host;
	String to_host;
	String from_host2;	
	String to_host2;
	String from_host3;
	
	
	String payload;
	
	// for the packets 
	String cfrom_ip;
	String cfrom_port;
	String cto_port;
	String cto_ip;
	
	
	policy(int id)
	{
		this.id = id;
		
		switch (id) {
        case 1: 
        	host="192.168.0.1";
        	name="Blame Attack 1";
        	type="stateless";
        	proto="tcp";
        	host_port="5551";
        	attacker_port="any";
        	attacker="any";
        	to_host="Now I own your computer";
        	break;
        case 2: 
        	host="192.168.0.1";
        	name="Blame Attack 2" ;
        	type="stateful";
        	host_port="5551";
        	attacker_port="any";
        	attacker="any";
        	to_host="Now I own your computer";
            break;
        case 3:  
        	host="192.168.0.1";
        	name="Buffer Overflow";
        	type="stateful";
        	host_port="5551";
        	attacker_port="any";
        	attacker="any";
        	to_host="\\x90{10}.*\\xcd\\x80";            	
        	break;
        case 4: 
        	host="192.168.0.1";
        	name="Plaintext POP";
        	type="stateless";
        	proto="tcp";
        	host_port="110";
        	attacker_port="any";
        	attacker="any";
        	from_host="\\+OK.*\\r\\n";
        	to_host="USER .*\\r\\n";
        	from_host2="\\+OK.*\\r\\n";
        	to_host2="PASS.*\r\n";
        	from_host3="\\+OK.*\\r\\n";	
        break;
        case 5: 
        	host="192.168.0.1";
        	name="TFTP attacker boot";
        	type="stateless";
        	proto="udp";
        	host_port="any";
        	attacker_port="69";
        	attacker="any";
        	from_host="vmlinuz";
        	to_host="\\x00\\x03\\x00\\x01";
        break;
        default: break;
      
    }
   
			
	}

	public  void CleanP()
	{
		
		payload = "";
		
		 
		  cfrom_ip = "";
			 cfrom_port="";
			 cto_port="";
			 cto_ip="";
			
		
		
	}
  

}

