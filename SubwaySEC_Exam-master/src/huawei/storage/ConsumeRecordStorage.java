package huawei.storage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import huawei.model.Card;
import huawei.model.ConsumeRecord;

public class ConsumeRecordStorage {
	private Map<String, List<ConsumeRecord>> consumeRecordMap = new HashMap<String, List<ConsumeRecord>>();
	
	private static ConsumeRecordStorage instance = new ConsumeRecordStorage();
	
	public static ConsumeRecordStorage getInstance() {
		return instance;
	}

	public void add(Card card, ConsumeRecord consumeRecord) {	
		addById(card.getCardId(), consumeRecord);
	}
	public void addById(String cardId, ConsumeRecord consumeRecord) {	
		List<ConsumeRecord> list = consumeRecordMap.get(cardId);
		if(list == null) {
			list = new ArrayList<ConsumeRecord>();
		}
		list.add(consumeRecord);
		consumeRecordMap.put(cardId, list);
	}
	
	public void delete(Card card) {		
		deleteById(card.getCardId());
	}	
	public void deleteById(String cardId) {
		consumeRecordMap.remove(cardId);
	}
	
	public List<ConsumeRecord> getConsumeRecord(Card card) {
		return getConsumeRecordById(card.getCardId());
	}
	public List<ConsumeRecord> getConsumeRecordById(String cardId) {
		return consumeRecordMap.get(cardId);
	}
	
	public void clear() {
		consumeRecordMap.clear();
	}
}
