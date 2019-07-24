package br.com.santander.predictbacen.reclameaqui.model;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.util.ArrayList;
import java.util.Iterator;

public class CompanyComplainList {

	public static ArrayList<String> extractComplainList(String jsonString)
	{
		ArrayList<String> complainList = new ArrayList<String>();
		
		JSONParser parser = new JSONParser();

        try 
        {
            JSONObject jsonObject = (JSONObject) parser.parse(jsonString);
            JSONObject complainResult = (JSONObject) jsonObject.get("complainResult");
            if (complainResult == null || complainResult.isEmpty())
            	return complainList;

            JSONObject complains = (JSONObject) complainResult.get("complains");
			if (complains == null || complains.isEmpty())
				return complainList;

            JSONArray data = (JSONArray) complains.get("data");
			if (data == null || data.isEmpty())
				return complainList;
            
            Iterator<Object> iterator = data.iterator();
            while (iterator.hasNext()) 
            {	
				JSONObject complainObject = (JSONObject) iterator.next();

				Complain complain = new Complain();
				String complainJson = complain.getComplainJson(complainObject);
				complain.saveToDatabase(complainObject);

				complainList.add( complainJson );
            }
        } 
        catch (ParseException e) 
        {
            e.printStackTrace();
        }

		return complainList;
	}
	
}
