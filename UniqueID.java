

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UniqueID {
	private static  List<Integer> id=new ArrayList<Integer>();
	private static int range=10000;
	private static int index=0;
	static{
		for(int i=0;i<range;i++){
			id.add(i);
		}
		Collections.shuffle(id);
	}
	private UniqueID(){
		
	}
	
	public static int getid(){
		if(index>id.size()-1) index=0;
		return id.get(index++);
	}
}