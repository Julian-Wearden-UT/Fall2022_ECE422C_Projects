/*
* EE422C Final Project submission by
* Replace <...> with your actual data.
* <Julian Wearden>
* <jfw864>
* <17615>
* Fall 2022
*/

package eHillsServerSide;

import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

public class ReadJsonList {
	
    public static <T> ArrayList<T> readJsonList(Class<T> clazz, String fileName, boolean isItem) 
    {
    	
		ArrayList<T> itemList = new ArrayList<T>();
        //System.out.println("read from " + fileName + ".json:");
        for(Object i : readItemList(clazz, fileName, isItem)) {
//	       	For DEBUG
			//System.out.println(i.toString());
			//System.out.println();
        	itemList.add((T) i);
        }
        return itemList;
    }
    
    private static <T> ArrayList<T> readItemList(Class<T> clazz, String fileName, boolean isItem) {
        ArrayList<T> itemList = new ArrayList<T>();
        try {
            // create a reader of file
            Reader reader = Files.newBufferedReader(Paths.get("files/"+ fileName +".json"));
            
            // read file into Item
            if(isItem) {
                itemList = new Gson().fromJson(reader, new TypeToken<ArrayList<Item>>(){}.getType());
            }
            else {
                itemList = new Gson().fromJson(reader, new TypeToken<ArrayList<User>>(){}.getType());
            }
            
            // close reader
            reader.close();
    
        } catch (Exception ex) {
            ex.printStackTrace();
        } 
        return itemList;
    }
    
}

//package eHillsServerSide;
//
//import java.io.Reader;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//import java.util.ArrayList;
//
//import com.google.gson.*;
//import com.google.gson.reflect.TypeToken;

//public class ReadJsonItemList {
//	
//    public static ArrayList<Item> readJsonList(String fileName) 
//    {
//		ArrayList<Item> itemList = new ArrayList<Item>();
//        System.out.println("read from " + fileName + ".json:");
//        for(Item i : readItemList(fileName)) {
////	       	For DEBUG
////			System.out.println(i.toString());
////			System.out.println();
//        	itemList.add(i);
//        }
//        return itemList;
//    }
//    
//    private static ArrayList<Item> readItemList(String fileName) {
//        ArrayList<Item> itemList = new ArrayList<Item>();
//        try {
//            // create a reader of file
//            Reader reader = Files.newBufferedReader(Paths.get("files/"+ fileName +".json"));
//            
//            // read file into Item
//            itemList = new Gson().fromJson(reader, new TypeToken<ArrayList<Item>>(){}.getType());
//
//            // close reader
//            reader.close();
//    
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        } 
//        return itemList;
//    }
//    
//	private static Item readItem(String fileName) {
//		ArrayList<Item> itemList = new ArrayList<Item>();
//		Item item = new Item();
//		try {
//			// create a reader of file
//			Reader reader = Files.newBufferedReader(Paths.get("files/"+ fileName +".json"));
//		      
//			// read file into Item
//			item = new Gson().fromJson(reader, Item.class);
//		
//			// close reader
//			reader.close();
//	
//		} catch (Exception ex) {
//			ex.printStackTrace();
//		} 
//		return item;
//		}
//	    
//	}
//
