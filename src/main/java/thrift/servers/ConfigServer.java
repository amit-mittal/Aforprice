package thrift.servers;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.thrift.TException;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TTransportException;

import thrift.genereated.config.DateObj;
import thrift.genereated.config.Event;
import thrift.genereated.config.ConfigService;
import static thrift.genereated.config.configConstants.CONFIG_SERVER_PORT;
import static thrift.genereated.config.configConstants.EVENTS;

public class ConfigServer implements ConfigService.Iface {
	private static final Logger logger = Logger.getLogger(ConfigServer.class);
	
	/*
	 * @returns 0: date1 same as date2
	 * 			1: date1 before date2
	 * 			2: date1 after date2
	 */
	private int compareDate(DateObj date1, DateObj date2){
		if(date1.getYear()<date2.getYear())
			return -1;
		else if(date1.getYear()>date2.getYear())
			return 1;
		
		if(date1.getMonth()<date2.getMonth())
			return -1;
		else if(date1.getMonth()>date2.getMonth())
			return 1;
		
		if(date1.getDate()<date2.getDate())
			return -1;
		else if(date1.getDate()>date2.getDate())
			return 1;
		
		return 0;
	}
	
	@Override
	public List<Event> getEvents(DateObj startDate, DateObj endDate) throws TException {
		logger.info("getEvents called for ("+startDate+", "+endDate+")");
		List<Event> events = new ArrayList<Event>();
		for(Event event : EVENTS){
			DateObj date = event.getEventDate();
			if(compareDate(date, startDate) != -1)
				if(compareDate(date, endDate) != 1)
					events.add(event);
		}
		return events;
	}
	
	public static void main(String[] args) {
		Integer port = -1;
		Integer instance = -1;
		if(args.length>=1){
			//args format key=value.. serverId=1 instance=1
			for(int i=0; i<args.length; i++){
				logger.info("processing arg: " + args[i]);
				String[] keyVal = args[i].split("=");
				if(keyVal.length!=2)
					throw new IllegalArgumentException("Invalid argument format, must be in key=val format");
				String key = keyVal[0];
				if(key.equalsIgnoreCase("instance")){
					instance = Integer.parseInt(keyVal[1]);
					if( (port=CONFIG_SERVER_PORT.get(instance-1)) == null)
						throw new IllegalArgumentException("instance "+instance+" is missing in CONFIG_SERVER_PORT list");
				}
				else
					logger.warn("Ignoring arg"+keyVal[0]);;
			}//for(int i=0; i<args.length; i++){ ends...
			if( instance==-1)
				throw new IllegalArgumentException("missing argument instance");
			logger.info("Port="+port);
		}//if(args.length>=1) ends...
		
		try {
			TServerSocket serverTransport = new TServerSocket(port);
			ConfigService.Processor processor = new ConfigService.Processor(new ConfigServer());
	        TServer server = new TThreadPoolServer(new TThreadPoolServer.Args(serverTransport).processor(processor));
	        System.out.println("Starting server on port: "+port);
	        server.serve();
		} catch (TTransportException e) {
			e.printStackTrace();
		}
	}
}
