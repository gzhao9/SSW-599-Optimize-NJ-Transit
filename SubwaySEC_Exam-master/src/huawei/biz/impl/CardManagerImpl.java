package huawei.biz.impl;

import huawei.biz.CardManager;
import huawei.exam.CardEnum;
import huawei.exam.ReturnCodeEnum;
import huawei.exam.SubwayException;
import huawei.model.Card;
import huawei.model.ConsumeRecord;
import huawei.storage.CardInfoStorage;
import huawei.util.BillingCounter;
import huawei.util.CardIdCreator;

import java.util.List;

/**
 * <p>Title: 待考生实现类</p>
 *
 * <p>Description: 卡票中心</p>
 *
 * <p>Copyright: Copyright (c) 2013</p>
 *
 * <p>Company: </p>
 *
 * @author
 * @version 1.0 OperationCenter V100R002C20, 2015/9/7]
 */
public class CardManagerImpl implements CardManager
{
	private final static int MAX_FUND = 999;
	private final static int MIN_FUND = 0;
	private final static int WARNING_FUND = 20;
	
	public static int getMaxFund() {
		return MAX_FUND;
	}
	public static int getMinFund() {
		return MIN_FUND;
	}	
	public static int getWarningFund() {
		return WARNING_FUND;
	}
	
    @Override
    public Card buyCard(String enterStation, String exitStation)
        throws SubwayException
    {
        //TODO 待考生实现
    	Card card = new Card();
        
    	int enterStationNum = Integer.parseInt(enterStation.substring(1));
    	int exitStationNum = Integer.parseInt(exitStation.substring(1));
    	
    	// E01:无效的地铁线路
    	if(enterStationNum < 0 || exitStationNum > 38 ||
			enterStationNum == 9 || exitStationNum == 9 ||
			enterStationNum == 19 || exitStationNum == 19 ||
			enterStationNum == 29 || exitStationNum == 29) {
    		throw new SubwayException(ReturnCodeEnum.E01, card);
    	}
    	
    	// 设置card
    	CardIdCreator cardIdCreator = CardIdCreator.getInstance();
    	card.setCardId(cardIdCreator.createCardId());
    	card.setCardType(CardEnum.A);
    	
    	//  计算并设置乘车总费用
    	int billing = BillingCounter.calculateBilling(CardEnum.A, enterStation, null, exitStation, null);
		card.setMoney(billing);

    	// 保存card对象
    	CardInfoStorage cardInfoStorage= CardInfoStorage.getInstance();
    	cardInfoStorage.save(card);
    	
    	return card;
    }

    @Override
    public Card buyCard(CardEnum cardEnum, int money) throws SubwayException {
        //TODO 待考生实现
    	Card card = new Card();
    	
    	CardIdCreator cardIdCreator = CardIdCreator.getInstance();
    	card.setCardId(cardIdCreator.createCardId());
    	card.setCardType(cardEnum);
    	
    	// 学生卡, 办卡或充值时, 每充值50元送10元
    	if(cardEnum == CardEnum.D) {
    		int remainder = money % 50;
    		money += remainder * 10;
    	}
    	
    	if(money >= MIN_FUND && money <= MAX_FUND) {
        	card.setMoney(money);
    	} else {
    		// E10:办卡总余额超过上限
//    		throw new SubwayException(ReturnCodeEnum.E10, card);
    	}
    	
    	// 保存card对象
    	CardInfoStorage cardInfoStorage= CardInfoStorage.getInstance();
    	cardInfoStorage.save(card);
    	
        return card;
    }

    @Override
    public Card recharge(String cardId, int money)
        throws SubwayException
    {
        //TODO 待考生实现
    	CardInfoStorage cardInfoStorage= CardInfoStorage.getInstance();
    	Card card = cardInfoStorage.getById(cardId);
    	
    	// 学生卡, 办卡或充值时, 每充值50元送10元
    	if(card.getCardType() == CardEnum.D) {
    		int remainder = money / 50;
    		money += remainder * 10;
    	}
    	
    	int newMoney = card.getMoney() + money;
    	if(newMoney >= MIN_FUND && newMoney <= MAX_FUND) {
        	card.setMoney(newMoney);
    	} else {
    		// E10:充值后总余额超过上限
//        		throw new SubwayException(ReturnCodeEnum.E10, card);
    	}
    	
    	// 保存card对象
    	cardInfoStorage.save(card);
    	
        return card;
    }

    @Override
    public Card queryCard(String cardId) throws SubwayException
    {
    	//TODO 待考生实现
    	CardInfoStorage cardInfoStorage= CardInfoStorage.getInstance();
    	return cardInfoStorage.getById(cardId);
    }

    @Override
    public Card deleteCard(String cardId)
        throws SubwayException
    {
        //TODO 待考生实现
    	CardInfoStorage cardInfoStorage= CardInfoStorage.getInstance();
 
        return cardInfoStorage.deleteById(cardId);
    }

    @Override
    public Card consume(String cardId, int billing)
        throws SubwayException
    {
        //TODO 待考生实现
    	CardInfoStorage cardInfoStorage= CardInfoStorage.getInstance();
    	Card card = cardInfoStorage.getById(cardId);
    	
		if(card.getMoney() >= billing ) {
			card.setMoney(card.getMoney() - billing);
			
			// 保存card对象
        	cardInfoStorage.save(card);
		} else {
        	// E02:余额不足
			throw new SubwayException(ReturnCodeEnum.E02, card);
		}
		
		if(card.getMoney() < WARNING_FUND) {
    		// E03:余额过低, 请及时充值
			throw new SubwayException(ReturnCodeEnum.E03, card);
		}
		
		return card;
    	
    }

    @Override
    public List<ConsumeRecord> queryConsumeRecord(String cardId)
        throws SubwayException
    {
        //TODO 待考生实现
        return CardInfoStorage.getInstance().getConsumeRecordById(cardId);
    }
}