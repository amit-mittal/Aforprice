package stores;

import global.errorhandling.ErrorCodes;
import global.exceptions.Bhagte2BandBajGaya;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FileStore {
	
	private File parentDir;
	private Map<String, PrintWriter> fileMap = new HashMap<String, PrintWriter>();
	private Map<String, Cache> dataCache = new HashMap<String, Cache>();	
	private char lineSeparator;
	private final String EMPTY = "";
	
	public FileStore(String path, char lineSeaparator){
		parentDir = new File(path);
		if(!parentDir.exists())
			if(!parentDir.mkdirs())		
				throw new Bhagte2BandBajGaya(ErrorCodes.INVALID_DIR);
		this.lineSeparator = lineSeaparator;
	}
	public void open(String fileName){
		String key = fileName.toLowerCase();
		if(!fileMap.containsKey(fileName.toLowerCase())){
			File file = getFile(fileName);
			try {
				fileMap.put(key, new PrintWriter(new FileWriter(file, true),  true) );
			} catch (IOException e) {
				throw new Bhagte2BandBajGaya(e);
			}
			dataCache.put(key, new Cache());
		}
	}
	
	public void write(String fileName, List<String> content){
		String key = fileName.toLowerCase();		
		String toWrite = dataCache.get(key).putAndGet(content);
		if(toWrite == EMPTY)
			return;
		PrintWriter writer = fileMap.get(key);
		if(writer == null)
			throw new Bhagte2BandBajGaya(ErrorCodes.SYSTEM_BUG);
		writer.print(toWrite + String.valueOf(lineSeparator));		
	}
	
	public void write(String id, String content){
		List<String> l = new ArrayList<String>(1);
		l.add(content);
		write(id, l);
	}
	
	public void close(String id){
		String key = id.toLowerCase();
		PrintWriter writer = fileMap.get(key);
		fileMap.remove(key);
		if(writer == null)
			throw new Bhagte2BandBajGaya(ErrorCodes.SYSTEM_BUG);
		writer.println(dataCache.get(key).getAndClear());
		dataCache.remove(key);
		writer.close();		
	}
	
	public File getFile(String id){
		return new File(parentDir, id.toLowerCase());
	}
	
	public void closeAll(){		
		Set<String> keys = new HashSet<String>();
		keys.addAll(fileMap.keySet());
		for(String key: keys){
			close(key);
		}
	}
	
	
	private class Cache{
		//TODO: Get these from property file
		private final int TRIGGER_SIZE = 100;
		private final int MAX_WAIT = 30000;//30 seconds
		
		private List<String> data = new ArrayList<String>(TRIGGER_SIZE);
		private long lastModtime;

		/**
		 * Returns the data which needs to be written to file. If the cache is not full yet,
		 * an empty string is returned.
		 * @param toPut Data which needs to be added to cache, or returned along with the cached data
		 * in case a non-storage condition is triggered.
		 * @return Data to be written to file
		 */
		private String putAndGet(List<String> toPut){
			int size = data.size() + toPut.size();
			long timeElapsed = System.currentTimeMillis() - lastModtime;
			lastModtime = System.currentTimeMillis();
			List<String> toReturn = null;
			if( size >= TRIGGER_SIZE || timeElapsed > MAX_WAIT){
				toReturn = new ArrayList<String>(size);
				toReturn.addAll(data);
				toReturn.addAll(toPut);
				data.clear();				
			}
			else
				data.addAll(toPut);			
			if(toReturn != null){
				return toString(toReturn);
			}
						
			return EMPTY;
		}
		
		private String getAndClear(){			
			String all = toString(data);
			data.clear();
			return all;										
		}
		
		private String toString(List<String> data){
			StringBuilder builder = new StringBuilder();			
			for(String line: data){
				builder.append(line + String.valueOf(lineSeparator));
			}
			return builder.toString();
		}
	}
}