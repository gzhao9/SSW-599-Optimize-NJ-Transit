package huawei.util;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import huawei.exam.ReturnCodeEnum;
import huawei.exam.SubwayException;
import huawei.model.Card;

public class CardIdCreator {
	private CardIdCreator() {}
	
	private int counter = 0;
	// 回收注销的id以便重新利用
	private Set<Integer> recycleCache = new TreeSet<Integer>();
	private static CardIdCreator instance = new CardIdCreator();
	
	public static CardIdCreator getInstance() {
		return instance;
	}
	
	public String createCardId() throws SubwayException {
		String currentAvaliableId;
		if(recycleCache.isEmpty()) {
			if(counter > 99) {
				// E08:申请的地铁卡超出系统容量
				throw new SubwayException(ReturnCodeEnum.E08, new Card());
			} else {			
				currentAvaliableId = String.valueOf(counter);
				counter++;
			}
		} else {			
			Iterator<Integer> iter = recycleCache.iterator();  
			currentAvaliableId = iter.next().toString();
			iter.remove();
		}
		
		return currentAvaliableId;
	}
	
	public void clearRecord() {
		counter = 0;
		recycleCache.clear();
	}
	
	public void Recycle(String cardId) {
		recycleCache.add(Integer.valueOf(cardId));
	}
}
