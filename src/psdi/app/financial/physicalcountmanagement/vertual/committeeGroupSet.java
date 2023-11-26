package psdi.app.financial.physicalcountmanagement.vertual;

import java.rmi.RemoteException;

import psdi.mbo.Mbo;
import psdi.mbo.MboServerInterface;
import psdi.mbo.MboSet;
import psdi.mbo.custapp.NonPersistentCustomMboSet;
import psdi.util.MXException;

public class committeeGroupSet extends NonPersistentCustomMboSet {

	public committeeGroupSet(MboServerInterface ms) throws RemoteException {
		super(ms);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Mbo getMboInstance(MboSet ms) throws MXException, RemoteException {
		// TODO Auto-generated method stub
		return new committeeGroup(ms);
	}
	
}
