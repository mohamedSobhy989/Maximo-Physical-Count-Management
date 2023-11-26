package psdi.app.financial.physicalcountmanagement.vertual;

import java.rmi.RemoteException;

import psdi.app.common.virtual.ChangeStatusSet;
import psdi.mbo.Mbo;
import psdi.mbo.MboRemote;
import psdi.mbo.MboServerInterface;
import psdi.mbo.MboSet;
import psdi.mbo.MboSetRemote;
import psdi.mbo.custapp.NonPersistentCustomMboSet;
import psdi.util.MXException;

public class PhyCntChgStatusSet extends ChangeStatusSet {

	public PhyCntChgStatusSet(MboServerInterface ms) throws MXException, RemoteException {
		super(ms);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected MboSetRemote getMboIntoSet(MboRemote arg0) throws MXException, RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Mbo getMboInstance(MboSet arg0) throws MXException, RemoteException {
		// TODO Auto-generated method stub
		return new PhyCntChgStatus(arg0);
	}

	 

}
