package huawei.storage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import huawei.exam.CardEnum;
import huawei.exam.ReturnCodeEnum;
import huawei.exam.SubwayException;
import huawei.model.Card;
import huawei.model.ConsumeRecord;
import huawei.util.CardIdCreator;

public class CardInfoStorage {
	private CardInfoStorage() {}
	
	private Map<String, Card> cardObjMap = new HashMap<String, Card>();
	private static CardInfoStorage instance = new CardInfoStorage();
	
	public static CardInfoStorage getInstance() {
		return instance;
	}
	
	public void save(Card card) {
		cardObjMap.put(card.getCardId(), card);
	}
	
	public Card delete(Card card) throws SubwayException {
		return deleteById(card.getCardId());	
	}
	public Card deleteById(String cardId) throws SubwayException {
		if(containsId(cardId)) {
			CardIdCreator.getInstance().Recycle(cardId);
			return cardObjMap.remove(cardId);
		} else {			
			// E06:无效的地铁卡	
			throw new SubwayException(ReturnCodeEnum.E06, null);
		}
	}
	
	public Card get(Card card) throws SubwayException {
		return getById(card.getCardId());
	}
	public Card getById(String cardId) throws SubwayException {
		if(containsId(cardId)) {
			return cardObjMap.get(cardId);
		} else {			
			// E06:无效的地铁卡
			Card card = new Card();
			card.setCardId(cardId);
			card.setCardType(CardEnum.E);
			throw new SubwayException(ReturnCodeEnum.E06, card);
		}
	}
	
	public void saveConsumeRecord(Card card, ConsumeRecord consumeRecord) throws SubwayException {
		saveConsumeRecordById(card.getCardId(), consumeRecord);
	}
	public void saveConsumeRecordById(String cardId, ConsumeRecord consumeRecord) throws SubwayException {
		if(containsId(cardId)) {	
			ConsumeRecordStorage.getInstance().addById(cardId, consumeRecord);
		} else {			
			// E06:无效的地铁卡
			Card card = new Card();
			card.setCardId(cardId);
			card.setCardType(CardEnum.E);
			throw new SubwayException(ReturnCodeEnum.E06, card);
		}
	}
	
	public List<ConsumeRecord> getConsumeRecord(Card card) throws SubwayException {
		return getConsumeRecordById(card.getCardId());
	}
	public List<ConsumeRecord> getConsumeRecordById(String cardId) throws SubwayException {
		if(containsId(cardId)) {	
			return ConsumeRecordStorage.getInstance().getConsumeRecordById(cardId);
		} else {			
			// E06:无效的地铁卡
			Card card = new Card();
			card.setCardId(cardId);
			card.setCardType(CardEnum.E);
			throw new SubwayException(ReturnCodeEnum.E06, card);
		}
	}
	
	public boolean containsId(String cardId) {
		return cardObjMap.containsKey(cardId);
	}
	
	public void clear() {
		cardObjMap.clear();
		ConsumeRecordStorage.getInstance().clear();
	}
}
