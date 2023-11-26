package psdi.app.financial.physicalcountmanagement.committee;

import java.rmi.RemoteException;

import psdi.mbo.Mbo;
import psdi.mbo.MboRemote;
import psdi.mbo.MboSet;
import psdi.mbo.MboSetRemote;
import psdi.util.MXApplicationException;
import psdi.util.MXException;

public class itemPhyCntCommittee extends Mbo {

	public itemPhyCntCommittee(MboSet ms) throws RemoteException {
		super(ms);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void init() throws MXException {
		 
			super.init();
  
	}
	
	@Override
	public void add() throws MXException, RemoteException {
		System.out.println("add user ");
		this.setValue("committeeid", this.getOwner().getString("committeeid"));
		System.out.println(this.getOwner().getString("committeeid"));
		System.out.println("added");
		super.add(); 
	}

	
	@Override
	public void delete() throws MXException, RemoteException {
		System.out.println("Delete user ");  
			super.delete(); 
	}

	 
	public void deleteItem() throws RemoteException, MXException
	{
		System.out.println("event delete item "); 
			super.delete(); 
	}
	
}
