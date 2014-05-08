package entities;

import java.util.Date;

import util.UtilityLogger;

public class DailyRecon
{
	private String id;
	private String name;
	private Date runDate;
	private String type;
	private String text;
	private int count;
	private Date lastModified;
	
	public int getexpectedProductCount()
	{
		int expectedProductCount = -1;
		try
		{
			expectedProductCount = Integer.parseInt(text);
		}
		catch(Exception e)
		{
			UtilityLogger.logWarning("Can't parse text details to int");
		}
	
		return expectedProductCount;
	}
	
	public int getActualProductCount()
	{
		return count;
	}
	
	public DailyRecon(String id, String name, Date runDate, String type, String text, int count, Date lastModified)
	{
		this.id = id;
		this.name = name;
		this.runDate = runDate;
		this.type = type;
		this.text = text;
		this.count = count;
		this.lastModified = lastModified;
	}
	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id = id;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public Date getRunDate()
	{
		return runDate;
	}
	public void setRunDate(Date runDate)
	{
		this.runDate = runDate;
	}
	public String getType()
	{
		return type;
	}
	public void setType(String type)
	{
		this.type = type;
	}
	public String getText()
	{
		return text;
	}
	public void setText(String text)
	{
		this.text = text;
	}
	public int getCount()
	{
		return count;
	}
	public void setCount(int count)
	{
		this.count = count;
	}
	public Date getLastModified()
	{
		return lastModified;
	}
	public void setLastModified(Date lastModified)
	{
		this.lastModified = lastModified;
	}
	

}
