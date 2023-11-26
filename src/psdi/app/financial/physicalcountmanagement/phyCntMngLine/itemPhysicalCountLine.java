package psdi.app.financial.physicalcountmanagement.phyCntMngLine;

import java.rmi.RemoteException;


import psdi.mbo.Mbo;
import psdi.mbo.MboRemote;
import psdi.mbo.MboSet;
import psdi.mbo.MboSetRemote;
import psdi.server.AppService;
import psdi.util.MXApplicationException;
import psdi.util.MXException;

public class itemPhysicalCountLine extends Mbo {

	public itemPhysicalCountLine(MboSet ms) throws RemoteException {
		super(ms);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void init() throws MXException {
		System.out.println("init in line ");
		try {
		
			MboRemote owner = getOwner();
			System.out.println("owner  = " + owner);
		
			String status= owner.getString("status");
			if (status.equalsIgnoreCase("CAN") || status.equalsIgnoreCase("CLOSE")) {
				System.out.println("status is : " + status);
				setFlag(READONLY, true);
			}
			
			
			super.init();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			

	}
	
	@Override
	public void delete() throws MXException, RemoteException {
		System.out.println("delete ");
		super.delete();
	}
	
	
	public void delete(long accessModifier) throws MXException, RemoteException 
	{
		System.out.println("event delete item ");
		MboRemote owner = getOwner();
		String status = owner.getString("STATUS");
		String[] params = {status,"Delete"}; 
		if(status.equalsIgnoreCase("WAPPR"))
			super.delete(accessModifier);
		else
			throw new MXApplicationException("filtering","deleteitem",params);
	}
	@Override
	public void add() throws MXException, RemoteException {
		System.out.println("event add item ");
		MboRemote owner = getOwner().getOwner();
		System.out.println("owner : " + owner);
		String storeroom = owner.getString("STOREROOM");
		setValue("STOREROOM", storeroom);
		super.add();
	}
	public void deleteAllItemList() throws RemoteException, MXException
	{
		System.out.println(" deleteall  item list");
		MboRemote owner = getOwner();
		String status = owner.getString("STATUS");
		String[] params = {status,"Delete"}; 
		MboSetRemote mboSet = getThisMboSet();
		System.out.println("count of list is " + mboSet.count());
		if(status.equalsIgnoreCase("WAPPR")) {
			for (int i = 0; i < mboSet.count(); i++) {
				mboSet.getMbo(i).delete();
			}
		}else
			throw new MXApplicationException("filtering","deleteitem",params);
	}
	
	
	public void applyPhyCnt(String status) throws RemoteException, MXException {
		System.out.println("applyPhyCnt..........");
		
		MboSetRemote lines = this.getMboSet("PHYCNTAPPLYING");//itemphycntline
		//lines.setOrderBy("INVBALANCESID asc");
		System.out.println("count of lines = " + lines.count());
		System.out.println("itemnum of lines = " + lines.getMbo(0).getString("itemnum")
				+ " and physcnt = " + lines.getMbo(0).getString("newphyscnt"));
		
		MboSetRemote invBalances = this.getMboSet("INVBALANCES");//invbalances
		System.out.println("count of invBalances = " + invBalances.count());
		int count = 0;
		String[] params = {status,"Update"}; 
		if(status.equals("APPR")) {
			
		
		for (int i = 0; i < invBalances.count(); i++) {
			 String LineInvBal = lines.getMbo(count).getString("INVBALANCESID");
			 String InvBalanceID = invBalances.getMbo(i).getString("INVBALANCESID");
			 //System.out.println("line invBal = " + LineInvBal + " and invBal id = " + InvBalanceID);
			 if(LineInvBal.equals(InvBalanceID)) {
				 MboRemote line = lines.getMbo(count);
				 MboRemote invBal = invBalances.getMbo(i);
				 //System.out.println("old phycnt = " + invBalances.getMbo(i).getString("PHYSCNT"));
				 //System.out.println("new phycnt = " + line.getString("NEWPHYSCNT"));
				 //invBal.setFlag(READONLY, false);
				 invBal.setFieldFlag("PHYSCNT", READONLY, false);
				 invBal.setValue("ADJUSTEDPHYSCNT", line.getString("NEWPHYSCNT"));
 				 invBal.setValue("ADJUSTEDPHYSCNTDATE", ((AppService) this.getMboServer()).getMXServer().getDate());
 				 invBal.setValue("NEXTPHYCNTDATE", ((AppService) this.getMboServer()).getMXServer().getDate());
 				 invBal.setValue("PHYSCNT", line.getString("NEWPHYSCNT"));
  				 //invBal.setFieldFlag("PHYSCNT", READONLY, true);
 				 System.out.println("end of condition ...... added " + count);
				 count++;
				 save();
			 } 
		}
		this.save();	
		}else
			throw new MXApplicationException("filtering","deleteitem",params);
	}
	
}
